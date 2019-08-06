package com.nuclearfarts.feralhogs.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nuclearfarts.feralhogs.FeralHogs;
import com.nuclearfarts.feralhogs.HogTarget;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements HogTarget {
	
	@Unique
	private int secondsUntilHogs = FeralHogs.threeToFiveMinutes();
	
	@Inject(at = @At("TAIL"), method = "writeCustomDataToTag(Lnet/minecraft/nbt/CompoundTag;)V")
	private void write(CompoundTag tag, CallbackInfo info) {
		tag.putInt("HogTimer", secondsUntilHogs);
	}
	
	@Inject(at = @At("TAIL"), method = "readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V")
	private void read(CompoundTag tag, CallbackInfo info) {
		if(tag.containsKey("HogTimer")) {
			System.out.println("Loaded " + secondsUntilHogs);
		}
	}
	
	public void setSecondsUntilHogs(int to) {
		secondsUntilHogs = to;
	}
	
	public int getSecondsUntilHogs() {
		return secondsUntilHogs;
	}
}
