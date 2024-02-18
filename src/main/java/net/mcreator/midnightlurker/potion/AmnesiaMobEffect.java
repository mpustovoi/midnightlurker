
package net.mcreator.midnightlurker.potion;

import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.mcreator.midnightlurker.procedures.AmnesiaOnEffectActiveTickProcedure;

public class AmnesiaMobEffect extends MobEffect {
	public AmnesiaMobEffect() {
		super(MobEffectCategory.HARMFUL, -12708839);
	}

	@Override
	public String getDescriptionId() {
		return "effect.midnightlurker.amnesia";
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		AmnesiaOnEffectActiveTickProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void initializeClient(java.util.function.Consumer<IClientMobEffectExtensions> consumer) {
		consumer.accept(new IClientMobEffectExtensions() {
			@Override
			public boolean isVisibleInGui(MobEffectInstance effect) {
				return false;
			}
		});
	}
}