package net.magmacraft.registry;

import net.magmacraft.MagmacraftMod;
import net.magmacraft.content.item.MagmaWalkerBootsItem;
import net.magmacraft.content.item.ModArmorMaterials;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MagmacraftMod.MOD_ID);

    public static final RegistryObject<Item> ASH_SHARD = ITEMS.register("ash_shard",
            () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> MAGMA_WALKER_BOOTS = ITEMS.register("magma_walker_boots",
            () -> new MagmaWalkerBootsItem(
                    ModArmorMaterials.MAGMA_CORE,
                    ArmorItem.Type.BOOTS,
                    new Item.Properties().fireResistant().rarity(Rarity.EPIC).stacksTo(1)
            ));

    public static final RegistryObject<Item> ASH_ZOMBIE_SPAWN_EGG = ITEMS.register("ash_zombie_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ASH_ZOMBIE, 0x3A383B, 0xFF4500, new Item.Properties()));

    public static final RegistryObject<Item> ASH_SKELETON_SPAWN_EGG = ITEMS.register("ash_skeleton_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.ASH_SKELETON, 0x2B292C, 0xFF7700, new Item.Properties()));

    public static final RegistryObject<Item> MAGMA_GOLEM_SPAWN_EGG = ITEMS.register("magma_golem_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.MAGMA_GOLEM, 0x1B181C, 0xFF2200, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
