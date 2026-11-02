package net.magmacraft.worldgen.feature;

import com.mojang.serialization.Codec;
import net.magmacraft.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class LeaflessAshTreeFeature extends Feature<NoneFeatureConfiguration> {
    public LeaflessAshTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        BlockState ground = level.getBlockState(origin.below());
        if (!ground.is(ModBlocks.ASH_DIRT.get()) && !ground.is(ModBlocks.SCORCHED_MAGMA_EARTH.get())
                && !ground.is(ModBlocks.ASH_STONE.get())) {
            return false;
        }

        int trunkHeight = 5 + random.nextInt(4);
        BlockState logY = ModBlocks.ASH_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
        BlockState bark = ModBlocks.ASH_WOOD.get().defaultBlockState();

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            if (random.nextBoolean() && level.isEmptyBlock(origin.relative(dir))) {
                level.setBlock(origin.relative(dir), bark, 2);
            }
        }

        for (int y = 0; y < trunkHeight; y++) {
            level.setBlock(origin.above(y), logY, 2);
        }

        int branchCount = 3 + random.nextInt(3);
        for (int b = 0; b < branchCount; b++) {
            Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            BlockPos branchPos = origin.above(trunkHeight - 2 - random.nextInt(2));
            int length = 2 + random.nextInt(3);
            for (int l = 1; l <= length; l++) {
                branchPos = branchPos.relative(dir).above(l % 2 == 0 ? 1 : 0);
                level.setBlock(branchPos, ModBlocks.ASH_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, dir.getAxis()), 2);
            }
        }
        return true;
    }
}
