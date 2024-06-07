package cron.scheduler;

import cron.job.Schedulable;

/**
 * An interface defines the functionalities provided by a scheduling service.
 */
public interface SchedulingService {

    /**
     * Starts the scheduler service, making it ready to manage and execute
     * scheduled jobs.
     */
    public void start();

    /**
     * Adds a new `Schedulable` object to the scheduler's jobs.
     *
     * @param scheduledJob the `Schedulable` object representing the job to be scheduled
     * @return true if the job was successfully added, false otherwise
     */
    public boolean addScheduledJob(Schedulable scheduledJob);

    /**
     * Removes a previously added `Schedulable` object from the scheduler's jobs.
     *
     * @param scheduledJob the `Schedulable` object representing the job to be removed
     * @return true if the job was successfully removed, false otherwise
     */
    public boolean removeScheduledJob(Schedulable scheduledJob);

    /**
     * Starts the execution of a specific `Schedulable` object immediately.
     *
     * @param scheduledJob the `Schedulable` object representing the job to be started
     * @return true if the job was successfully started, false otherwise
     */
    public boolean startScheduledJob(Schedulable scheduledJob);

    /**
     * Stops the execution of a specific `Schedulable` object.
     *
     * @param scheduledJob the `Schedulable` object representing the job to be stopped
     * @return true if the job was successfully stopped, false otherwise
     */
    public boolean stopScheduledJob(Schedulable scheduledJob);

    /**
     * Starts the execution of all currently scheduled jobs.
     */
    public boolean startAllScheduledJobs();

    /**
     * Stops the execution of all currently scheduled jobs in the scheduler's queue.
     */
    public boolean stopAllScheduledJobs();

    /**
     * Retrieves the total number of currently scheduled jobs in the scheduler's queue.
     */
    public long getNumberOfScheduledJobs();

    /**
     * Retrieves the total number of jobs that have been added to the scheduler.
     */
    public long getNumberOfAddedJobs();

    /**
     * Shuts down the scheduler service, stopping all ongoing and scheduled
     * executions, and releasing any resources associated with it.
     */
    public boolean shutdown();
}

