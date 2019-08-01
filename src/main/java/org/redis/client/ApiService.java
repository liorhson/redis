package org.redis.client;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.redis.common.Task;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.redis.common.Utils.*;

/**
 * @author Lior Hasson
 */
public class ApiService {

    private RedisClient redisClient;

    public ApiService() {
        redisClient = initClient();
    }

    public void echoAtTime(Date date, String message) {
        Task task = new Task(date.getTime(), message);

        try (StatefulRedisConnection<String, String> connect = redisClient.connect()) {
            RedisCommands<String, String> transaction = connect.sync();
            transaction.multi();


            String taskKey = task.getKey();
            Map<String, String> data = new HashMap<>();
            data.put(TASK_MESSAGE, task.getMessage());
            data.put(TIME, String.valueOf(task.getTime()));

            transaction.hmset(taskKey, data);
            transaction.zadd(PRIORITY_QUEUE, task.getTime(), taskKey);
            transaction.exec();
        }
    }
}
