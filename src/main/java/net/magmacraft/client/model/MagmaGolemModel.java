package net.magmacraft.client.model;

import net.magmacraft.MagmacraftMod;
import net.magmacraft.content.entity.MagmaGolemEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MagmaGolemModel extends HierarchicalModel<MagmaGolemEntity> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation(MagmacraftMod.MOD_ID, "magma_golem"), "main");

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public MagmaGolemModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.rightArm = this.body.getChild("right_arm");
        this.leftArm = this.body.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();

        PartDefinition body = part.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-14.0F, -28.0F, -8.0F, 28.0F, 24.0F, 16.0F)
                .texOffs(0, 40).addBox(-10.0F, -4.0F, -6.0F, 20.0F, 8.0F, 12.0F),
                PartPose.offset(0.0F, 8.0F, 0.0F));

        body.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(64, 40).addBox(-6.0F, -10.0F, -8.0F, 12.0F, 12.0F, 12.0F),
                PartPose.offset(0.0F, -28.0F, -2.0F));

        body.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(0, 60).addBox(-10.0F, -4.0F, -5.0F, 10.0F, 34.0F, 10.0F),
                PartPose.offset(-14.0F, -24.0F, 0.0F));

        body.addOrReplaceChild("left_arm", CubeListBuilder.create()
                .texOffs(40, 60).addBox(0.0F, -4.0F, -5.0F, 10.0F, 34.0F, 10.0F),
                PartPose.offset(14.0F, -24.0F, 0.0F));

        part.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(80, 64).addBox(-5.0F, 0.0F, -5.0F, 10.0F, 12.0F, 10.0F),
                PartPose.offset(-7.0F, 12.0F, 0.0F));

        part.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(80, 64).addBox(-5.0F, 0.0F, -5.0F, 10.0F, 12.0F, 10.0F),
                PartPose.offset(7.0F, 12.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(MagmaGolemEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        if (!entity.isAwake()) {
            float progress = Mth.clamp(entity.getAwakeningTicks() / 80.0F, 0.0F, 1.0F);
            this.body.xRot = (1.0F - progress) * 0.65F;
            this.head.xRot = (1.0F - progress) * 0.5F;
            this.rightArm.xRot = (1.0F - progress) * -0.8F + Mth.sin(ageInTicks * 0.4F) * progress * 0.2F;
            this.leftArm.xRot = (1.0F - progress) * -0.8F - Mth.sin(ageInTicks * 0.4F) * progress * 0.2F;
            return;
        }
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6F) * 1.2F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6F + (float) Math.PI) * 1.2F * limbSwingAmount;
        this.rightArm.xRot = Mth.cos(limbSwing * 0.6F + (float) Math.PI) * 1.1F * limbSwingAmount;
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6F) * 1.1F * limbSwingAmount;
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
