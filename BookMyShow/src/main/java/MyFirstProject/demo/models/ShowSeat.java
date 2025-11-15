package MyFirstProject.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a seat's availability status for a specific show.
 *
 * ⭐ CRITICAL FOR CONCURRENCY CONTROL ⭐
 * This is the entity that gets locked during concurrent booking requests!
 *
 * Table: show_seat
 *
 * Why separate from Seat:
 * - Seat: Physical seat in the theater (A1, A2, B1, etc.)
 * - ShowSeat: That seat's status for a specific show (available/blocked/occupied)
 *
 * Example:
 * - Physical Seat A1 exists in Screen 1
 * - ShowSeat for "Inception 7 PM": A1 is AVAILABLE
 * - ShowSeat for "Inception 10 PM": A1 is OCCUPIED (someone booked it)
 * - Same physical seat, different status for different shows
 *
 * Concurrency Scenario:
 * When two users try to book the same seat simultaneously:
 * 1. Transaction isolation locks this ShowSeat row
 * 2. First user checks status → AVAILABLE
 * 3. First user changes status → BLOCKED
 * 4. Second user waits for lock
 * 5. Second user checks status → BLOCKED (sees updated value)
 * 6. Second user gets exception
 */
@Getter
@Setter
@Entity
public class ShowSeat extends BaseModel {

    /**
     * The physical seat in the theater.
     *
     * @ManyToOne: Many ShowSeats can reference the same physical Seat (for different shows)
     *
     * Example:
     * Physical Seat A1 is referenced by:
     * - ShowSeat#1 (Show "Inception 7 PM")
     * - ShowSeat#2 (Show "Inception 10 PM")
     * - ShowSeat#3 (Show "Interstellar 3 PM")
     */
    @ManyToOne
    private Seat seat;

    /**
     * The show for which this seat status applies.
     *
     * @ManyToOne: Many ShowSeats belong to one show
     *
     * Example:
     * Show "Inception 7 PM" has ShowSeats:
     * - ShowSeat#1: A1, AVAILABLE
     * - ShowSeat#2: A2, BLOCKED
     * - ShowSeat#3: A3, OCCUPIED
     */
    @ManyToOne
    private Show show;

    /**
     * Current status of this seat for this show.
     *
     * ⭐ CRITICAL FIELD FOR CONCURRENCY ⭐
     *
     * Possible values:
     * - AVAILABLE (1): Can be booked
     * - BLOCKED (2): Temporarily blocked (booking in progress, payment pending)
     * - OCCUPIED (0): Booked and paid for
     *
     * Status Transitions:
     * AVAILABLE → BLOCKED (user starts booking)
     * BLOCKED → OCCUPIED (payment successful)
     * BLOCKED → AVAILABLE (payment failed or timeout)
     *
     * Concurrency Protection:
     * When BookingServices.bookMovie() runs with SERIALIZABLE isolation:
     * 1. Reads this field (acquires read lock)
     * 2. Checks if AVAILABLE
     * 3. Updates to BLOCKED (acquires write lock)
     * 4. No other transaction can modify until commit
     */
    @Enumerated(EnumType.ORDINAL)
    private SeatStatus seatStatus;
}
