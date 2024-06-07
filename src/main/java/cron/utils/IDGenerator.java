package cron.utils;

import java.util.UUID;

/**
 * A class provides a static method to generate unique identifiers (IDs).
 * It utilizes the `UUID.randomUUID().toString()` method to generate a Universally
 * Unique Identifier (UUID) and returns it as a String.
 */
public final class IDGenerator {

    /**
     * Generates a new unique identifier string.
     *
     * @return a String representing the generated unique identifier
     */
    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}
