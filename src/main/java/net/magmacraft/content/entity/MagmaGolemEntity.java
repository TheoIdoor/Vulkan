package net.magmacraft.content.entity;

import net.magmacraft.registry.ModBlocks;
import net.magmacraft.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MagmaGolemEntity extends Monster {
    private static final EntityDataAccessor<Boolean> DATA_AWAKE =
            SynchedEntityData.defineId(MagmaGolemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_AWAKENING_TICKS =
            SynchedEntityData.defineId(MagmaGolemEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_PHASE =
            SynchedEntityData.defineId(MagmaGolemEntity.class, EntityDataSerializers.INT);

    public final AnimationState awakenAnimationState = new AnimationState();

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.translatable("entity.magmacraft.magma_golem"),
            BossEvent.BossBarColor.RED,
            BossEvent.BossBarOverlay.NOTCHED_10
    );

    private int specialAttackCooldown = 80;
    private int minionSummonCooldown = 200;

    public MagmaGolemEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        this.xpReward = 250;
        this.setMaxUpStep(1.5F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 350.0D)
                .add(Attributes.ATTACK_DAMAGE, 14.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.24D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ARMOR, 14.0D)
                .add(Attributes.FOLLOW_RANGE, 36.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_AWAKE, false);
        this.entityData.define(DATA_AWAKENING_TICKS, 0);
        this.entityData.define(DATA_PHASE, 1);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.1D, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 1.0D, 36.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.7D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 16.0F));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public boolean isAwake() { return this.entityData.get(DATA_AWAKE); }
    public int getAwakeningTicks() { return this.entityData.get(DATA_AWAKENING_TICKS); }
    public int getPhase() { return this.entityData.get(DATA_PHASE); }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (this.getAwakeningTicks() > 0 && !this.awakenAnimationState.isStarted()) {
                this.awakenAnimationState.start(this.tickCount);
            }
            if (this.isAwake()) {
                for (int i = 0; i < this.getPhase(); i++) {
                    this.level().addParticle(ParticleTypes.LAVA,
                            this.getRandomX(0.8D), this.getRandomY(), this.getRandomZ(0.8D),
                            0.0D, 0.05D, 0.0D);
                }
            }
            return;
        }

        if (!this.isAwake()) {
            int awakenTicks = this.getAwakeningTicks();
            if (awakenTicks == 0) {
                Player nearest = this.level().getNearestPlayer(this, 14.0D);
                if (nearest != null && !nearest.isCreative() && !nearest.isSpectator()) {
                    this.entityData.set(DATA_AWAKENING_TICKS, 1);
                    this.playSound(SoundEvents.WARDEN_ROAR, 2.5F, 0.5F);
                }
            } else {
                awakenTicks++;
                this.entityData.set(DATA_AWAKENING_TICKS, awakenTicks);
                if (awakenTicks >= 80) {
                    this.entityData.set(DATA_AWAKE, true);
                    this.entityData.set(DATA_AWAKENING_TICKS, 0);
                    this.performGroundSlam();
                }
            }
            return;
        }

        float healthRatio = this.getHealth() / this.getMaxHealth();
        int newPhase = healthRatio > 0.66F ? 1 : (healthRatio > 0.33F ? 2 : 3);
        if (newPhase != this.getPhase()) {
            this.entityData.set(DATA_PHASE, newPhase);
            this.playSound(SoundEvents.WITHER_BREAK_BLOCK, 2.0F, 0.7F);
            this.performGroundSlam();
            if (newPhase == 3 && this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
                this.bossEvent.setColor(BossEvent.BossBarColor.YELLOW);
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.31D);
            }
        }

        this.bossEvent.setProgress(healthRatio);

        if (this.getPhase() == 3 && this.tickCount % 10 == 0) {
            BlockPos below = this.blockPosition().below();
            if (this.level().getBlockState(below).isSolidRender(this.level(), below)) {
                this.level().setBlockAndUpdate(below, ModBlocks.SCORCHED_MAGMA_EARTH.get().defaultBlockState());
            }
            if (this.level().getBlockState(this.blockPosition()).isAir()) {
                this.level().setBlockAndUpdate(this.blockPosition(), Blocks.FIRE.defaultBlockState());
            }
        }

        LivingEntity target = this.getTarget();
        if (target != null && target.isAlive()) {
            if (--this.specialAttackCooldown <= 0) {
                if (this.getPhase() == 1) {
                    this.performGroundSlam();
                    this.specialAttackCooldown = 90;
                } else if (this.getPhase() == 2) {
                    this.shootMagmaBarrage(target);
                    this.specialAttackCooldown = 70;
                } else {
                    this.performGroundSlam();
                    this.shootMagmaBarrage(target);
                    this.specialAttackCooldown = 55;
                }
            }
            if (this.getPhase() >= 2 && --this.minionSummonCooldown <= 0) {
                this.summonAshMinions();
                this.minionSummonCooldown = 260;
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        if (this.isAwake()) {
            super.customServerAiStep();
        }
    }

    private void performGroundSlam() {
        this.playSound(SoundEvents.GENERIC_EXPLODE, 2.0F, 0.7F);
        AABB area = this.getBoundingBox().inflate(6.0D, 2.0D, 6.0D);
        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, area,
                e -> e != this && !(e instanceof AshZombieEntity) && !(e instanceof AshSkeletonEntity));
        for (LivingEntity entity : targets) {
            entity.hurt(this.damageSources().mobAttack(this), 10.0F + this.getPhase() * 3.0F);
            Vec3 knock = entity.position().subtract(this.position()).normalize().scale(1.4D);
            entity.push(knock.x, 0.65D, knock.z);
            entity.setSecondsOnFire(4);
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.FLAME,
                    this.getX(), this.getY() + 0.2D, this.getZ(),
                    65, 3.5D, 0.3D, 3.5D, 0.08D);
        }
    }

    private void shootMagmaBarrage(LivingEntity target) {
        this.playSound(SoundEvents.BLAZE_SHOOT, 2.0F, 0.6F);
        for (int i = -1; i <= 1; i++) {
            Vec3 dir = target.getEyePosition().subtract(this.getEyePosition()).normalize();
            SmallFireball fireball = new SmallFireball(this.level(), this,
                    dir.x + i * 0.15D, dir.y, dir.z + i * 0.15D);
            fireball.setPos(this.getX(), this.getEyeY() - 0.3D, this.getZ());
            this.level().addFreshEntity(fireball);
        }
    }

    private void summonAshMinions() {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;
        this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, 2.0F, 0.6F);
        for (int i = 0; i < 2; i++) {
            BlockPos spawnPos = this.blockPosition().offset(
                    Mth.nextInt(this.random, -4, 4), 0, Mth.nextInt(this.random, -4, 4));
            if (i % 2 == 0) {
                AshZombieEntity zombie = ModEntities.ASH_ZOMBIE.get().create(serverLevel);
                if (zombie != null) {
                    zombie.moveTo(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, 0.0F, 0.0F);
                    zombie.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(spawnPos),
                            MobSpawnType.MOB_SUMMONED, null, null);
                    serverLevel.addFreshEntity(zombie);
                }
            } else {
                AshSkeletonEntity skeleton = ModEntities.ASH_SKELETON.get().create(serverLevel);
                if (skeleton != null) {
                    skeleton.moveTo(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, 0.0F, 0.0F);
                    skeleton.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(spawnPos),
                            MobSpawnType.MOB_SUMMONED, null, null);
                    serverLevel.addFreshEntity(skeleton);
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean bypass = source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                || (source.getEntity() instanceof Player p && p.isCreative());
        if (!this.isAwake() && !bypass) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Awake", this.isAwake());
        tag.putInt("Phase", this.getPhase());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_AWAKE, tag.getBoolean("Awake"));
        this.entityData.set(DATA_PHASE, Math.max(1, tag.getInt("Phase")));
    }
}
