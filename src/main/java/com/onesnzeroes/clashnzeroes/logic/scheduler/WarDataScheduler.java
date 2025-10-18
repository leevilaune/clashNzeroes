package com.onesnzeroes.clashnzeroes.logic.scheduler;

import com.onesnzeroes.clashnzeroes.logic.manager.WarManager;
import com.onesnzeroes.clashnzeroes.model.war.WarEntity;

import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WarDataScheduler {

    private WarManager warManager;
    private ScheduledExecutorService scheduler;
    private static List<String> alreadyScheduled = Collections.synchronizedList(new ArrayList<>());

    public WarDataScheduler(WarManager wm) {
        this.warManager = wm;
        this.scheduler = Executors.newScheduledThreadPool(4);
    }

    public void scheduleWarRecording(String clanTag, long warEndTime) {
        if(alreadyScheduled.contains(clanTag)) return;
        alreadyScheduled.add(clanTag);

        long nowMillis = Instant.now().toEpochMilli();
        long warEndMillis = warEndTime * 1000;
        long delayMillis = warEndMillis - nowMillis;
        if (delayMillis < 0) delayMillis = 0;

        Runnable recordTask = () -> {
            try {
                System.out.println("Recording clan war for " + clanTag + " at " + Instant.now());
                warManager.saveCurrentWar(clanTag);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                alreadyScheduled.remove(clanTag);
            }
        };

        this.scheduler.schedule(recordTask, delayMillis, TimeUnit.MILLISECONDS);
        System.out.println("Scheduled war recording for clan " + clanTag + " in " + delayMillis + " ms");
    }

    public void stop() {
        this.scheduler.shutdown();
    }

    public WarManager getWarManager() {
        return warManager;
    }

    public void setWarManager(WarManager warManager) {
        this.warManager = warManager;
    }
}