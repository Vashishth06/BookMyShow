package MyFirstProject.demo.exceptions;


/**
 * Custom exception thrown when a show/screening does not exist or is invalid.
 *
 * When this exception is thrown:
 * - User tries to book tickets for a show ID that doesn't exist in database
 * - Show has been cancelled or removed
 * - Show ID format is invalid
 *
 * Why custom exceptions:
 * - More descriptive than generic Exception
 * - Allows specific error handling in catch blocks
 * - Makes code more readable and maintainable
 * - Can add custom fields/methods specific to this error type
 *
 * Usage Example:
 * Optional<Show> optionalShow = showRepository.findById(showId);
 * if (optionalShow.isEmpty()) {
 *     throw new InvalidShowException("Show not found with ID: " + showId);
 * }
 *
 * Exception Hierarchy:
 * Exception (checked) → Must be declared in method signature
 * RuntimeException (unchecked) → Optional to declare
 *
 * This uses Exception (checked), so methods must declare:
 * public void bookMovie(...) throws InvalidShowException
 */
public class InvalidShowException extends Exception {

    /**
     * Constructor that accepts a custom error message.
     * <p>
     * The message parameter allows providing context-specific information
     * about why the show is invalid, making debugging easier.
     * <p>
     * Best Practices for error messages:
     * - Be specific: "Show with ID 123 not found"
     * - Don't expose sensitive data: Avoid internal IDs in production
     * - User-friendly: "The selected show is no longer available"
     * - Actionable: "Please select a different show"
     *
     * @param message Descriptive error message explaining what went wrong
     */
    public InvalidShowException(String message) {
        super(message);  // Pass message to parent Exception class
    }

    // Possible enhancements:
    // - Add showId field to store the invalid ID
    // - Add constructor with Throwable cause for exception chaining
    // - Add custom error code for API responses
}
