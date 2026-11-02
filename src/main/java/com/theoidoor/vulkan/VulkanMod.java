package com.theoidoor.vulkan;

import com.theoidoor.vulkan.entity.ModEntities;
import com.theoidoor.vulkan.worldgen.ModBiomes;
import com.theoidoor.vulkan.worldgen.ModFeatures;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

@Mod(VulkanMod.MOD_ID)
public class VulkanMod {
    public static final String MOD_ID = "vulkan";

    public VulkanMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModFeatures.FEATURES.register(modBus);
        ModEntities.ENTITY_TYPES.register(modBus);
        modBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Regions.register(new VolcanicRegion());
            SurfaceRuleManager.addSurfaceRules(
                    SurfaceRuleManager.RuleCategory.OVERWORLD,
                    MOD_ID,
                    ModBiomes.surfaceRules()
            );
        });
    }
}
