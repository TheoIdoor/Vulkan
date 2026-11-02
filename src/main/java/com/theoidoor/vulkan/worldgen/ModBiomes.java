package com.theoidoor.vulkan.worldgen;

import com.theoidoor.vulkan.VulkanMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;

public final class ModBiomes {
    public static final ResourceKey<Biome> VOLCANIC_WASTES_KEY = ResourceKey.create(
            Registries.BIOME,
            new ResourceLocation(VulkanMod.MOD_ID, "volcanic_wastes")
    );

    private ModBiomes() {
    }

    public static SurfaceRules.RuleSource surfaceRules() {
        return SurfaceRules.ifTrue(
                SurfaceRules.isBiome(VOLCANIC_WASTES_KEY),
                SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.state(Blocks.BLACKSTONE.defaultBlockState())),
                        SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.state(Blocks.BASALT.defaultBlockState()))
                )
        );
    }
}
