package org.redis.common;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;

/**
 * @author Lior Hasson
 */
public class Utils {

    public static final String PRIORITY_QUEUE = "__PRIORITY_QUEUE__";
    public static final String TASK_MESSAGE = "TASK_MESSAGE";
    public static final String TIME = "TIME";

    public static RedisClient initClient() {
        RedisURI redisUri = RedisURI.Builder.redis("localhost")
                .withPassword("password")
                .withDatabase(10)
                .build();
        return RedisClient.create(redisUri);
    }
}
