package cron.job;

import cron.trigger.SchedulingTrigger;

/**
 * An abstract class represents a schedulable object. It defines the
 * basic functionalities for a scheduled job, including retrieving various
 * job details and managing execution scheduling.
 */
public abstract class Schedulable implements Comparable<Schedulable> {

    /**
     * Retrieves the unique identifier for this schedulable object.
     *
     * @return the schedule ID as a String
     */
    public abstract String getScheduleId();

    /**
     * Retrieves the `Executable` object associated with this schedulable
     * object, representing the actual task to be executed.
     *
     * @return the `Executable` object
     */
    public abstract Executable getJob();

    /**
     * Retrieves the `SchedulingTrigger` object associated with this
     * schedulable object, defining the scheduling logic for the job.
     *
     * @return the `SchedulingTrigger` object
     */
    public abstract SchedulingTrigger getTrigger();

    /**
     * Checks if this schedulable object is currently scheduled for immediate
     * execution based on its `SchedulingTrigger` logic.
     *
     * @return true if the job is scheduled to run now, false otherwise
     */
    public abstract boolean getIsScheduledNow();

    /**
     * Updates the internal state of this SchedulingTrigger object to reflect
     * the next scheduled execution time.
     */
    public abstract void updateNextScheduleTime();

    /**
     * Increments the internal counter for successfully completed executions
     * of this schedulable object.
     */
    public abstract void incrementCompletedExecutions();

    /**
     * Increments the internal counter for attempted executions of this
     * schedulable object, including both successful and failed attempts.
     */
    public abstract void incrementAttemptedExecutions();

    /**
     * Implements the `Comparable` interface for sorting `Schedulable` objects
     * based on their next scheduled execution time. This method uses the
     * `getNextScheduleTime()` method of the associated `SchedulingTrigger`
     * object for comparison.
     *
     * @param scheduledJob the `Schedulable` object to compare with
     * @return a negative integer if this object's next schedule time is
     *         earlier, a positive integer if it's later, or 0 if they are equal
     */
    @Override
    public int compareTo(Schedulable scheduledJob) {
        return this.getTrigger().getNextScheduleTime()
                .compareTo(scheduledJob.getTrigger().getNextScheduleTime());
    }
}
