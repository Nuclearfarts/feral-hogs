package com.nuclearfarts.feralhogs;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class FeralHogEntity extends PigEntity {

	public FeralHogEntity(EntityType<? extends FeralHogEntity> entityType_1, World world_1) {
		super(entityType_1, world_1);
	}

	protected void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new MeleeAttackGoal(this, 1.25, true));
		this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
		this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.add(8, new LookAroundGoal(this));
		this.targetSelector.add(2, new FollowTargetGoal<PlayerEntity>(this, PlayerEntity.class, true));
	}
	
	protected void initAttributes() {
		super.initAttributes();
		getAttributeContainer().register(EntityAttributes.ATTACK_DAMAGE).setBaseValue(3);
	}
}
