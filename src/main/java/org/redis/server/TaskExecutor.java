package org.redis.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Lior Hasson
 */
public class TaskExecutor {
    private static int CORE_POOL_SIZE = 10;
    private static int POOL_SIZE = 1000;

    private ExecutorService threadPool;

    public TaskExecutor() {
        this.threadPool = createExecutor();
    }

    public void submit(TaskWrapper taskWrapper) {
        threadPool.submit(taskWrapper);
    }

    public void shutdown() {
        threadPool.shutdown();
    }

    private ExecutorService createExecutor() {
        return new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                CORE_POOL_SIZE,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(POOL_SIZE));
    }

}
