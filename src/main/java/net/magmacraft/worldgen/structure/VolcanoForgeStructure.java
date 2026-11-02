package net.magmacraft.worldgen.structure;

import com.mojang.serialization.Codec;
import net.magmacraft.content.entity.MagmaGolemEntity;
import net.magmacraft.registry.ModBlocks;
import net.magmacraft.registry.ModEntities;
import net.magmacraft.registry.ModWorldGen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class VolcanoForgeStructure extends Structure {
    public static final Codec<VolcanoForgeStructure> CODEC = simpleCodec(VolcanoForgeStructure::new);

    public VolcanoForgeStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getMiddleBlockX();
        int z = chunkPos.getMiddleBlockZ();
        int y = context.chunkGenerator().getFirstOccupiedHeight(
                x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
        BlockPos center = new BlockPos(x, y, z);
        return Optional.of(new GenerationStub(center, builder -> generatePieces(builder, center)));
    }

    private static void generatePieces(StructurePiecesBuilder builder, BlockPos center) {
        builder.addPiece(new VolcanoForgePiece(center));
    }

    @Override
    public StructureType<?> type() {
        return ModWorldGen.VOLCANO_FORGE.get();
    }

    public static class VolcanoForgePiece extends StructurePiece {
        private final BlockPos center;
        private boolean bossSpawned = false;

        public VolcanoForgePiece(BlockPos center) {
            super(ModWorldGen.VOLCANO_FORGE_PIECE.get(), 0, new BoundingBox(
                    center.getX() - 36, center.getY() - 12, center.getZ() - 36,
                    center.getX() + 36, center.getY() + 38, center.getZ() + 36));
            this.center = center;
        }

        public VolcanoForgePiece(StructurePieceSerializationContext ctx, CompoundTag tag) {
            super(ModWorldGen.VOLCANO_FORGE_PIECE.get(), tag);
            this.center = new BlockPos(tag.getInt("CX"), tag.getInt("CY"), tag.getInt("CZ"));
            this.bossSpawned = tag.getBoolean("BossSpawned");
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
            tag.putInt("CX", this.center.getX());
            tag.putInt("CY", this.center.getY());
            tag.putInt("CZ", this.center.getZ());
            tag.putBoolean("BossSpawned", this.bossSpawned);
        }

        @Override
        public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator,
                                RandomSource random, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos pivot) {
            BlockState ashBricks = ModBlocks.ASH_STONE_BRICKS.get().defaultBlockState();
            BlockState crackedBricks = ModBlocks.CRACKED_ASH_STONE_BRICKS.get().defaultBlockState();
            BlockState gildedStone = ModBlocks.GILDED_FORGE_STONE.get().defaultBlockState();
            BlockState basalt = Blocks.BASALT.defaultBlockState();
            BlockState lava = Blocks.LAVA.defaultBlockState();

            int radius = 32;
            int height = 34;

            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    double dist = Math.sqrt(dx * dx + dz * dz);
                    if (dist <= radius) {
                        int colHeight = (int) ((1.0D - (dist / radius)) * height);
                        for (int dy = -6; dy <= colHeight; dy++) {
                            BlockPos pos = center.offset(dx, dy, dz);
                            if (!chunkBox.isInside(pos)) continue;

                            if (dist < 18 && dy >= 0 && dy <= 14) {
                                if (dy == 0) {
                                    if (Math.abs(dx) == 6 || Math.abs(dz) == 6) {
                                        level.setBlock(pos, lava, 2);
                                    } else if ((dx + dz) % 3 == 0) {
                                        level.setBlock(pos, gildedStone, 2);
                                    } else {
                                        level.setBlock(pos, ashBricks, 2);
                                    }
                                } else if (Math.abs(dx) == 12 && Math.abs(dz) == 12) {
                                    level.setBlock(pos, dy % 4 == 0 ? gildedStone : ashBricks, 2);
                                } else {
                                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                                }
                            } else {
                                if ((Math.abs(dx) <= 1 && dz > 18) && dy == colHeight) {
                                    level.setBlock(pos, lava, 2);
                                } else {
                                    level.setBlock(pos, random.nextInt(4) == 0 ? crackedBricks : basalt, 2);
                                }
                            }
                        }
                    }
                }
            }

            for (int dz = -radius; dz <= -17; dz++) {
                for (int dx = -3; dx <= 3; dx++) {
                    for (int dy = 1; dy <= 6; dy++) {
                        BlockPos tunnelPos = center.offset(dx, dy, dz);
                        if (!chunkBox.isInside(tunnelPos)) continue;
                        if (Math.abs(dx) == 3 || dy == 6) {
                            level.setBlock(tunnelPos, dz % 4 == 0 ? gildedStone : ashBricks, 2);
                        } else {
                            level.setBlock(tunnelPos, Blocks.AIR.defaultBlockState(), 2);
                        }
                    }
                }
            }

            if (!this.bossSpawned && chunkBox.isInside(center.above())) {
                MagmaGolemEntity boss = ModEntities.MAGMA_GOLEM.get().create(level.getLevel());
                if (boss != null) {
                    boss.moveTo(center.getX() + 0.5D, center.getY() + 1.0D, center.getZ() + 0.5D, 0.0F, 0.0F);
                    boss.setPersistenceRequired();
                    boss.finalizeSpawn(level, level.getCurrentDifficultyAt(center),
                            MobSpawnType.STRUCTURE, null, null);
                    level.addFreshEntity(boss);
                    this.bossSpawned = true;
                }
            }
        }
    }
}
