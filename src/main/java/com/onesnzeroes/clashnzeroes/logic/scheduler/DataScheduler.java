package com.onesnzeroes.clashnzeroes.logic.scheduler;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A scheduler that runs a periodic task at a fixed interval.
 * <p>
 * The scheduler uses a single-threaded {@link ScheduledExecutorService} to execute
 * the {@link #scheduledAction()} method at fixed intervals defined by {@code periodMillis}.
 * It also provides a shutdown hook to cleanly stop the scheduler when the JVM exits.
 * </p>
 */
public class DataScheduler {

    /** The executor service responsible for running scheduled tasks. */
    private ScheduledExecutorService scheduler;

    /** The period between successive executions of the task, in milliseconds. */
    private long periodMillis;

    /**
     * Constructs a new DataScheduler with the specified period.
     *
     * @param periodMillis the interval between task executions in milliseconds
     */
    public DataScheduler(long periodMillis) {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.periodMillis = periodMillis;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down " + Instant.now());
            scheduler.shutdown();
        }));
    }

    /**
     * Schedules the periodic task to run at fixed rate.
     * <p>
     * The task calls {@link #scheduledAction()} and then {@link #onFinish()} after each execution.
     * </p>
     */
    public void schedule() {
        Runnable saveTask = () -> {
            try {
                scheduledAction();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                onFinish();
            }
        };

        this.scheduler.scheduleAtFixedRate(saveTask, computeDelay(), this.periodMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * The action that will be executed on each scheduled run.
     * <p>
     * Override this method to implement the actual task logic.
     * </p>
     */
    public void scheduledAction() {
        System.out.println("Scheduled Action at " + Instant.now());
    }

    /**
     * Called after each scheduled task execution, regardless of whether it completed successfully.
     * <p>
     * Override this method to implement cleanup or follow-up logic.
     * </p>
     */
    public void onFinish() {
        System.out.println("Finished Action at " + Instant.now());
    }

    /**
     * Computes the initial delay before the first execution of the task.
     * <p>
     * Override this method to delay the first execution if desired. The default is 0.
     * </p>
     *
     * @return the delay in milliseconds before the first execution
     */
    public long computeDelay() {
        return 0;
    }

}