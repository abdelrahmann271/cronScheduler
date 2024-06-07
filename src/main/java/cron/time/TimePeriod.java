
package cron.time;

import java.util.Calendar;

/**
 * A class represents a specific time period defined by seconds, minutes, hours, days, months, and years.
 */
public final class TimePeriod implements SchedulingPeriod {

    private final int seconds;
    private final int minutes;
    private final int hours;
    private final int days;
    private final int months;
    private final int years;

    /**
     * Creates a new `TimePeriod` object with the specified duration components.
     */
    public TimePeriod(int seconds, int minutes, int hours, int days, int months, int years) {
        if (seconds < 0 || seconds > 59) {
            throw new IllegalArgumentException("Seconds must be between 0 and 59 (inclusive).");
        }
        if (minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException("Minutes must be between 0 and 59 (inclusive).");
        }
        if (hours < 0 || hours > 23) {
            throw new IllegalArgumentException("Hours must be between 0 and 23 (inclusive).");
        }
        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;
        this.days = days;
        this.months = months;
        this.years = years;
    }

    /**
     * Retrieves the number of seconds in this time period.
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Retrieves the number of minutes in this time period.
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Retrieves the number of hours in this time period.
     */
    public int getHours() {
        return hours;
    }

    /**
     * Retrieves the number of days in this time period.
     */
    public int getDays() {
        return days;
    }

    /**
     * Retrieves the number of months in this time period.
     */
    public int getMonths() {
        return months;
    }

    /**
     * Retrieves the number of years in this time period.
     */
    public int getYears() {
        return years;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Calendar getIntervalEndTimeFromGivenTime(Calendar startTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("Start time is required.");
        }

        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.DATE, getDays());
        endTime.add(Calendar.MONTH, getMonths());
        endTime.add(Calendar.YEAR, getYears());
        endTime.add(Calendar.HOUR, getHours());
        endTime.add(Calendar.MINUTE, getMinutes());
        endTime.add(Calendar.SECOND, getSeconds());
        return endTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TimePeriod other = (TimePeriod) obj;
        return seconds == other.seconds && minutes == other.minutes && hours == other.hours && days == other.days
            && months == other.months && years == other.years;
    }
}
