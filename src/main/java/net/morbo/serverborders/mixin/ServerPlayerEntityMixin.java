package net.morbo.serverborders.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.morbo.serverborders.config.Configs;
import net.morbo.serverborders.ServerBoards;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
	protected ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}

	@Inject(method = "playerTick", at = @At("TAIL"))
	public void onTick(CallbackInfo ci) {
		if (this.isOutOfBorders(this.getPos())) {
			ServerBoards.LOGGER.info("Player tick out of borders");
			// TODO: Send info to Redis PubSub
		}
	}

	private boolean isOutOfBorders(Vec3d pos) {
		return (
			pos.getX() > Configs.borderPositiveX ||
			pos.getX() < Configs.borderNegativeX ||
			pos.getZ() > Configs.borderPositiveZ ||
			pos.getZ() < Configs.borderNegativeZ
		);
	}
}
