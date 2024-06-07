package cron.scheduler;

import cron.job.Executable;
import cron.job.Job;
import cron.job.Schedulable;
import cron.job.ScheduledJob;
import cron.time.TimePeriod;
import cron.trigger.SchedulingTrigger;
import cron.trigger.Trigger;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

public class SchedulerTest {

    @Test(expected = IllegalArgumentException.class)
    public void testAddScheduledJobNull() {
        SchedulingService scheduler = getSchedulerNewInstance();
        scheduler.start();
        scheduler.addScheduledJob(null);
    }

    @Test
    public void testStartAlreadyRunningScheduledJob() {
        SchedulingService scheduler = getSchedulerNewInstance();
        scheduler.start();
        Executable mockJob = Mockito.mock(Executable.class);
        Trigger trigger
                = new Trigger(new TimePeriod(1, 0, 0, 0, 0, 0),
                              new TimePeriod(2, 0, 0, 0, 0, 0));
        ScheduledJob scheduledJob = new ScheduledJob(mockJob, trigger);
        scheduler.addScheduledJob(scheduledJob);
        scheduler.startScheduledJob(scheduledJob);
        Assert.assertFalse(scheduler.startScheduledJob(scheduledJob));
    }

    @Test
    public void testAddScheduledJobSuccess() {
        SchedulingService scheduler = getSchedulerNewInstance();
        scheduler.start();
        Executable mockJob = Mockito.mock(Executable.class);
        Trigger trigger
                = new Trigger(new TimePeriod(1, 0, 0, 0, 0, 0),
                              new TimePeriod(2, 0, 0, 0, 0, 0));
        ScheduledJob scheduledJob = new ScheduledJob(mockJob, trigger);
        Assert.assertTrue(scheduler.addScheduledJob(scheduledJob));
    }

    @Test
    public void testStartScheduledJobSuccess() {
        SchedulingService scheduler = getSchedulerNewInstance();
        scheduler.start();
        Executable mockJob = Mockito.mock(Executable.class);
        Trigger trigger
                = new Trigger(new TimePeriod(1, 0, 0, 0, 0, 0),
                              new TimePeriod(2, 0, 0, 0, 0, 0));
        ScheduledJob scheduledJob = new ScheduledJob(mockJob, trigger);
        scheduler.addScheduledJob(scheduledJob);
        Assert.assertTrue(scheduler.startScheduledJob(scheduledJob));
    }

    @Test
    public void testStopScheduledJobSuccess() {
        SchedulingService scheduler = getSchedulerNewInstance();
        scheduler.start();
        Executable mockJob = Mockito.mock(Executable.class);
        Trigger trigger
                = new Trigger(new TimePeriod(1, 0, 0, 0, 0, 0),
                              new TimePeriod(2, 0, 0, 0, 0, 0));
        ScheduledJob scheduledJob = new ScheduledJob(mockJob, trigger);
        scheduler.addScheduledJob(scheduledJob);
        scheduler.startScheduledJob(scheduledJob);
        Assert.assertTrue(scheduler.stopScheduledJob(scheduledJob));
    }

    @Test
    public void testStopNonExistentScheduledJob() {
        SchedulingService scheduler = getSchedulerNewInstance();
        scheduler.start();
        Schedulable mockJob
                = new ScheduledJob(Mockito.mock(Executable.class), Mockito.mock(SchedulingTrigger.class));
        Assert.assertFalse(Scheduler.getInstance().stopScheduledJob(mockJob));
    }

    @Test
    public void testStartMultipleJobs() {
        SchedulingService scheduler = getSchedulerNewInstance();
        scheduler.start();
        Executable mockJob1 = Mockito.mock(Executable.class);
        Executable mockJob2 = Mockito.mock(Executable.class);
        Trigger trigger1
                = new Trigger(new TimePeriod(1, 0, 0, 0, 0, 0),
                new TimePeriod(1, 0, 0, 0, 0, 0));
        Trigger trigger2 = new Trigger(new TimePeriod(2, 0, 0, 0, 0, 0),
                new TimePeriod(1, 0, 0, 0, 0, 0));
        ScheduledJob scheduledJob1 = new ScheduledJob(mockJob1, trigger1);
        ScheduledJob scheduledJob2 = new ScheduledJob(mockJob2, trigger2);
        scheduler.addScheduledJob(scheduledJob1);
        scheduler.addScheduledJob(scheduledJob2);
        Assert.assertTrue(scheduler.startScheduledJob(scheduledJob1));
        Assert.assertTrue(scheduler.startScheduledJob(scheduledJob2));
    }

