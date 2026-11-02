package net.magmacraft.client;

import net.magmacraft.MagmacraftMod;
import net.magmacraft.client.model.MagmaGolemModel;
import net.magmacraft.client.renderer.MagmaGolemRenderer;
import net.magmacraft.registry.ModEntities;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MagmacraftMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    private static final ResourceLocation ASH_ZOMBIE_TEX =
            new ResourceLocation(MagmacraftMod.MOD_ID, "textures/entity/ash_zombie.png");
    private static final ResourceLocation ASH_SKELETON_TEX =
            new ResourceLocation(MagmacraftMod.MOD_ID, "textures/entity/ash_skeleton.png");

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.ASH_ZOMBIE.get(), ctx -> new ZombieRenderer(ctx) {
            @Override
            public ResourceLocation getTextureLocation(Zombie entity) {
                return ASH_ZOMBIE_TEX;
            }
        });

        event.registerEntityRenderer(ModEntities.ASH_SKELETON.get(), ctx -> new SkeletonRenderer(ctx) {
            @Override
            public ResourceLocation getTextureLocation(AbstractSkeleton entity) {
                return ASH_SKELETON_TEX;
            }
        });

        event.registerEntityRenderer(ModEntities.MAGMA_GOLEM.get(), MagmaGolemRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MagmaGolemModel.LAYER_LOCATION, MagmaGolemModel::createBodyLayer);
    }
}
