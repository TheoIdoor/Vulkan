package net.magmacraft.registry;

import net.magmacraft.MagmacraftMod;
import net.magmacraft.worldgen.feature.LeaflessAshTreeFeature;
import net.magmacraft.worldgen.structure.VolcanoForgeStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModWorldGen {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, MagmacraftMod.MOD_ID);

    public static final DeferredRegister<StructureType<?>> STRUCTURES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, MagmacraftMod.MOD_ID);

    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES =
            DeferredRegister.create(Registries.STRUCTURE_PIECE, MagmacraftMod.MOD_ID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> LEAFLESS_ASH_TREE =
            FEATURES.register("leafless_ash_tree", () -> new LeaflessAshTreeFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<StructureType<VolcanoForgeStructure>> VOLCANO_FORGE =
            STRUCTURES.register("volcano_forge", () -> () -> VolcanoForgeStructure.CODEC);

    public static final RegistryObject<StructurePieceType> VOLCANO_FORGE_PIECE =
            STRUCTURE_PIECES.register("volcano_forge_piece", () -> VolcanoForgeStructure.VolcanoForgePiece::new);

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
        STRUCTURES.register(eventBus);
        STRUCTURE_PIECES.register(eventBus);
    }
}
