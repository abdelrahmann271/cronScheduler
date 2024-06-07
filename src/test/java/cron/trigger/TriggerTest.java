package cron.trigger;

import cron.job.Job;
import cron.job.ScheduledJob;
import cron.time.TimePeriod;
import org.junit.Assert;
import org.junit.Test;

public class TriggerTest {
    @Test
    public void testTriggerIsScheduledNow() throws InterruptedException {
        Job job = new Job("Job", () -> {
            System.out.println("Job Execution");
        });
        TimePeriod startInterval = new TimePeriod(0,0,0,0,0,0);
        TimePeriod timePeriod = new TimePeriod(1,0,0,0,0,0);
        Trigger trigger = new Trigger(startInterval, timePeriod);
        ScheduledJob scheduledJob = new ScheduledJob(job, trigger);
        Thread.sleep(1000);
        Assert.assertTrue(scheduledJob.getIsScheduledNow());
    }

    @Test
    public void testTriggerIsScheduledNowWithStartDelay() throws InterruptedException {
        Job job = new Job("Job", () -> {
            System.out.println("Job Execution");
        });
        TimePeriod startInterval = new TimePeriod(2,0,0,0,0,0);
        TimePeriod timePeriod = new TimePeriod(1,0,0,0,0,0);
        Trigger trigger = new Trigger(startInterval, timePeriod);
        ScheduledJob scheduledJob = new ScheduledJob(job, trigger);
        Thread.sleep(3000);
        Assert.assertTrue(scheduledJob.getIsScheduledNow());
    }
}
