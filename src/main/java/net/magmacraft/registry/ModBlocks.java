package net.magmacraft.registry;

import net.magmacraft.MagmacraftMod;
import net.magmacraft.content.block.BrittleLavaCrystalBlock;
import net.magmacraft.content.block.GeyserBlock;
import net.magmacraft.content.block.ScorchedMagmaEarthBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MagmacraftMod.MOD_ID);

    public static final BlockSetType ASH_BLOCK_SET = BlockSetType.register(new BlockSetType(MagmacraftMod.MOD_ID + ":ash"));
    public static final WoodType ASH_WOOD_TYPE = WoodType.register(new WoodType(MagmacraftMod.MOD_ID + ":ash", ASH_BLOCK_SET));

    private static BlockBehaviour.Properties ashWoodProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_LIGHT_GRAY)
                .strength(2.5F, 3.0F)
                .sound(SoundType.WOOD)
                .lightLevel(state -> 5);
    }

    public static final RegistryObject<Block> ASH_DIRT = registerBlock("ash_dirt",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(0.6F).sound(SoundType.ROOTED_DIRT)));

    public static final RegistryObject<Block> SCORCHED_MAGMA_EARTH = registerBlock("scorched_magma_earth",
            () -> new ScorchedMagmaEarthBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(1.2F, 4.0F).lightLevel(s -> 6).sound(SoundType.BASALT)));

    public static final RegistryObject<Block> GEYSER_BLOCK = registerBlock("geyser_block",
            () -> new GeyserBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.0F, 6.0F).lightLevel(s -> 7).randomTicks().sound(SoundType.DEEPSLATE)));

    public static final RegistryObject<Block> BRITTLE_LAVA_CRYSTAL = BLOCKS.register("brittle_lava_crystal",
            () -> new BrittleLavaCrystalBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.FIRE).strength(0.4F).lightLevel(s -> 11).noOcclusion().randomTicks().sound(SoundType.AMETHYST).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> ASH_LOG = registerBlock("ash_log", () -> new RotatedPillarBlock(ashWoodProps()));
    public static final RegistryObject<Block> STRIPPED_ASH_LOG = registerBlock("stripped_ash_log", () -> new RotatedPillarBlock(ashWoodProps()));
    public static final RegistryObject<Block> ASH_WOOD = registerBlock("ash_wood", () -> new RotatedPillarBlock(ashWoodProps()));
    public static final RegistryObject<Block> STRIPPED_ASH_WOOD = registerBlock("stripped_ash_wood", () -> new RotatedPillarBlock(ashWoodProps()));
    public static final RegistryObject<Block> ASH_PLANKS = registerBlock("ash_planks", () -> new Block(ashWoodProps()));
    public static final RegistryObject<Block> ASH_STAIRS = registerBlock("ash_stairs",
            () -> new StairBlock(() -> ASH_PLANKS.get().defaultBlockState(), ashWoodProps()));
    public static final RegistryObject<Block> ASH_SLAB = registerBlock("ash_slab", () -> new SlabBlock(ashWoodProps()));
    public static final RegistryObject<Block> ASH_FENCE = registerBlock("ash_fence", () -> new FenceBlock(ashWoodProps()));
    public static final RegistryObject<Block> ASH_FENCE_GATE = registerBlock("ash_fence_gate", () -> new FenceGateBlock(ashWoodProps(), ASH_WOOD_TYPE));
    public static final RegistryObject<Block> ASH_DOOR = registerBlock("ash_door", () -> new DoorBlock(ashWoodProps().noOcclusion(), ASH_BLOCK_SET));
    public static final RegistryObject<Block> ASH_TRAPDOOR = registerBlock("ash_trapdoor", () -> new TrapDoorBlock(ashWoodProps().noOcclusion(), ASH_BLOCK_SET));
    public static final RegistryObject<Block> ASH_PRESSURE_PLATE = registerBlock("ash_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, ashWoodProps().noCollission(), ASH_BLOCK_SET));
    public static final RegistryObject<Block> ASH_BUTTON = registerBlock("ash_button",
            () -> new ButtonBlock(ashWoodProps().noCollission(), ASH_BLOCK_SET, 30, true));

    public static final RegistryObject<Block> ASH_STONE = registerBlock("ash_stone",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.DEEPSLATE)));
    public static final RegistryObject<Block> ASH_COBBLESTONE = registerBlock("ash_cobblestone",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.2F, 6.0F).sound(SoundType.DEEPSLATE)));
    public static final RegistryObject<Block> ASH_STONE_BRICKS = registerBlock("ash_stone_bricks",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5F, 8.0F).sound(SoundType.DEEPSLATE_BRICKS)));
    public static final RegistryObject<Block> CRACKED_ASH_STONE_BRICKS = registerBlock("cracked_ash_stone_bricks",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(2.5F, 8.0F).lightLevel(s -> 3).sound(SoundType.DEEPSLATE_BRICKS)));
    public static final RegistryObject<Block> GILDED_FORGE_STONE = registerBlock("gilded_forge_stone",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).requiresCorrectToolForDrops().strength(4.0F, 12.0F).lightLevel(s -> 8).sound(SoundType.GILDED_BLACKSTONE)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> new BlockItem(toReturn.get(), new Item.Properties().fireResistant()));
        return toReturn;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
