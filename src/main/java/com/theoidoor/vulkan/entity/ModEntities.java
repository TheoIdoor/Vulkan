package com.theoidoor.vulkan.entity;

import com.theoidoor.vulkan.VulkanMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = VulkanMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, VulkanMod.MOD_ID);

    public static final RegistryObject<EntityType<VolcanoWarden>> VOLCANO_WARDEN =
            ENTITY_TYPES.register("volcano_warden", () -> EntityType.Builder
                    .of(VolcanoWarden::new, MobCategory.MONSTER)
                    .sized(1.2F, 2.4F)
                    .clientTrackingRange(10)
                    .build("volcano_warden"));

    private ModEntities() {
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(VOLCANO_WARDEN.get(), VolcanoWarden.createAttributes().build());
    }
}
