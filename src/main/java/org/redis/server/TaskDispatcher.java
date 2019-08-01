package org.redis.server;

import io.lettuce.core.KeyValue;
import io.lettuce.core.Limit;
import io.lettuce.core.Range;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import org.redis.common.Task;

import java.util.List;

import static org.redis.common.Utils.*;

/**
 * @author Lior Hasson
 */
public class TaskDispatcher implements Runnable {
    private final RedisClient redisClient;

    public TaskDispatcher(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public void run() {
        TaskExecutor taskExecutor = new TaskExecutor();

        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            while (!threadInterrupted()) {
                try {
                    scanQueue(taskExecutor, connection);

                    //Assumption: We can be late by ~100ms in a task
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.out.println("Error while trying to fetch keys from redis: " + e.getMessage());
                }
            }
        } finally {
            taskExecutor.shutdown();
        }
    }

    private void scanQueue(TaskExecutor taskExecutor, StatefulRedisConnection<String, String> connection) {
        Range<? extends Number> range = Range.create(Double.MIN_VALUE, System.currentTimeMillis());
        Limit limit = Limit.from(100);

        List<String> zrangeByScore = connection.sync().zrangebyscore(PRIORITY_QUEUE, range, limit);

        zrangeByScore.forEach(key -> {
            List<KeyValue<String, String>> taskDetails = connection.sync().hmget(key, TASK_MESSAGE, TIME);
            String message = taskDetails.get(0).getValue();
            String time = taskDetails.get(1).getValue();

            Task task = new Task(Long.parseLong(time), message);
            TaskWrapper taskWrapper = new TaskWrapper(redisClient, task);

            taskExecutor.submit(taskWrapper);
        });
    }

    static boolean threadInterrupted() {
        if (Thread.interrupted()) {
            Thread.currentThread().interrupt();
            return true;
        }
        return false;
    }
}
