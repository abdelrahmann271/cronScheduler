package cron.trigger;

import cron.time.TimePeriod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Calendar;

/**
 * A class implements the `SchedulingTrigger` interface and represents a trigger for scheduling jobs.
 */
public final class Trigger implements SchedulingTrigger {
    private static final Logger logger = LogManager.getLogger(Trigger.class);
    private final TimePeriod startDelay;
    private final TimePeriod scheduleFrequency;
    private final Calendar createdAt;

    private boolean isOneTimeExecution = false;
    private Calendar nextScheduleTime;

    /**
     * Creates a new `Trigger` object with the specified start delay and schedule frequency.
     *
     * @param startDelay the time period to wait before the first execution (as a `TimePeriod` object)
     * @param scheduleFrequency the time period between subsequent executions (as a `TimePeriod` object)
     * @throws IllegalArgumentException if either startDelay or scheduleFrequency is null
     */
    public Trigger(TimePeriod startDelay, TimePeriod scheduleFrequency) {
        if (startDelay == null)
            throw new IllegalArgumentException();
        if (scheduleFrequency == null
            || scheduleFrequency.equals(new TimePeriod(0,0,0,0,0,0))) {
            isOneTimeExecution = true;
        }
        this.startDelay = startDelay;
        this.scheduleFrequency = scheduleFrequency;
        this.createdAt = Calendar.getInstance();
        this.nextScheduleTime = startDelay.getIntervalEndTimeFromGivenTime(createdAt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Calendar getNextScheduleTime() {
        return nextScheduleTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Calendar updateNextScheduleTime() {
        if (scheduleFrequency == null) {
            throw new IllegalStateException("Cannot get next scheduled time for a one time trigger.");
        }
        nextScheduleTime = scheduleFrequency.getIntervalEndTimeFromGivenTime(nextScheduleTime);
        return (Calendar) nextScheduleTime.clone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getIsOneTimeExecution() {
        return isOneTimeExecution;
    }

    /**
     * Retrieves the start delay for this trigger.
     *
     * @return the start delay as a `TimePeriod` object
     */
    public TimePeriod getStartDelay() {
        return startDelay;
    }

    /**
     * Retrieves the schedule frequency for this trigger.
     *
     * @return the schedule frequency as a `TimePeriod` object
     */
    public TimePeriod getScheduleFrequency() {
        return scheduleFrequency;
    }

    /**
     * Retrieves the time this trigger was created.
     *
     * @return a `Calendar` object representing the creation time
     */
    public Calendar getCreatedAt() {
        return createdAt;
    }
}
