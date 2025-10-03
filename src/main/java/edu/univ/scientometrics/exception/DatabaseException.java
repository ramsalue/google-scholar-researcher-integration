package edu.univ.scientometrics.exception;

/**
 * Custom exception for database-related errors.
 *
 * Thrown when database operations fail (connection errors,
 * constraint violations, transaction failures, etc.)
 *
 * SOLID Principles:
 * - Single Responsibility: Only represents database errors
 *
 * @author Sprint 3 Team
 * @since 1.0
 */
public class DatabaseException extends RuntimeException {

    /**
     * Constructs exception with message.
     *
     * @param message Error description
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Constructs exception with message and cause.
     *
     * @param message Error description
     * @param cause Original exception
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}