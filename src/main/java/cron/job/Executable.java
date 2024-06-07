package cron.job;

/**
 * An interface defines an object that represents an executable task.
 * An `Executable` object combines a name, an identifier, and a
 * `Runnable` object representing the actual work to be done.
 */
public interface Executable extends Runnable {

    /**
     * Retrieves the unique identifier for this executable object.
     *
     * @return the identifier as a String
     */
    public String getId();

    /**
     * Retrieves the name associated with this executable object.
     *
     * @return the name as a String
     */
    public String getName();

    /**
     * Sets the name associated with this executable object.
     *
     * @param name the name to set
     */
    public void setName(String name);

    /**
     * Retrieves the `Runnable` object that encapsulates the actual
     * work to be performed by this executable.
     *
     * @return the `Runnable` object
     */
    public Runnable getFunction();

    /**
     * Sets the `Runnable` object that encapsulates the actual
     * work to be performed by this executable.
     *
     * @param function the `Runnable` object to set
     */
    public void setFunction(Runnable function);

    /**
     * Executes the task associated with this executable object.
     * This method should delegate the execution to the underlying
     * `Runnable` object retrieved by `getFunction()`.
     */
    @Override
    public void run();
}
