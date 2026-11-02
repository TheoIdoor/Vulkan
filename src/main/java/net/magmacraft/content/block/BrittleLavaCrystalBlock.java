package net.magmacraft.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BrittleLavaCrystalBlock extends Block {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public BrittleLavaCrystalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.tick(state, level, pos, random);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if ((random.nextInt(3) == 0 || this.fewerNeigboursThan(level, pos, 4))
                && this.slightlyCrack(state, level, pos)) {
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
            for (Direction direction : Direction.values()) {
                mutablePos.setWithOffset(pos, direction);
                BlockState neighborState = level.getBlockState(mutablePos);
                if (neighborState.is(this) && !this.slightlyCrack(neighborState, level, mutablePos)) {
                    level.scheduleTick(mutablePos, this, Mth.nextInt(random, 20, 40));
                }
            }
        } else {
            level.scheduleTick(pos, this, Mth.nextInt(random, 20, 40));
        }
    }

    private boolean slightlyCrack(BlockState state, Level level, BlockPos pos) {
        int age = state.getValue(AGE);
        if (age < 3) {
            level.setBlock(pos, state.setValue(AGE, age + 1), 2);
            level.playSound(null, pos, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 0.5F, 0.7F + age * 0.15F);
            level.destroyBlockProgress(pos.hashCode(), pos, (age + 1) * 3);
            return false;
        } else {
            this.meltToLava(level, pos);
            return true;
        }
    }

    private void meltToLava(Level level, BlockPos pos) {
        level.destroyBlockProgress(pos.hashCode(), pos, -1);
        level.setBlockAndUpdate(pos, Blocks.LAVA.defaultBlockState());
        level.neighborChanged(pos, Blocks.LAVA, pos);
    }

    private boolean fewerNeigboursThan(BlockGetter level, BlockPos pos, int maxNeighbors) {
        int count = 0;
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.values()) {
            mutablePos.setWithOffset(pos, direction);
            if (level.getBlockState(mutablePos).is(this)) {
                ++count;
                if (count >= maxNeighbors) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
