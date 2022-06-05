package net.morbo.serverborders;

import net.fabricmc.api.ModInitializer;
import net.morbo.serverborders.redis.RedisListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerBorders implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ServerBorders");
	public static RedisListener REDIS;

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing...");
//		try {
//			REDIS = new RedisListener();
//		} catch (JedisConnectionException e) {
//			LOGGER.error("Failed connect to Redis");
//			e.printStackTrace();
//		}
	}
}
