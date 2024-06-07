package cron.scheduler;

/**
 * Afinal class provides a factory method for obtaining `SchedulingService` instances.
 */
public final class SchedulerFactory {

    /**
     * Retrieves the default implementation of the `SchedulingService`
     * interface. This method typically uses a singleton pattern to
     * ensure a single instance of the scheduler is created.
     *
     * @return the default `SchedulingService` instance
     */
    public static SchedulingService getDefaultScheduler() {
        return Scheduler.getInstance();
    }
}
