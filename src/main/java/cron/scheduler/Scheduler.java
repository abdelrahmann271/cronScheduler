package cron.scheduler;

import cron.job.Schedulable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * A final class implements the `SchedulingService` interface and provides
 * a thread-based scheduler service for managing and executing scheduled jobs.
 */
public final class Scheduler implements Runnable, SchedulingService {
    private static final Logger logger = LogManager.getLogger(Scheduler.class);
    private final PriorityBlockingQueue<Schedulable> scheduledJobsQueue;
    private final Map<String, Schedulable> scheduledJobs;
    private final ExecutorService executorService;
    private final Timer timer;

    private static volatile Scheduler instance;

    private Scheduler() {
        this.scheduledJobsQueue = new PriorityBlockingQueue<>();
        this.scheduledJobs = new ConcurrentHashMap<>();
        this.executorService = Executors.newCachedThreadPool();
        this.timer = new Timer();
    }

    protected static Scheduler getInstance() {
        if (instance == null) {
            synchronized (Scheduler.class) {
                if (instance == null) {
                    instance = new Scheduler();
                }
            }
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        logger.info("Scheduler started.");
        new Thread(this).start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addScheduledJob(Schedulable scheduledJob) {
        if (scheduledJob == null) {
            throw new IllegalArgumentException("Cannot add a null scheduled job.");
        }
        if (scheduledJobs.containsKey(scheduledJob.getScheduleId())) {
            logger.error("Failed to add scheduled job with name (" + scheduledJob.getJob().getName()+ ") and ID "
                         + "(" + scheduledJob.getScheduleId() + ") as it already exists.");
            return false;
        }
        scheduledJobs.put(scheduledJob.getScheduleId(), scheduledJob);
        logger.info("Scheduled job with name (" + scheduledJob.getJob().getName()+ ") and ID "
                    + "(" + scheduledJob.getScheduleId() + ") is added to the scheduler.");
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeScheduledJob(Schedulable scheduledJob) {
        if (! scheduledJobs.containsKey(scheduledJob.getScheduleId())) {
            logger.error("Failed to remove scheduled job with name (" + scheduledJob.getJob().getName()+ ") and ID "
                         + "(" + scheduledJob.getScheduleId() + ") as it does not exist in the scheduler.");
            return false;
        }
        stopScheduledJob(scheduledJob);
        scheduledJobs.remove(scheduledJob.getScheduleId());
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startScheduledJob(Schedulable scheduledJob) {
        if (! scheduledJobs.containsKey(scheduledJob.getScheduleId())) {
            logger.error("Failed to start scheduled job with name (" + scheduledJob.getJob().getName()+ ") and ID "
                         + "(" + scheduledJob.getScheduleId() + ") as it does not exist in the scheduler.");
            return false;
        }
        if (scheduledJobsQueue.contains(scheduledJob)) {
            logger.error("Failed to start scheduled job with name (" + scheduledJob.getJob().getName()+ ") and ID "
                         + "(" + scheduledJob.getScheduleId() + ") as it is already started.");
            return false;
        }
        scheduledJobsQueue.add(scheduledJob);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopScheduledJob(Schedulable scheduledJob) {
        if (! scheduledJobs.containsKey(scheduledJob.getScheduleId())) {
            logger.error("Failed to stop scheduled job with name (" + scheduledJob.getJob().getName()+ ") and ID "
                         + "(" + scheduledJob.getScheduleId() + ") as it does not exist in the scheduler.");
            return false;
        }
        if (! scheduledJobsQueue.contains(scheduledJob)) {
            logger.error("Failed to stop scheduled job with name (" + scheduledJob.getJob().getName()+ ") and ID "
                         + "(" + scheduledJob.getScheduleId() + ") as it is already stopped.");
            return false;
        }
        scheduledJobsQueue.remove(scheduledJob);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startAllScheduledJobs() {
        for (Schedulable scheduledJob : this.scheduledJobs.values()) {
            try {
                startScheduledJob(scheduledJob);
            }
            catch (Exception e) {
                logger.error("Error while starting scheduled job with name ("
                             + scheduledJob.getJob().getName()+ ") and ID " + "(" + scheduledJob.getScheduleId()
                             + ").", e);
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopAllScheduledJobs() {
        synchronized (scheduledJobsQueue) {
            scheduledJobsQueue.clear();
        }
        logger.info("All schedules jobs are stopped.");
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getNumberOfScheduledJobs() {
        return scheduledJobsQueue.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getNumberOfAddedJobs() {
        return scheduledJobs.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shutdown() {
        try {
            stopAllScheduledJobs();
            executorService.shutdown();
            return true;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runScheduledJobs();
            }
        }, 0, 1000);
    }

    /**
     * Checks the `scheduledJobsQueue` and run jobs that are ready to be executed.
     */
    private void runScheduledJobs() {
        while (! scheduledJobsQueue.isEmpty() && scheduledJobsQueue.peek().getIsScheduledNow()) {
                Schedulable scheduledJob = scheduledJobsQueue.poll();
                logger.info("Successfully removed scheduled job with name (" + scheduledJob.getJob().getName()+ ") "
                            + "and ID " + scheduledJob.getScheduleId()+ ") from the jobs queue.");
                submitScheduledJob(scheduledJob);
                if (! scheduledJob.getTrigger().getIsOneTimeExecution()) {
                    scheduledJob.updateNextScheduleTime();
                    scheduledJobsQueue.add(scheduledJob);
                }
                logger.info("Successfully added scheduled job with name (" + scheduledJob.getJob().getName()
                            + ") and ID " + scheduledJob.getScheduleId()+ ") to the jobs queue.");
        }
    }

    /**
     * Submits the specified `scheduledJob` for execution using the `ExecutorService`.
     *
     * @param scheduledJob the `Schedulable` object representing the job to be submitted
     */
    private void submitScheduledJob(Schedulable scheduledJob) {
        try {
            this.executorService.submit(scheduledJob.getJob());
            logger.info("Successfully submitted scheduled job with name (" + scheduledJob.getJob().getName()+ ") "
                    + "and ID " + scheduledJob.getScheduleId()+ ").");
            scheduledJob.incrementCompletedExecutions();
        }
        catch (Throwable e) {
            logger.info("Failed to submit scheduled job with name (" + scheduledJob.getJob().getName()+ ") "
                    + "and ID " + scheduledJob.getScheduleId()+ ").");
            scheduledJob.incrementAttemptedExecutions();
        }
    }
}
