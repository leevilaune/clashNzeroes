package com.onesnzeroes.clashnzeroes.manager;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlayerDataScheduler {

    private PlayerManager playerManager;
    private ScheduledExecutorService scheduler;

    public PlayerDataScheduler(PlayerManager pm) {
        this.playerManager = pm;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void startEvenHourSave() {
        Runnable saveTask = () -> {
            try {
                System.out.println("Saving player data at " + Instant.now());
                this.playerManager.savePlayers();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        long initialDelay = computeDelayToNextEvenHour();
        System.out.println("First save in " + initialDelay + " seconds");

        this.scheduler.scheduleAtFixedRate(saveTask, initialDelay, 3600, TimeUnit.SECONDS);
    }

    private long computeDelayToNextEvenHour() {
        Instant now = Instant.now();
        long epochSeconds = now.getEpochSecond();

        long secondsInDay = epochSeconds % 86400; // 24*60*60
        long currentHour = secondsInDay / 3600;

        long nextEvenHour = (currentHour % 2 == 0) ? currentHour + 2 : currentHour + 1;
        if (nextEvenHour >= 24) nextEvenHour -= 24;

        long nextEvenHourSeconds = nextEvenHour * 3600;

        return nextEvenHourSeconds - secondsInDay;
    }
    public void stop() {
        this.scheduler.shutdown();
    }
}
