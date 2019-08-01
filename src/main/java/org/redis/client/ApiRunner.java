package org.redis.client;

import java.util.Date;

/**
 * @author Lior Hasson
 */
public class ApiRunner {

    public static void main(String[] args) throws InterruptedException {
        ApiService api = new ApiService();

        while (true) {
            Date date = new Date();
            api.echoAtTime(date, "Triggering a new event in: " + date.toString());

            long schedule = (long) (Math.random() * 1000);

            Thread.sleep(schedule);
        }
    }
}
