
package net.mcreator.midnightlurker.client.renderer;

import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import net.mcreator.midnightlurker.entity.model.MidnightLurkerFakerWatcherModel;
import net.mcreator.midnightlurker.entity.layer.MidnightLurkerFakerWatcherLayer;
import net.mcreator.midnightlurker.entity.MidnightLurkerFakerWatcherEntity;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

public class MidnightLurkerFakerWatcherRenderer extends GeoEntityRenderer<MidnightLurkerFakerWatcherEntity> {
	public MidnightLurkerFakerWatcherRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new MidnightLurkerFakerWatcherModel());
		this.shadowRadius = 0.7f;
		this.addLayer(new MidnightLurkerFakerWatcherLayer(this));
	}

	@Override
	public RenderType getRenderType(MidnightLurkerFakerWatcherEntity entity, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
		stack.scale(0.95f, 0.95f, 0.95f);
		return RenderType.entityTranslucent(getTextureLocation(entity));
	}
}