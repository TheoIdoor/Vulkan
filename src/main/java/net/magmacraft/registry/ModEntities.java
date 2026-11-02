package net.magmacraft.registry;

import net.magmacraft.MagmacraftMod;
import net.magmacraft.content.entity.AshSkeletonEntity;
import net.magmacraft.content.entity.AshZombieEntity;
import net.magmacraft.content.entity.MagmaGolemEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MagmacraftMod.MOD_ID);

    public static final RegistryObject<EntityType<AshZombieEntity>> ASH_ZOMBIE =
            ENTITIES.register("ash_zombie", () -> EntityType.Builder.of(AshZombieEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .build(new ResourceLocation(MagmacraftMod.MOD_ID, "ash_zombie").toString()));

    public static final RegistryObject<EntityType<AshSkeletonEntity>> ASH_SKELETON =
            ENTITIES.register("ash_skeleton", () -> EntityType.Builder.of(AshSkeletonEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .build(new ResourceLocation(MagmacraftMod.MOD_ID, "ash_skeleton").toString()));

    public static final RegistryObject<EntityType<MagmaGolemEntity>> MAGMA_GOLEM =
            ENTITIES.register("magma_golem", () -> EntityType.Builder.of(MagmaGolemEntity::new, MobCategory.MONSTER)
                    .sized(2.2F, 3.6F)
                    .fireImmune()
                    .build(new ResourceLocation(MagmacraftMod.MOD_ID, "magma_golem").toString()));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
