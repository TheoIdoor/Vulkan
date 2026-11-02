package com.theoidoor.vulkan.worldgen;

import com.theoidoor.vulkan.VulkanMod;
import com.theoidoor.vulkan.worldgen.feature.VolcanoFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, VulkanMod.MOD_ID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> VOLCANO =
            FEATURES.register("volcano", () -> new VolcanoFeature(NoneFeatureConfiguration.CODEC));

    private ModFeatures() {
    }
}
