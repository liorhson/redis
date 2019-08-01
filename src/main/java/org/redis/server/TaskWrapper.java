package org.redis.server;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;
import org.redis.common.Task;

import static org.redis.common.Utils.PRIORITY_QUEUE;

/**
 * @author Lior Hasson
 */
public class TaskWrapper implements Runnable {
    private final Task task;
    private final RedisClient redisClient;

    public TaskWrapper(RedisClient redisClient, Task task) {
        this.task = task;
        this.redisClient = redisClient;
    }

    @Override
    public void run() {
        System.out.println(task.getMessage());

        //Remove the key, and clean the priority queue
        RedisCommands<String, String> transaction = redisClient.connect().sync();
        transaction.multi();
        transaction.del(task.getKey());
        transaction.zrem(PRIORITY_QUEUE, task.getKey());
        transaction.exec();
    }
}
