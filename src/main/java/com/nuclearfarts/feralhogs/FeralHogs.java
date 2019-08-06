package com.nuclearfarts.feralhogs;

import java.util.Random;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

public class FeralHogs implements ModInitializer {
	
	public static final EntityType<FeralHogEntity> FERAL_HOG = new EntityType<FeralHogEntity>(FeralHogEntity::new, EntityCategory.MONSTER, true, true, false, false, EntityDimensions.changing(0.9f, 0.9f));
	private static final Random RAND = new Random();
	
	private int ticksUntilSecond = 0;
	
	@Override
	public void onInitialize() {
		Registry.register(Registry.ENTITY_TYPE, new Identifier("feralhogs", "feral_hog"), FERAL_HOG);
		ServerTickCallback.EVENT.register(this::serverTickCallback);
	}
	
	public void serverTickCallback(MinecraftServer server) {
		ticksUntilSecond--;
		if(ticksUntilSecond <= 0) {
			for(ServerPlayerEntity p : server.getPlayerManager().getPlayerList()) {
				HogTarget hog = (HogTarget)p;
				int sec = hog.getSecondsUntilHogs();
				if(sec <= 1) {
					spawnHogs(p);
					hog.setSecondsUntilHogs(threeToFiveMinutes());
				} else {
					hog.setSecondsUntilHogs(sec - 1);
				}
			}
			ticksUntilSecond = 20;
		}
	}
	
	private void spawnHogs(ServerPlayerEntity p) {
		int hogs = thirtyToFifty();
		BlockPos playerPos = p.getBlockPos();
		int attemptCounter = 0;
		for(int i = 0; i < hogs && attemptCounter < 400; i++) {
			BlockPos hogSpawn = getValidHogSpawn(generateHogSpawn(playerPos), p.world);
			if(hogSpawn != null) {
				FeralHogEntity toSpawn = FERAL_HOG.create(p.world);
				toSpawn.setPosition(hogSpawn.getX() + 0.5, hogSpawn.getY(), hogSpawn.getZ() + 0.5);
				p.world.spawnEntity(toSpawn);
			} else {
				i--;
			}
			attemptCounter++;
		}
		System.out.println(attemptCounter);
	}
	
	private BlockPos getValidHogSpawn(BlockPos maxY, World in) {
		int x = maxY.getX();
		int z = maxY.getZ();
		if(in.isSkyVisible(maxY)) {
			return new BlockPos(x, in.getChunk(maxY).sampleHeightmap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z) + 1, z);
		} else {
			int minY = maxY.getY() - 10;
			for(int y = maxY.getY(); y >= minY && y >= 0; y--) {
				if(in.getBlockState(new BlockPos(x, y, z)).getBlock().canMobSpawnInside() && !in.getBlockState(new BlockPos(x, y - 1, z)).getBlock().canMobSpawnInside()) {
					return new BlockPos(x, y, z);
				}
			}
		}
		return null;
	}
	
	private BlockPos generateHogSpawn(BlockPos center) {
		return center.add(new Vec3i(RAND.nextInt(100) - 50, 5, RAND.nextInt(100) - 50));
	}
	
	public static int thirtyToFifty() {
		return 30 + RAND.nextInt(20 + 1);
	}
	
	public static int threeToFiveMinutes() {
		return 3 * 60 + RAND.nextInt((2 * 60) + 1);
	}
}