    @Test
    public void testSchedulerShutdown() {
        SchedulingService scheduler = getSchedulerNewInstance();
        scheduler.start();
        Executable mockJob = Mockito.mock(Executable.class);
        Trigger trigger
                = new Trigger(new TimePeriod(1, 0, 0, 0, 0, 0),
                new TimePeriod(2, 0, 0, 0, 0, 0));
        ScheduledJob scheduledJob = new ScheduledJob(mockJob, trigger);
        scheduler.addScheduledJob(scheduledJob);
        scheduler.startScheduledJob(scheduledJob);
        scheduler.shutdown();
        Assert.assertEquals(0, scheduler.getNumberOfScheduledJobs());
    }

    @Test
    public void testVerifyJobExecution() throws InterruptedException {
        SchedulingService scheduler = getSchedulerNewInstance();
        scheduler.start();
        Executable mockJob = Mockito.mock(Executable.class);
        Trigger trigger
                = new Trigger(new TimePeriod(1, 0, 0, 0, 0, 0),
                              new TimePeriod(1, 0, 0, 0, 0, 0));
        ScheduledJob scheduledJob = new ScheduledJob(mockJob, trigger);
        scheduler.addScheduledJob(scheduledJob);
        scheduler.startScheduledJob(scheduledJob);
        TimeUnit.SECONDS.sleep(2);
        Mockito.verify(mockJob, Mockito.times(2)).run();
    }

