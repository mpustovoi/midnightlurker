
package net.mcreator.midnightlurker.client.renderer;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import net.mcreator.midnightlurker.entity.model.MidnightLurkerCreepModel;
import net.mcreator.midnightlurker.entity.layer.MidnightLurkerCreepLayer;
import net.mcreator.midnightlurker.entity.MidnightLurkerCreepEntity;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class MidnightLurkerCreepRenderer extends GeoEntityRenderer<MidnightLurkerCreepEntity> {
	public MidnightLurkerCreepRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new MidnightLurkerCreepModel());
		this.shadowRadius = 0.7f;
		this.addRenderLayer(new MidnightLurkerCreepLayer(this));
	}

	@Override
	public RenderType getRenderType(MidnightLurkerCreepEntity animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}

	@Override
	public void preRender(PoseStack poseStack, MidnightLurkerCreepEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red,
			float green, float blue, float alpha) {
		float scale = 0.95f;
		this.scaleHeight = scale;
		this.scaleWidth = scale;
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
