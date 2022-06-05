package net.morbo.serverborders.redis;

import net.morbo.serverborders.ServerBorders;
import net.morbo.serverborders.config.Configs;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisListener {
    private static JedisPool jedisPool;

    public RedisListener() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(0);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        if (Configs.redisPassword.isEmpty()) {
            jedisPool = new JedisPool(config,
                    Configs.redisHost,
                    Configs.redisPort,
                    0,
                    Configs.redisSSL);
        } else {
            jedisPool = new JedisPool(config,
                    Configs.redisHost,
                    Configs.redisPort,
                    0,
                    Configs.redisPassword,
                    Configs.redisSSL);
        }
    }

    public void send(RedisMessage msg) {
        try {
            ServerBorders.LOGGER.info("Send to Redis");
            try (Jedis publisher = jedisPool.getResource()) {
                publisher.publish(msg.REDIS_CHANNEL, msg.getRawMessage());
            }
        } catch (JedisConnectionException e) {
            ServerBorders.LOGGER.error("Failed to send Redis message");
            e.printStackTrace();
        }
    }
}
