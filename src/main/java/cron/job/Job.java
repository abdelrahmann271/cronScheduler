package cron.job;

import cron.utils.IDGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A class represents a job that encapsulates a name, identifier,
 * and the actual task to be executed. It implements both the `Runnable` and
 * `Executable` interfaces.
 */
public final class Job implements Runnable, Executable {

    private static final Logger logger = LogManager.getLogger(Job.class);
    private final String id;
    private String name;
    private Runnable function;

    /**
     * Creates a new `Job` object with the specified name and the task to be executed.
     *
     * @param name the name of the job (must not be null or empty)
     * @param function the `Runnable` object representing the task to be executed (must not be null)
     * @throws IllegalArgumentException if either name or function is null or empty
     */
    public Job(String name, Runnable function) {
        if (name.isEmpty() || name == null || function == null) {
            throw new IllegalArgumentException();
        }
        this.id = IDGenerator.generateId();
        this.name = name;
        this.function = function;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public void setName(String name) {
        if (name == null)
            throw new IllegalArgumentException();
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Runnable getFunction() {
        return function;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFunction(Runnable function) {
        if (function == null)
            throw new IllegalArgumentException();
        this.function = function;
    }

    /**
     * {@inheritDoc}
     *
     * This implementation executes the job function and logs the execution time.
     * In case of exceptions, it logs the error and throws a wrapped RuntimeException.
     */
    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            function.run();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            logger.info("Job with name (" + getName()  + ") and ID (" + getId() + ") executed in (" + duration + ") ms.");
        }
        catch (Exception e) {
            String errMsg = "Error while executing function. " + e;
            logger.error(errMsg);
            throw new RuntimeException(errMsg, e);
        }
    }
}
