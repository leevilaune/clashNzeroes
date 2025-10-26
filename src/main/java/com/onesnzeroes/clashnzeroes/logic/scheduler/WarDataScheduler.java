package com.onesnzeroes.clashnzeroes.logic.scheduler;

import com.onesnzeroes.clashnzeroes.dao.TrackedWarDao;
import com.onesnzeroes.clashnzeroes.logic.manager.WarManager;
import com.onesnzeroes.clashnzeroes.model.tracked.TrackedWar;
import com.onesnzeroes.clashnzeroes.model.war.WarEntity;
import com.onesnzeroes.clashnzeroes.util.Trace;

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

    private TrackedWarDao dao;

    public WarDataScheduler(WarManager wm) {
        this.warManager = wm;
        this.scheduler = Executors.newScheduledThreadPool(4);
        this.dao = new TrackedWarDao();
        loadTracked();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down " + Instant.now());
            scheduler.shutdown();
        }));
    }

    public void scheduleWarRecording(String clanTag, long warEndTime, Long preparationStartTime) {
        if(alreadyScheduled.contains(clanTag)) return;
        if(Instant.now().getEpochSecond() > warEndTime) return;
        alreadyScheduled.add(clanTag);
        this.dao.persistIfNotExists(new TrackedWar(clanTag,warEndTime, preparationStartTime));

        long nowMillis = Instant.now().toEpochMilli();
        long warEndMillis = warEndTime * 1000;
        long delayMillis = warEndMillis - nowMillis;
        if (delayMillis < 0) delayMillis = 0;

        Runnable recordTask = () -> {
            try {
                Trace.info("Recording clan war for " + clanTag + " at " + Instant.now());
                warManager.saveCurrentWar(clanTag);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                alreadyScheduled.remove(clanTag);
            }
        };

        this.scheduler.schedule(recordTask, (delayMillis+1000), TimeUnit.MILLISECONDS);
        Trace.info("Scheduled war recording for clan " + clanTag + " in " + (delayMillis+1000) + " ms");
    }

    public void loadTracked(){
        List<TrackedWar> trackedWars = this.dao.findAll();
        trackedWars.forEach(w -> scheduleWarRecording(w.getTag(),w.getEndTs(),w.getPreparationStartTs()));
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