package net.morbo.serverborders;

import net.fabricmc.api.ModInitializer;
import net.morbo.serverborders.config.SimpleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerBoards implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ServerBorders");

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing...");
	}
}
