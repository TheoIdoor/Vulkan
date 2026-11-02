package net.magmacraft.registry;

import net.magmacraft.MagmacraftMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MagmacraftMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAGMACRAFT_TAB = CREATIVE_MODE_TABS.register("magmacraft_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.MAGMA_WALKER_BOOTS.get()))
                    .title(Component.translatable("creativetab.magmacraft_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.ASH_DIRT.get());
                        output.accept(ModBlocks.SCORCHED_MAGMA_EARTH.get());
                        output.accept(ModBlocks.GEYSER_BLOCK.get());
                        output.accept(ModBlocks.ASH_LOG.get());
                        output.accept(ModBlocks.STRIPPED_ASH_LOG.get());
                        output.accept(ModBlocks.ASH_WOOD.get());
                        output.accept(ModBlocks.STRIPPED_ASH_WOOD.get());
                        output.accept(ModBlocks.ASH_PLANKS.get());
                        output.accept(ModBlocks.ASH_STAIRS.get());
                        output.accept(ModBlocks.ASH_SLAB.get());
                        output.accept(ModBlocks.ASH_FENCE.get());
                        output.accept(ModBlocks.ASH_FENCE_GATE.get());
                        output.accept(ModBlocks.ASH_DOOR.get());
                        output.accept(ModBlocks.ASH_TRAPDOOR.get());
                        output.accept(ModBlocks.ASH_PRESSURE_PLATE.get());
                        output.accept(ModBlocks.ASH_BUTTON.get());
                        output.accept(ModBlocks.ASH_STONE.get());
                        output.accept(ModBlocks.ASH_COBBLESTONE.get());
                        output.accept(ModBlocks.ASH_STONE_BRICKS.get());
                        output.accept(ModBlocks.CRACKED_ASH_STONE_BRICKS.get());
                        output.accept(ModBlocks.GILDED_FORGE_STONE.get());
                        output.accept(ModItems.ASH_SHARD.get());
                        output.accept(ModItems.MAGMA_WALKER_BOOTS.get());
                        output.accept(ModItems.ASH_ZOMBIE_SPAWN_EGG.get());
                        output.accept(ModItems.ASH_SKELETON_SPAWN_EGG.get());
                        output.accept(ModItems.MAGMA_GOLEM_SPAWN_EGG.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
