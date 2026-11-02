package net.magmacraft.client.renderer;

import net.magmacraft.MagmacraftMod;
import net.magmacraft.client.model.MagmaGolemModel;
import net.magmacraft.content.entity.MagmaGolemEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MagmaGolemRenderer extends MobRenderer<MagmaGolemEntity, MagmaGolemModel> {
    private static final ResourceLocation TEXTURE_PHASE_1 =
            new ResourceLocation(MagmacraftMod.MOD_ID, "textures/entity/magma_golem_phase1.png");
    private static final ResourceLocation TEXTURE_PHASE_2 =
            new ResourceLocation(MagmacraftMod.MOD_ID, "textures/entity/magma_golem_phase2.png");
    private static final ResourceLocation TEXTURE_PHASE_3 =
            new ResourceLocation(MagmacraftMod.MOD_ID, "textures/entity/magma_golem_phase3.png");

    public MagmaGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new MagmaGolemModel(context.bakeLayer(MagmaGolemModel.LAYER_LOCATION)), 1.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(MagmaGolemEntity entity) {
        return switch (entity.getPhase()) {
            case 2 -> TEXTURE_PHASE_2;
            case 3 -> TEXTURE_PHASE_3;
            default -> TEXTURE_PHASE_1;
        };
    }
}
