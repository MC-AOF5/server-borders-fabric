package net.morbo.serverborders.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.morbo.serverborders.ServerBorders;
import net.morbo.serverborders.WorldDirection;
import net.morbo.serverborders.config.Configs;
import net.morbo.serverborders.redis.RedisMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
	private List<String> outOfBorderPlayers = new ArrayList<>();

	protected ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}

	@Inject(method = "playerTick", at = @At("TAIL"))
	public void onTick(CallbackInfo ci) {
		if (this.isOutOfBorders(this.getPos())) {
			boolean isFirstDetect = !outOfBorderPlayers.contains(this.getName().getString());
			if (isFirstDetect) {
				ServerBorders.LOGGER.info("Player " + this.getName().getString() + " exceeded bounds");
				outOfBorderPlayers.add(this.getName().getString());

				sendRedisMessage();
			}
		} else {
			boolean isNotFirstDetect = outOfBorderPlayers.contains(this.getName().getString());
			if (isNotFirstDetect) {
				ServerBorders.LOGGER.info("Player " + this.getName().getString() + " entered bounds");
				outOfBorderPlayers.remove(this.getName().getString());
			}
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

	private void sendRedisMessage() {
		RedisMessage msg = new RedisMessage(
			this.getName().getString(),
			this.getCollidedDirection()
		);
		ServerBorders.REDIS.send(msg);
	}

	private WorldDirection getCollidedDirection() {
		if (this.getPos().getX() > Configs.borderPositiveX) {
			return WorldDirection.EAST;
		} else if (this.getPos().getX() < Configs.borderNegativeX) {
			return WorldDirection.WEST;
		} else if (this.getPos().getZ() > Configs.borderPositiveZ) {
			return WorldDirection.SOUTH;
		} else {
			return WorldDirection.NORTH;
		}
	}
}
