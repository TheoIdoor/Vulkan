package net.magmacraft.content.item;

import net.magmacraft.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class MagmaWalkerBootsItem extends ArmorItem {
    public MagmaWalkerBootsItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!level.isClientSide() && entity instanceof LivingEntity living
                && living.getItemBySlot(EquipmentSlot.FEET) == stack) {
            crystallizeLavaNearby(living, level, living.blockPosition());
        }
    }

    public static void crystallizeLavaNearby(LivingEntity living, Level level, BlockPos pos) {
        if (!living.onGround()) return;
        BlockState crystalState = ModBlocks.BRITTLE_LAVA_CRYSTAL.get().defaultBlockState();
        int radius = 3;
        BlockPos.MutableBlockPos mutableAbove = new BlockPos.MutableBlockPos();

        for (BlockPos targetPos : BlockPos.betweenClosed(pos.offset(-radius, -1, -radius), pos.offset(radius, -1, radius))) {
            if (targetPos.closerToCenterThan(living.position(), radius)) {
                mutableAbove.set(targetPos.getX(), targetPos.getY() + 1, targetPos.getZ());
                if (level.getBlockState(mutableAbove).isAir()) {
                    BlockState targetState = level.getBlockState(targetPos);
                    if (targetState.is(Blocks.LAVA) && targetState.getValue(LiquidBlock.LEVEL) == 0
                            && crystalState.canSurvive(level, targetPos)
                            && level.isUnobstructed(crystalState, targetPos, CollisionContext.empty())) {
                        level.setBlockAndUpdate(targetPos, crystalState);
                        level.scheduleTick(targetPos, ModBlocks.BRITTLE_LAVA_CRYSTAL.get(),
                                Mth.nextInt(living.getRandom(), 40, 80));
                    }
                }
            }
        }
    }
}
