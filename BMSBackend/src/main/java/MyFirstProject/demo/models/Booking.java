package MyFirstProject.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

/**
 * Represents a movie ticket booking in the system.
 * This is the core transactional entity that ties together users, shows, seats, and payments.
 *
 * Table: booking
 *
 * Relationships:
 * - Each booking belongs to one User (who made the booking)
 * - Each booking is for one Show (specific movie screening)
 * - Each booking can have multiple ShowSeats (user can book multiple seats)
 * - Each booking can have multiple Payments (partial payments, retries)
 *
 * Booking Lifecycle:
 * 1. PENDING: Seats blocked, awaiting payment
 * 2. CONFIRMED: Payment successful, tickets issued
 * 3. CANCELLED: User cancelled or payment failed
 */
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Booking extends BaseModel {

    /**
     * The show for which tickets are booked.
     *
     * @ManyToOne: Many bookings can be for one show
     * Example: "Inception 7 PM" show can have 200 bookings
     *
     * Fetch type LAZY by default: Show details loaded only when accessed
     * booking.getShow() → triggers database query
     */
    @ManyToOne
    private Show show;

    /**
     * List of seats booked in this booking.
     *
     * @ManyToMany: One booking can have multiple seats, one seat can be in multiple bookings (different shows)
     *
     * Why ManyToMany:
     * - One booking → Multiple seats (A1, A2, A3)
     * - One ShowSeat can appear in different bookings for different shows
     *
     * Example:
     * Booking#1: [ShowSeat#101 (A1), ShowSeat#102 (A2)]
     * Booking#2: [ShowSeat#103 (B1), ShowSeat#104 (B2), ShowSeat#105 (B3)]
     */
    @ManyToMany
    private List<ShowSeat> seats;

    /**
     * Payment transactions associated with this booking.
     *
     * @OneToMany: One booking can have multiple payment attempts/transactions
     *
     * Why multiple payments:
     * - Initial payment attempt
     * - Retry after failure
     * - Partial payments (pay now, pay later)
     * - Refunds (negative transactions)
     *
     * Example:
     * Booking#1 payments:
     * - Payment#1: ₹500, Status: FAILED
     * - Payment#2: ₹500, Status: SUCCESS
     */
    @OneToMany
    private List<Payment> payments;

    /**
     * User who made this booking.
     *
     * @ManyToOne: Many bookings can belong to one user
     * @CreatedBy: JPA auditing can automatically set this to logged-in user
     *
     * Example: User "john@example.com" has 10 bookings
     */
    @ManyToOne
    @CreatedBy
    private User user;

    /**
     * Current status of the booking.
     *
     * @Enumerated(ORDINAL): Stores enum as integer (0, 1, 2...)
     *
     * Status Flow:
     * PENDING (0) → User books seats, awaiting payment
     *   ↓
     * CONFIRMED (1) → Payment successful
     *   OR
     * CANCELLED (2) → Payment failed or user cancelled
     *
     * ORDINAL vs STRING:
     * - ORDINAL: Saves space, faster, but breaks if enum order changes
     * - STRING: More readable in DB, safer for enum changes
     */
    @Enumerated(EnumType.ORDINAL)
    private BookingStatus bookingStatus;

    /**
     * Total price for this booking (sum of all seat prices).
     * Stored in smallest currency unit (paisa/cents).
     *
     * Example:
     * - 2 Normal seats (₹200 each) + 1 VIP seat (₹500) = ₹900
     * - Stored as: 900
     *
     * Why int and not double:
     * - Avoids floating point precision errors
     * - Money should never use float/double
     */
    private int price;

    /**
     * Timestamp when booking was made.
     *
     * Business timestamp (different from createdAt):
     * - createdAt: When record was created in DB (technical)
     * - timeOfBooking: When user made the booking (business logic)
     *
     * Used for:
     * - Showing booking history to user
     * - Expiring bookings (auto-cancel after X minutes)
     * - Analytics and reporting
     */
    private Date timeOfBooking;
}