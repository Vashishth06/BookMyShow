package MyFirstProject.demo.dtos;

import MyFirstProject.demo.models.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for movie booking responses to clients.
 *
 * Purpose:
 * - Return booking result to client in a structured format
 * - Hide internal implementation details
 * - Provide consistent response structure for success and failure
 * - Allow clients to handle results programmatically
 *
 * Response Philosophy:
 * - Always return a response (never null)
 * - Include status (SUCCESS/FAILURE) so client knows outcome
 * - On success: Include booking details (ID, amount)
 * - On failure: Don't expose technical error details (security)
 *
 * Why not return Booking entity:
 * - Booking has many internal fields (payments, seats, timestamps)
 * - Client only needs essential info (ID and price)
 * - DTO is simpler and more focused
 */
@Getter
@Setter
public class BookMovieResponseDTO {

    /**
     * Total amount to be paid for the booking in smallest currency unit.
     *
     * Example:
     * - If total is ₹900, this field contains 900
     * - If total is $10.50, this field contains 1050 (cents)
     *
     * Best Practice:
     * Always store money as integers (avoid floating point errors)
     *
     * Success scenario: Contains calculated amount
     * Failure scenario: Contains 0 or default value
     */
    private int totalAmount;

    /**
     * Unique identifier of the created booking.
     *
     * Use Cases:
     * - Client stores this ID to track the booking
     * - Used for payment processing
     * - Used to retrieve booking details later
     * - Used for cancellation or modifications
     *
     * Success scenario: Contains generated booking ID
     * Failure scenario: Contains null
     */
    private Long bookingId;

    /**
     * Status of the booking operation.
     *
     * Possible values:
     * - SUCCESS: Booking created successfully, seats blocked
     * - FAILURE: Booking failed (seats unavailable, invalid user, etc.)
     * - PENDING: (Rarely used in response, more for booking entity status)
     *
     * Why enum instead of boolean:
     * - More descriptive than true/false
     * - Extensible (can add more statuses like PARTIAL_SUCCESS)
     * - Type-safe
     *
     * Client should always check this field first before processing response
     */
    private ResponseStatus responseStatus;

    // In production, consider adding:
    // - String errorMessage (for failure cases)
    // - String errorCode (for specific error types)
    // - Date expiryTime (when blocked seats will be released)
}
