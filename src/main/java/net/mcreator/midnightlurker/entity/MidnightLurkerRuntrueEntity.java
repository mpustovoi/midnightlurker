
package net.mcreator.midnightlurker.entity;

import software.bernie.geckolib3.util.GeckoLibUtil;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.IAnimatable;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.protocol.Packet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

import net.mcreator.midnightlurker.procedures.MidnightLurkerRuntrueOnInitialEntitySpawnProcedure;
import net.mcreator.midnightlurker.procedures.MidnightLurkerRuntrueOnEntityTickUpdateProcedure;
import net.mcreator.midnightlurker.procedures.MidnightLurkerEntityDiesProcedure;
import net.mcreator.midnightlurker.procedures.LurkerinwaterconditionProcedure;
import net.mcreator.midnightlurker.init.MidnightlurkerModEntities;

import javax.annotation.Nullable;

public class MidnightLurkerRuntrueEntity extends Monster implements IAnimatable {
	public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(MidnightLurkerRuntrueEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(MidnightLurkerRuntrueEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(MidnightLurkerRuntrueEntity.class, EntityDataSerializers.STRING);
	private AnimationFactory factory = GeckoLibUtil.createFactory(this);
	private boolean swinging;
	private boolean lastloop;
	private long lastSwing;
	public String animationprocedure = "empty";

	public MidnightLurkerRuntrueEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(MidnightlurkerModEntities.MIDNIGHT_LURKER_RUNTRUE.get(), world);
	}

	public MidnightLurkerRuntrueEntity(EntityType<MidnightLurkerRuntrueEntity> type, Level world) {
		super(type, world);
		xpReward = 25;
		setNoAi(false);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(SHOOT, false);
		this.entityData.define(ANIMATION, "undefined");
		this.entityData.define(TEXTURE, "midnightlurkervoidgate");
	}

	public void setTexture(String texture) {
		this.entityData.set(TEXTURE, texture);
	}

	public String getTexture() {
		return this.entityData.get(TEXTURE);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false, false));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, (float) 100, 1, 1.2));
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, (float) 100));
		this.goalSelector.addGoal(5, new FloatGoal(this) {
			@Override
			public boolean canUse() {
				double x = MidnightLurkerRuntrueEntity.this.getX();
				double y = MidnightLurkerRuntrueEntity.this.getY();
				double z = MidnightLurkerRuntrueEntity.this.getZ();
				Entity entity = MidnightLurkerRuntrueEntity.this;
				Level world = MidnightLurkerRuntrueEntity.this.level;
				return super.canUse() && LurkerinwaterconditionProcedure.execute(entity);
			}

			@Override
			public boolean canContinueToUse() {
				double x = MidnightLurkerRuntrueEntity.this.getX();
				double y = MidnightLurkerRuntrueEntity.this.getY();
				double z = MidnightLurkerRuntrueEntity.this.getZ();
				Entity entity = MidnightLurkerRuntrueEntity.this;
				Level world = MidnightLurkerRuntrueEntity.this.level;
				return super.canContinueToUse() && LurkerinwaterconditionProcedure.execute(entity);
			}
		});
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("midnightlurker:lurkerfootsteps")), 0.15f, 1);
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("midnightlurker:lurkerhurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("midnightlurker:lurkerdeath"));
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.getDirectEntity() instanceof AbstractArrow)
			return false;
		if (source.getDirectEntity() instanceof Player)
			return false;
		if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud)
			return false;
		if (source == DamageSource.FALL)
			return false;
		if (source == DamageSource.CACTUS)
			return false;
		if (source == DamageSource.DROWN)
			return false;
		if (source == DamageSource.LIGHTNING_BOLT)
			return false;
		if (source.isExplosion())
			return false;
		if (source.getMsgId().equals("trident"))
			return false;
		if (source == DamageSource.ANVIL)
			return false;
		if (source == DamageSource.DRAGON_BREATH)
			return false;
		if (source == DamageSource.WITHER)
			return false;
		if (source.getMsgId().equals("witherSkull"))
			return false;
		return super.hurt(source, amount);
	}

	@Override
	public void die(DamageSource source) {
		super.die(source);
		MidnightLurkerEntityDiesProcedure.execute(this.level, this);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
		SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
		MidnightLurkerRuntrueOnInitialEntitySpawnProcedure.execute(world, this.getX(), this.getY(), this.getZ(), this);
		return retval;
	}

	@Override
	public void baseTick() {
		super.baseTick();
		MidnightLurkerRuntrueOnEntityTickUpdateProcedure.execute(this.level, this.getX(), this.getY(), this.getZ(), this);
		this.refreshDimensions();
	}

	@Override
	public EntityDimensions getDimensions(Pose p_33597_) {
		return super.getDimensions(p_33597_).scale((float) 1);
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.42);
		builder = builder.add(Attributes.MAX_HEALTH, 60);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 8);
		builder = builder.add(Attributes.FOLLOW_RANGE, 100);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1);
		return builder;
	}

	private <E extends IAnimatable> PlayState movementPredicate(AnimationEvent<E> event) {
		if (this.animationprocedure.equals("empty")) {
			if ((event.isMoving() || !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F))

					&& !this.isAggressive()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("running6", EDefaultLoopTypes.LOOP));
				return PlayState.CONTINUE;
			}
			if (this.isInWaterOrBubble()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("swim6", EDefaultLoopTypes.LOOP));
				return PlayState.CONTINUE;
			}
			if (this.isAggressive() && event.isMoving()) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation("stalking6", EDefaultLoopTypes.LOOP));
				return PlayState.CONTINUE;
			}
			event.getController().setAnimation(new AnimationBuilder().addAnimation("idle6", EDefaultLoopTypes.LOOP));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	private <E extends IAnimatable> PlayState procedurePredicate(AnimationEvent<E> event) {
		Entity entity = this;
		Level world = entity.level;
		boolean loop = false;
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		if (!loop && this.lastloop) {
			this.lastloop = false;
			event.getController().setAnimation(new AnimationBuilder().addAnimation(this.animationprocedure, EDefaultLoopTypes.PLAY_ONCE));
			event.getController().clearAnimationCache();
			return PlayState.STOP;
		}
		if (!this.animationprocedure.equals("empty") && event.getController().getAnimationState().equals(software.bernie.geckolib3.core.AnimationState.Stopped)) {
			if (!loop) {
				event.getController().setAnimation(new AnimationBuilder().addAnimation(this.animationprocedure, EDefaultLoopTypes.PLAY_ONCE));
				if (event.getController().getAnimationState().equals(software.bernie.geckolib3.core.AnimationState.Stopped)) {
					this.animationprocedure = "empty";
					event.getController().markNeedsReload();
				}
			} else {
				event.getController().setAnimation(new AnimationBuilder().addAnimation(this.animationprocedure, EDefaultLoopTypes.LOOP));
				this.lastloop = true;
			}
		}
		return PlayState.CONTINUE;
	}

	@Override
	protected void tickDeath() {
		++this.deathTime;
		if (this.deathTime == 20) {
			this.remove(MidnightLurkerRuntrueEntity.RemovalReason.KILLED);
			this.dropExperience();
		}
	}

	public String getSyncedAnimation() {
		return this.entityData.get(ANIMATION);
	}

	public void setAnimation(String animation) {
		this.entityData.set(ANIMATION, animation);
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController<>(this, "movement", 4, this::movementPredicate));
		data.addAnimationController(new AnimationController<>(this, "procedure", 4, this::procedurePredicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}
}