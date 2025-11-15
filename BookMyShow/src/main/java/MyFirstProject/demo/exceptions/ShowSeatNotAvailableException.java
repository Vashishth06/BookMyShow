package MyFirstProject.demo.exceptions;

/**
 * Custom exception thrown when user tries to book seats that are not available.
 *
 * This is the MOST COMMON exception in concurrent booking scenarios.
 *
 * When this exception is thrown:
 *
 * 1. Seat Already Booked:
 *    - Another user completed booking for the same seat
 *    - Seat status is OCCUPIED
 *
 * 2. Seat Temporarily Blocked:
 *    - Another user is currently booking the same seat
 *    - Seat status is BLOCKED (booking in progress)
 *    - Will become AVAILABLE if that booking fails/times out
 *
 * 3. Concurrent Booking Conflict:
 *    - Two users tried to book same seat simultaneously
 *    - Transaction isolation ensures only one succeeds
 *    - The other gets this exception
 *
 * Concurrency Scenario:
 * Time  | User A                    | User B
 * ------|---------------------------|---------------------------
 * T1    | Check Seat A1 → AVAILABLE |
 * T2    |                           | Check Seat A1 → AVAILABLE
 * T3    | Mark A1 → BLOCKED ✓       |
 * T4    |                           | Try to mark A1 → BLOCKED ✗
 * T5    |                           | Throw ShowSeatNotAvailableException
 *
 * Why this exception is critical:
 * - Prevents double-booking
 * - Ensures data integrity
 * - Provides clear feedback to users
 * - Allows retry logic in UI
 *
 * Checked exception forces:
 * - Controllers to handle unavailability gracefully
 * - Services to declare this can happen
 * - Proper user feedback ("Seat taken, select another")
 */
public class ShowSeatNotAvailableException extends Exception {

    /**
     * Constructor that accepts a custom error message.
     * <p>
     * Message should indicate:
     * - Which seat(s) are unavailable
     * - Why they're unavailable (if known)
     * - What user should do next
     * <p>
     * Good Messages:
     * - "Seat A5 is no longer available. Please select another seat."
     * - "One or more selected seats are already booked."
     * - "Seats [A1, A2] are currently being booked by another user."
     * <p>
     * Bad Messages:
     * - "Error" (too vague)
     * - "Database constraint violation" (too technical)
     * - "Transaction rolled back" (internal detail)
     *
     * @param message User-friendly error message
     */
    public ShowSeatNotAvailableException(String message) {
        super(message);
    }

// Production enhancements:
// - Add list of unavailable seat IDs
// - Add list of alternative available seats nearby
// - Add reason code (OCCUPIED, BLOCKED, RESERVED)
// - Add retry-after timestamp (when BLOCKED seats might be free)
}
