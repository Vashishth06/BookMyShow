package MyFirstProject.demo.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Data Transfer Object for movie booking requests from clients.
 *
 * Purpose of DTOs:
 * - Decouple external API contract from internal domain models
 * - Control what data is exposed to clients
 * - Allow different request/response structures
 * - Facilitate API versioning without changing entities
 *
 * Why not use Entity directly:
 * - Entities have database-specific fields (createdAt, updatedAt)
 * - Entities expose internal relationships
 * - DTOs are simpler and focused on specific use cases
 * - Better security (don't expose all entity fields)
 *
 * This DTO contains the minimum information needed to book tickets:
 * - Who is booking? (userId)
 * - Which show? (showId)
 * - Which seats? (showsSeatId)
 */
@Getter  // Lombok: Auto-generates getter methods
@Setter  // Lombok: Auto-generates setter methods
public class BookMovieRequestDTO {

    /**
     * ID of the user making the booking.
     *
     * Validation needed (in production):
     * - @NotNull: User ID must be provided
     * - @Positive: Must be a positive number
     *
     * Security consideration:
     * In real application, userId should come from authenticated session
     * rather than user input (prevents booking as someone else)
     */
    private Long userId;

    /**
     * ID of the show for which tickets are being booked.
     *
     * A show represents a specific screening:
     * Example: "Inception at 7:00 PM on Oct 11, 2025 in Screen 1"
     *
     * Validation needed (in production):
     * - @NotNull: Show ID must be provided
     * - @Positive: Must be a positive number
     */
    private Long showId;

    /**
     * List of ShowSeat IDs the user wants to book.
     *
     * Why List<Long> and not List<ShowSeat>:
     * - Client only needs to send IDs, not full objects
     * - Server fetches full ShowSeat objects from database
     * - More efficient (less data transferred over network)
     *
     * Validation needed (in production):
     * - @NotEmpty: At least one seat must be selected
     * - @Size(min=1, max=10): Limit seats per booking
     *
     * Example: [1001, 1002, 1003] for seats A1, A2, A3
     */
    private List<Long> showsSeatId;
}