package com.onesnzeroes.clashnzeroes.logic.scheduler;

import com.onesnzeroes.clashnzeroes.logic.manager.PlayerManager;
import com.onesnzeroes.clashnzeroes.util.Trace;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class PlayerDataScheduler extends DataScheduler {

    private PlayerManager playerManager;
    private ScheduledExecutorService scheduler;
    private WarDataScheduler warScheduler;

    public PlayerDataScheduler(PlayerManager pm, long periodMillis, WarDataScheduler warScheduler) {
        super(periodMillis);
        this.playerManager = pm;
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.warScheduler = warScheduler;
    }

    @Override
    public long computeDelay() {
        Instant now = Instant.now();
        long epochSeconds = now.getEpochSecond();

        long secondsInDay = epochSeconds % 86400; // 24*60*60
        long currentHour = secondsInDay / 3600;

        long nextEvenHour = (currentHour % 2 == 0) ? currentHour + 2 : currentHour + 1;
        if (nextEvenHour >= 24) nextEvenHour -= 24;

        long nextEvenHourSeconds = nextEvenHour * 3600;

        return nextEvenHourSeconds - secondsInDay;
    }

    @Override
    public void scheduledAction(){
        Trace.info("Scheduled player save at " + Instant.now());
        this.playerManager.savePlayers();
    }

    @Override
    public void onFinish(){
        Trace.info("Finished saving players at " + Instant.now());
    }

    public void stop() {
        this.scheduler.shutdown();
    }
}
