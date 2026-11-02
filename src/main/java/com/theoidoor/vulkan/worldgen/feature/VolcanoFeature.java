package com.theoidoor.vulkan.worldgen.feature;

import com.theoidoor.vulkan.entity.ModEntities;
import com.theoidoor.vulkan.entity.VolcanoWarden;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class VolcanoFeature extends Feature<NoneFeatureConfiguration> {
    private static final int RADIUS = 22;

    public VolcanoFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        int centerX = origin.getX();
        int centerZ = origin.getZ();
        int baseY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, centerX, centerZ);

        buildCone(level, random, centerX, centerZ, baseY);
        buildEntranceAndRooms(level, centerX, centerZ, baseY);
        spawnWarden(level, random, centerX + 7, baseY - 15, centerZ);
        return true;
    }

    private static void buildCone(WorldGenLevel level, RandomSource random, int centerX, int centerZ, int baseY) {
        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                double distance = Math.sqrt(dx * dx + dz * dz);
                if (distance > RADIUS) {
                    continue;
                }

                int x = centerX + dx;
                int z = centerZ + dz;
                int groundY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
                int topY;
                if (distance < 5.0D) {
                    topY = baseY + 13;
                } else if (distance < 7.0D) {
                    topY = baseY + 25 + random.nextInt(3);
                } else {
                    topY = baseY + Math.max(2, (int) ((RADIUS - distance) * 1.8D));
                }

                for (int y = groundY; y <= topY; y++) {
                    BlockState stone = random.nextInt(5) == 0
                            ? Blocks.BLACKSTONE.defaultBlockState()
                            : Blocks.BASALT.defaultBlockState();
                    set(level, new BlockPos(x, y, z), stone);
                }

                if (distance < 3.5D) {
                    set(level, new BlockPos(x, baseY + 14, z), Blocks.LAVA.defaultBlockState());
                } else if (distance < 5.0D) {
                    set(level, new BlockPos(x, baseY + 14, z), Blocks.MAGMA_BLOCK.defaultBlockState());
                } else if (distance >= 7.0D && distance < 8.5D) {
                    set(level, new BlockPos(x, topY, z), Blocks.POLISHED_BLACKSTONE.defaultBlockState());
                }
            }
        }
    }

    private static void buildEntranceAndRooms(WorldGenLevel level, int x, int z, int baseY) {
        for (int step = 0; step <= 20; step++) {
            int tunnelX = x - 22 + step;
            int centerY = baseY + 1 - step * 9 / 20;
            for (int dz = -1; dz <= 1; dz++) {
                set(level, new BlockPos(tunnelX, centerY - 2, z + dz), Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState());
                for (int dy = -1; dy <= 2; dy++) {
                    set(level, new BlockPos(tunnelX, centerY + dy, z + dz), Blocks.AIR.defaultBlockState());
                }
            }
        }

        buildRoom(level, x - 7, baseY - 10, z, 5, 6, 5);
        buildRoom(level, x - 7, baseY - 10, z + 10, 4, 6, 4);
        buildRoom(level, x - 7, baseY - 10, z - 10, 4, 6, 4);
        buildRoom(level, x + 7, baseY - 16, z, 7, 9, 7);

        for (int corridorX = x - 4; corridorX <= x + 1; corridorX++) {
            carveCorridor(level, corridorX, baseY - 8, z);
        }
        for (int corridorZ = z - 6; corridorZ <= z + 6; corridorZ++) {
            if (corridorZ < z - 4 || corridorZ > z + 4) {
                carveCorridor(level, x - 7, baseY - 8, corridorZ);
            }
        }

        placeForgeFurnishings(level, x - 7, baseY - 10, z);
        placeStorageFurnishings(level, x - 7, baseY - 10, z + 10);
        placeSleepingFurnishings(level, x - 7, baseY - 10, z - 10);
        placeBossArena(level, x + 7, baseY - 16, z);
        buildEntranceArch(level, x - 20, baseY, z);
    }

    private static void buildRoom(WorldGenLevel level, int centerX, int floorY, int centerZ, int halfX, int height, int halfZ) {
        for (int dx = -halfX; dx <= halfX; dx++) {
            for (int dz = -halfZ; dz <= halfZ; dz++) {
                for (int dy = 0; dy <= height; dy++) {
                    boolean wall = Math.abs(dx) == halfX || Math.abs(dz) == halfZ || dy == 0 || dy == height;
                    BlockState state = wall
                            ? (dy == 0 ? Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState() : Blocks.BLACKSTONE.defaultBlockState())
                            : Blocks.AIR.defaultBlockState();
                    set(level, new BlockPos(centerX + dx, floorY + dy, centerZ + dz), state);
                }
            }
        }
    }

    private static void carveCorridor(WorldGenLevel level, int x, int centerY, int z) {
        for (int dx = -1; dx <= 1; dx++) {
            set(level, new BlockPos(x + dx, centerY - 2, z), Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState());
            for (int dy = -1; dy <= 2; dy++) {
                set(level, new BlockPos(x + dx, centerY + dy, z), Blocks.AIR.defaultBlockState());
            }
        }
    }

    private static void buildEntranceArch(WorldGenLevel level, int x, int y, int z) {
        for (int dz : new int[]{-2, 2}) {
            for (int dy = 0; dy <= 2; dy++) {
                set(level, new BlockPos(x, y + dy, z + dz), Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState());
            }
        }
        for (int dz = -2; dz <= 2; dz++) {
            set(level, new BlockPos(x, y + 3, z + dz), Blocks.CHISELED_POLISHED_BLACKSTONE.defaultBlockState());
        }
    }

    private static void placeForgeFurnishings(WorldGenLevel level, int x, int floorY, int z) {
        set(level, new BlockPos(x - 2, floorY + 1, z), Blocks.BLAST_FURNACE.defaultBlockState().setValue(net.minecraft.world.level.block.AbstractFurnaceBlock.FACING, Direction.EAST));
        set(level, new BlockPos(x + 2, floorY + 1, z), Blocks.SMITHING_TABLE.defaultBlockState());
        set(level, new BlockPos(x, floorY + 1, z - 2), Blocks.ANVIL.defaultBlockState());
        set(level, new BlockPos(x, floorY + 1, z + 2), Blocks.LAVA_CAULDRON.defaultBlockState());
        set(level, new BlockPos(x, floorY + 1, z), Blocks.MAGMA_BLOCK.defaultBlockState());
        set(level, new BlockPos(x - 3, floorY + 1, z - 3), Blocks.LANTERN.defaultBlockState());
    }

    private static void placeStorageFurnishings(WorldGenLevel level, int x, int floorY, int z) {
        set(level, new BlockPos(x - 2, floorY + 1, z), Blocks.BARREL.defaultBlockState());
        set(level, new BlockPos(x + 2, floorY + 1, z), Blocks.CHEST.defaultBlockState());
        set(level, new BlockPos(x, floorY + 1, z + 2), Blocks.BARREL.defaultBlockState());
        set(level, new BlockPos(x, floorY + 1, z), Blocks.LANTERN.defaultBlockState());
    }

    private static void placeSleepingFurnishings(WorldGenLevel level, int x, int floorY, int z) {
        placeBed(level, new BlockPos(x - 2, floorY + 1, z));
        placeBed(level, new BlockPos(x + 1, floorY + 1, z));
        set(level, new BlockPos(x, floorY + 1, z), Blocks.SOUL_LANTERN.defaultBlockState());
        set(level, new BlockPos(x, floorY + 1, z + 2), Blocks.LANTERN.defaultBlockState());
    }

    private static void placeBed(WorldGenLevel level, BlockPos footPos) {
        BlockState foot = Blocks.RED_BED.defaultBlockState()
                .setValue(BedBlock.PART, BedPart.FOOT)
                .setValue(BedBlock.FACING, Direction.EAST);
        BlockState head = foot.setValue(BedBlock.PART, BedPart.HEAD);
        set(level, footPos, foot);
        set(level, footPos.relative(Direction.EAST), head);
    }

    private static void placeBossArena(WorldGenLevel level, int x, int floorY, int z) {
        for (int angle = 0; angle < 8; angle++) {
            double radians = Math.PI * angle / 4.0D;
            int pillarX = x + (int) Math.round(Math.cos(radians) * 5);
            int pillarZ = z + (int) Math.round(Math.sin(radians) * 5);
            for (int dy = 1; dy <= 5; dy++) {
                set(level, new BlockPos(pillarX, floorY + dy, pillarZ), Blocks.BASALT.defaultBlockState());
            }
            set(level, new BlockPos(pillarX, floorY + 6, pillarZ), Blocks.SOUL_LANTERN.defaultBlockState());
        }
        set(level, new BlockPos(x, floorY + 1, z), Blocks.CRYING_OBSIDIAN.defaultBlockState());
        set(level, new BlockPos(x + 1, floorY + 1, z), Blocks.MAGMA_BLOCK.defaultBlockState());
        set(level, new BlockPos(x - 1, floorY + 1, z), Blocks.MAGMA_BLOCK.defaultBlockState());
    }

    private static void spawnWarden(WorldGenLevel level, RandomSource random, int x, int floorY, int z) {
        EntityType<VolcanoWarden> entityType = ModEntities.VOLCANO_WARDEN.get();
        VolcanoWarden warden = entityType.create(level.getLevel());
        if (warden != null) {
            warden.moveTo(x + 0.5D, floorY + 1.0D, z + 0.5D, random.nextFloat() * 360.0F, 0.0F);
            warden.setPersistenceRequired();
            level.addFreshEntityWithPassengers(warden);
        }
    }

    private static void set(WorldGenLevel level, BlockPos pos, BlockState state) {
        if (pos.getY() >= level.getMinBuildHeight() && pos.getY() < level.getMaxBuildHeight() && level.ensureCanWrite(pos)) {
            level.setBlock(pos, state, 2);
        }
    }
}
