package org.redis.common;

/**
 * @author Lior Hasson
 */
public class Task {

    private String key;
    private String message;
    private long time;

    public Task(long time, String message) {
        this.message = message;
        this.time = time;
        this.key = "task_" + time;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }

    public String getKey() {
        return key;
    }
}
