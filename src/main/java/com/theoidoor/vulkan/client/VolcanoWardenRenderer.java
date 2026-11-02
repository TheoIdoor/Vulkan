package com.theoidoor.vulkan.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.theoidoor.vulkan.VulkanMod;
import com.theoidoor.vulkan.entity.ModEntities;
import com.theoidoor.vulkan.entity.VolcanoWarden;
import net.minecraft.client.model.BlazeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VulkanMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class VolcanoWardenRenderer extends MobRenderer<VolcanoWarden, BlazeModel<VolcanoWarden>> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("minecraft", "textures/entity/blaze.png");

    public VolcanoWardenRenderer(EntityRendererProvider.Context context) {
        super(context, new BlazeModel<>(context.bakeLayer(ModelLayers.BLAZE)), 0.7F);
    }

    @Override
    protected void scale(VolcanoWarden entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(1.7F, 1.7F, 1.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(VolcanoWarden entity) {
        return TEXTURE;
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.VOLCANO_WARDEN.get(), VolcanoWardenRenderer::new);
    }
}
