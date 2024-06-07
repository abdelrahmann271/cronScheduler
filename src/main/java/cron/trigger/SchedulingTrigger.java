package cron.trigger;

import java.util.Calendar;

/**
 * An interface defines a scheduling trigger.
 */
public interface SchedulingTrigger {

    /**
     * Calculates and returns the next scheduled time for job execution.
     */
    public Calendar getNextScheduleTime();

    /**
     * Updates the next scheduled time for job execution.
     */
    public Calendar updateNextScheduleTime();

    /**
     * Retrieves whether this trigger will fire one-time or periodically.
     */
    public boolean getIsOneTimeExecution();
}

