package cron.time;

import java.util.Calendar;

/**
 * An interface represents a scheduling period.
 */
public interface SchedulingPeriod {

    /**
     * Calculates the end time of the scheduling period interval based on
     * the provided starting time (`time`).
     */
    public Calendar getIntervalEndTimeFromGivenTime(Calendar time);
}