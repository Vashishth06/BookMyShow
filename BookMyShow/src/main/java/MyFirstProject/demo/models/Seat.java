package MyFirstProject.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a physical seat in a theater screen.
 *
 * Table: seat
 *
 * Example: Seat A15 in Screen 1 (row A, column 15, Premium type)
 *
 * This is the permanent seat configuration - doesn't change per show.
 * The same physical seat is used across all shows in that screen.
 *
 * Seat vs ShowSeat:
 * - Seat: Physical seat (A1) exists permanently
 * - ShowSeat: A1's status for a specific show (available/occupied)
 */
@Getter
@Setter
@Entity
public class Seat extends BaseModel {

    /**
     * Seat identifier (e.g., "A1", "B15", "C7").
     *
     * Naming conventions:
     * - Row letter + Seat number: A1, A2, B1, B2
     * - Full name: "Row A Seat 1"
     *
     * Used for displaying seat layout to users
     */
    private String name;

    /**
     * Type of seat (Normal, Premium, VIP).
     * Different types have different prices.
     *
     * @ManyToOne: Many seats can be of the same type
     *
     * Example:
     * SeatType "Premium" is used by:
     * - Seat A1, A2, A3 (first row)
     * - Seat B1, B2, B3 (second row)
     */
    @ManyToOne
    private SeatType seatType;

    /**
     * Row number in the screen layout.
     *
     * Used for:
     * - Displaying seat map to users
     * - Calculating seat position
     * - Determining seat quality (front rows vs back rows)
     *
     * Example:
     * Row 0: A (front)
     * Row 1: B
     * Row 2: C (back)
     */
    private int rowNum;

    /**
     * Column number in the screen layout.
     *
     * Used for:
     * - Seat positioning in the grid
     * - Left/center/right classification
     * - UI rendering
     *
     * Example:
     * Col 0: Left side
     * Col 5: Center
     * Col 10: Right side
     */
    private int colNum;
}
