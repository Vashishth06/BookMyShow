package MyFirstProject.demo.exceptions;

/**
 * Custom exception thrown when user authentication or validation fails.
 *
 * Scenarios where this exception is thrown:
 *
 * 1. During Login:
 *    - User email not found in database
 *    - Password doesn't match stored hash
 *    - Account is locked or deactivated
 *
 * 2. During Booking:
 *    - User ID doesn't exist
 *    - User account is invalid or deleted
 *
 * 3. During Sign-up:
 *    - If existing user logs in with wrong password
 *
 * Security Consideration:
 * Error messages should be generic to prevent user enumeration attacks.
 *
 * DON'T say: "User with email user@example.com not found"
 * DO say: "Invalid credentials. Please try again."
 *
 * This prevents attackers from discovering which emails are registered.
 *
 * Why checked exception:
 * - Forces callers to handle authentication failures
 * - Makes authentication errors explicit in method signatures
 * - Ensures proper error handling in controllers
 */
public class InvalidUserException extends Exception {

    /**
     * Constructor that accepts a custom error message.
     *
     * Message Guidelines:
     * - Internal logs: Detailed (for debugging)
     * - User-facing: Generic (for security)
     *
     * Internal: "User with ID 123 not found in database"
     * User-facing: "Invalid user. Please check your credentials."
     *
     * @param message Descriptive error message
     */
    public InvalidUserException(String message) {
        super(message);
    }

    // Production enhancements:
    // - Add error codes for different failure types
    // - Add userId field (for internal logging only)
    // - Add timestamp of failure (for rate limiting)
    // - Add failureType enum (NOT_FOUND, WRONG_PASSWORD, ACCOUNT_LOCKED)

    // Example:
    // public enum FailureType {
    //     USER_NOT_FOUND,
    //     INVALID_PASSWORD,
    //     ACCOUNT_LOCKED,
    //     EMAIL_NOT_VERIFIED
    // }
    // private FailureType failureType;
}
