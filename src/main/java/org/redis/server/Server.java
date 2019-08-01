package org.redis.server;

import io.lettuce.core.RedisClient;

import static org.redis.common.Utils.initClient;

/**
 * @author Lior Hasson
 */
public class Server {
    public static void main(String[] args) {
        System.out.println("Starting queue manager!");
        Server server = new Server();
        server.start();
    }

    private void start() {
        RedisClient redisClient = initClient();
        new TaskDispatcher(redisClient).run();
    }

}
