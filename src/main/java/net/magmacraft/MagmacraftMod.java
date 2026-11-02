package net.magmacraft;

import com.mojang.logging.LogUtils;
import net.magmacraft.content.entity.AshSkeletonEntity;
import net.magmacraft.content.entity.AshZombieEntity;
import net.magmacraft.content.entity.MagmaGolemEntity;
import net.magmacraft.registry.*;
import net.magmacraft.worldgen.MagmacraftRegion;
import net.magmacraft.worldgen.ModSurfaceRules;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

@Mod(MagmacraftMod.MOD_ID)
public class MagmacraftMod {
    public static final String MOD_ID = "magmacraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MagmacraftMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModEntities.register(modEventBus);
        ModWorldGen.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerAttributes);
        modEventBus.addListener(this::registerSpawnPlacements);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Regions.register(new MagmacraftRegion(new ResourceLocation(MOD_ID, "overworld_volcanic"), 4));
            SurfaceRuleManager.addSurfaceRules(
                SurfaceRuleManager.RuleCategory.OVERWORLD,
                MOD_ID,
                ModSurfaceRules.makeRules()
            );
        });
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.ASH_ZOMBIE.get(), AshZombieEntity.createAttributes().build());
        event.put(ModEntities.ASH_SKELETON.get(), AshSkeletonEntity.createAttributes().build());
        event.put(ModEntities.MAGMA_GOLEM.get(), MagmaGolemEntity.createAttributes().build());
    }

    private void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
            ModEntities.ASH_ZOMBIE.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Monster::checkMonsterSpawnRules,
            SpawnPlacementRegisterEvent.Operation.REPLACE
        );
        event.register(
            ModEntities.ASH_SKELETON.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Monster::checkMonsterSpawnRules,
            SpawnPlacementRegisterEvent.Operation.REPLACE
        );
    }
}
