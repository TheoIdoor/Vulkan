package net.magmacraft.worldgen;

import com.mojang.datafixers.util.Pair;
import net.magmacraft.MagmacraftMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class MagmacraftRegion extends Region {
    public static final ResourceKey<Biome> BOILING_WATERS = ResourceKey.create(
            Registries.BIOME, new ResourceLocation(MagmacraftMod.MOD_ID, "boiling_waters"));
    public static final ResourceKey<Biome> ASH_PLAINS = ResourceKey.create(
            Registries.BIOME, new ResourceLocation(MagmacraftMod.MOD_ID, "ash_plains"));
    public static final ResourceKey<Biome> VOLCANIC_CRAGS = ResourceKey.create(
            Registries.BIOME, new ResourceLocation(MagmacraftMod.MOD_ID, "volcanic_crags"));

    public MagmacraftRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        this.addModifiedVanillaOverworldBiomes(mapper, builder -> {
            builder.replaceBiome(Biomes.WARM_OCEAN, BOILING_WATERS);
            builder.replaceBiome(Biomes.DESERT, ASH_PLAINS);
            builder.replaceBiome(Biomes.BADLANDS, VOLCANIC_CRAGS);
            builder.replaceBiome(Biomes.ERODED_BADLANDS, VOLCANIC_CRAGS);
        });
    }
}
