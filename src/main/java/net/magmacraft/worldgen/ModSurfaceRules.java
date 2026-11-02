package net.magmacraft.worldgen;

import net.magmacraft.registry.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class ModSurfaceRules {
    private static final SurfaceRules.RuleSource ASH_DIRT = makeStateRule(ModBlocks.ASH_DIRT.get());
    private static final SurfaceRules.RuleSource SCORCHED_MAGMA = makeStateRule(ModBlocks.SCORCHED_MAGMA_EARTH.get());
    private static final SurfaceRules.RuleSource ASH_STONE = makeStateRule(ModBlocks.ASH_STONE.get());
    private static final SurfaceRules.RuleSource BASALT = makeStateRule(Blocks.SMOOTH_BASALT);

    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.RuleSource steepCliffs = SurfaceRules.ifTrue(SurfaceRules.steep(), ASH_STONE);

        SurfaceRules.RuleSource ashPlainsSurface = SurfaceRules.sequence(
                steepCliffs,
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.NETHER_STATE_SELECTOR, 0.35D), SCORCHED_MAGMA),
                                ASH_DIRT
                        )),
                ASH_STONE
        );

        SurfaceRules.RuleSource cragsSurface = SurfaceRules.sequence(
                steepCliffs,
                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SURFACE, 0.2D), BASALT),
                ASH_STONE
        );

        SurfaceRules.RuleSource boilingWatersSurface = SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PATCH, 0.4D), SCORCHED_MAGMA),
                ASH_STONE
        );

        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.isBiome(MagmacraftRegion.ASH_PLAINS), ashPlainsSurface),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(MagmacraftRegion.VOLCANIC_CRAGS), cragsSurface),
                SurfaceRules.ifTrue(SurfaceRules.isBiome(MagmacraftRegion.BOILING_WATERS), boilingWatersSurface)
        );
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