    @Test
    public void testMultipleJobsExecution() throws InterruptedException {
        SchedulingService scheduler = getSchedulerNewInstance();
        Executable job1 = new Job("Job1" , () -> {
            System.out.println("Executing job 1");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Executable job2 = new Job("Job2" , () -> {
            System.out.println("Executing job 2");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Trigger trigger1
                = new Trigger(new TimePeriod(0, 0, 0, 0, 0, 0),
                              new TimePeriod(1, 0, 0, 0, 0, 0));
        Trigger trigger2
                = new Trigger(new TimePeriod(0, 0, 0, 0, 0, 0),
                              new TimePeriod(1, 0, 0, 0, 0, 0));
        ScheduledJob scheduledJob1 = new ScheduledJob(job1, trigger1);
        ScheduledJob scheduledJob2 = new ScheduledJob(job2, trigger2);
        Assert.assertTrue(scheduler.addScheduledJob(scheduledJob1));
        Assert.assertTrue(scheduler.addScheduledJob(scheduledJob2));
        scheduler.start();
        Assert.assertTrue(scheduler.startAllScheduledJobs());
        Thread.sleep(5000);
        Assert.assertEquals(5, scheduledJob1.getNumberOfCompletedExecutions());
        Assert.assertEquals(5, scheduledJob2.getNumberOfCompletedExecutions());
        Assert.assertTrue(scheduler.shutdown());
    }

    @Test
    public void testMultipleJobsExecutionWithStartDelay() throws InterruptedException {
        SchedulingService scheduler = getSchedulerNewInstance();
        Executable job1 = new Job("Job1" , () -> {
            System.out.println("Executing job 1");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Executable job2 = new Job("Job2" , () -> {
            System.out.println("Executing job 2");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Trigger trigger1
                = new Trigger(new TimePeriod(5, 0, 0, 0, 0, 0),
                new TimePeriod(1, 0, 0, 0, 0, 0));
        Trigger trigger2
                = new Trigger(new TimePeriod(5, 0, 0, 0, 0, 0),
                new TimePeriod(1, 0, 0, 0, 0, 0));
        ScheduledJob scheduledJob1 = new ScheduledJob(job1, trigger1);
        ScheduledJob scheduledJob2 = new ScheduledJob(job2, trigger2);
        Assert.assertTrue(scheduler.addScheduledJob(scheduledJob1));
        Assert.assertTrue(scheduler.addScheduledJob(scheduledJob2));
        Assert.assertEquals(2, scheduler.getNumberOfAddedJobs());
        scheduler.start();
        Assert.assertTrue(scheduler.startAllScheduledJobs());
        Assert.assertEquals(2, scheduler.getNumberOfScheduledJobs());
        Thread.sleep(10000);
        Assert.assertEquals(5, scheduledJob1.getNumberOfCompletedExecutions());
        Assert.assertEquals(5, scheduledJob2.getNumberOfCompletedExecutions());
        Assert.assertTrue(scheduler.shutdown());
    }

    @Test
    public void testChangeScheduledJobTrigger() throws InterruptedException {
        SchedulingService scheduler = getSchedulerNewInstance();
        Executable job1 = new Job("Job1" , () -> {
            System.out.println("Executing job 1");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Executable job2 = new Job("Job2" , () -> {
            System.out.println("Executing job 2");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Trigger trigger1
                = new Trigger(new TimePeriod(0, 0, 0, 0, 0, 0),
                new TimePeriod(1, 0, 0, 0, 0, 0));
        Trigger trigger2
                = new Trigger(new TimePeriod(5, 0, 0, 0, 0, 0),
                new TimePeriod(5, 0, 0, 0, 0, 0));
        ScheduledJob scheduledJob1 = new ScheduledJob(job1, trigger1);

        Assert.assertTrue(scheduler.addScheduledJob(scheduledJob1));
        Assert.assertEquals(1, scheduler.getNumberOfAddedJobs());
        scheduler.start();
        Assert.assertTrue(scheduler.startAllScheduledJobs());
        Assert.assertEquals(1, scheduler.getNumberOfScheduledJobs());
        Thread.sleep(5000);
        long numberOfExecutionsWithFirstTrigger = scheduledJob1.getNumberOfCompletedExecutions();
        Assert.assertEquals(5, numberOfExecutionsWithFirstTrigger);
        scheduledJob1.setTrigger(trigger2);
        Thread.sleep(6000);
        Assert.assertEquals(2, scheduledJob1.getNumberOfCompletedExecutions()
                - numberOfExecutionsWithFirstTrigger);
        Assert.assertTrue(scheduler.shutdown());
    }

    @Test
    public void testScheduledJobOneTimeExecution() throws InterruptedException {
        SchedulingService scheduler = getSchedulerNewInstance();
        scheduler.start();
        Executable mockJob1 = Mockito.mock(Executable.class);
        Executable mockJob2 = Mockito.mock(Executable.class);
        Trigger trigger1
                = new Trigger(new TimePeriod(1, 0, 0, 0, 0, 0),
                              new TimePeriod(0, 0, 0, 0, 0, 0));
        Trigger trigger2
                = new Trigger(new TimePeriod(1, 0, 0, 0, 0, 0), null);
        ScheduledJob scheduledJob1 = new ScheduledJob(mockJob1, trigger1);
        ScheduledJob scheduledJob2 = new ScheduledJob(mockJob2, trigger2);
        scheduler.addScheduledJob(scheduledJob1);
        scheduler.addScheduledJob(scheduledJob2);
        scheduler.startScheduledJob(scheduledJob1);
        scheduler.startScheduledJob(scheduledJob2);
        TimeUnit.SECONDS.sleep(3);
        Mockito.verify(mockJob1, Mockito.times(1)).run();
        Mockito.verify(mockJob2, Mockito.times(1)).run();
    }

    private SchedulingService getSchedulerNewInstance() {
        Field instance = null;
        try {
            instance = Scheduler.class.getDeclaredField("instance");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        instance.setAccessible(true);
        try {
            instance.set(null, null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return SchedulerFactory.getDefaultScheduler();
    }
}