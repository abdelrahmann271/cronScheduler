package cron.job;

import cron.trigger.SchedulingTrigger;
import cron.utils.IDGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;

/**
 * A class represents a concrete implementation of the
 * `Schedulable` interface. It encapsulates an `Executable` object
 * (the job to be executed) and a `SchedulingTrigger` object
 * (defining the scheduling logic) along with additional information
 * related to job execution tracking.
 */
public final class ScheduledJob extends Schedulable {
    private static final Logger logger = LogManager.getLogger(ScheduledJob.class);
    private final String scheduleId;
    private final Executable job;

    public void setTrigger(SchedulingTrigger trigger) {
        this.trigger = trigger;
    }

    private SchedulingTrigger trigger;
    private long completedExecutions = 0;
    private long attemptedExecutions = 0;

    /**
     * Creates a new `ScheduledJob` object with the specified job, trigger,
     * and automatically generates a unique schedule ID.
     *
     * @param job the `Executable` object representing the job to be executed (must not be null)
     * @param trigger the `SchedulingTrigger` object defining the schedule (must not be null)
     * @throws IllegalArgumentException if either job or trigger is null
     */
    public ScheduledJob(Executable job, SchedulingTrigger trigger) {
        if (job == null || trigger == null) {
            throw new IllegalArgumentException("Job and trigger cannot be null.");
        }
        this.scheduleId = IDGenerator.generateId(); // Assuming IDGenerator exists
        this.job = job;
        this.trigger = trigger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getScheduleId() {
        return scheduleId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Executable getJob() {
        return job;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SchedulingTrigger getTrigger() {
        return trigger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getIsScheduledNow() {
        Calendar currTime = Calendar.getInstance();
        Calendar nextScheduleTime = trigger.getNextScheduleTime();
        return !(nextScheduleTime.getTimeInMillis() - currTime.getTimeInMillis() > 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateNextScheduleTime() {
        Calendar nextScheduledTime = trigger.updateNextScheduleTime();
        logger.info("Updated next schedule time for scheduled job with ID (" + getScheduleId() + ") to "
                + nextScheduledTime.getTime());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void incrementCompletedExecutions() {
        completedExecutions++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void incrementAttemptedExecutions() {
        attemptedExecutions++;
    }

    /**
     * Retrieves the next scheduled execution time for this job based on
     * the associated `SchedulingTrigger`.
     *
     * @return a `Calendar` object representing the next scheduled execution time
     */
    public Calendar getNextScheduleTime() {
        return trigger.getNextScheduleTime();
    }

    /**
     * Retrieves the number of successfully completed executions for this job.
     *
     * @return the number of completed executions
     */
    public long getNumberOfCompletedExecutions() {
        return completedExecutions;
    }

    /**
     * Retrieves the number of attempted executions for this job.
     *
     * @return the number of attempted executions
     */
    public long getNumberOfAttemptedExecutions() {
        return attemptedExecutions;
    }
}
