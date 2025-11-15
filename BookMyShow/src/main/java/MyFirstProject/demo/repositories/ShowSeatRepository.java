package MyFirstProject.demo.repositories;

import MyFirstProject.demo.models.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for ShowSeat entity database operations.
 *
 * CRITICAL for Concurrency Control:
 * This repository manages ShowSeat entities which are central to preventing
 * double-booking. The seat status updates here are protected by database
 * transactions with SERIALIZABLE isolation level.
 *
 * ShowSeat represents:
 * - A specific physical seat's status for a specific show
 * - Example: Seat A1 for "Inception 7 PM show" is AVAILABLE
 * - Example: Seat A1 for "Inception 10 PM show" is OCCUPIED
 *
 * Why ShowSeat is separate from Seat:
 * - Same physical seat has different status for different shows
 * - Seat A1 (physical) → Many ShowSeats (one per show)
 */
@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    /**
     * Retrieves multiple ShowSeats by their IDs in a single query.
     *
     * Why this method is important:
     * - Efficient bulk fetching (one query instead of N queries)
     * - Used when user books multiple seats at once
     * - Reduces database round trips
     *
     * How it works:
     * - Takes a list/collection of IDs
     * - Returns all ShowSeats matching those IDs
     * - If some IDs don't exist, they're simply not in the result
     *
     * SQL Generated:
     * SELECT * FROM show_seat WHERE id IN (?, ?, ?)
     *
     * Usage Example:
     * List<Long> seatIds = Arrays.asList(101L, 102L, 103L);
     * List<ShowSeat> seats = showSeatRepository.findAllById(seatIds);
     * // Returns ShowSeats with IDs 101, 102, and 103
     *
     * Concurrency Note:
     * When called within a SERIALIZABLE transaction, this query locks
     * the returned rows, preventing other transactions from modifying them.
     *
     * @param showSeatList Iterable collection of ShowSeat IDs to fetch
     * @return List of ShowSeat entities matching the provided IDs
     */
    @Override
    List<ShowSeat> findAllById(Iterable<Long> showSeatList);

    /**
     * Saves or updates a ShowSeat entity.
     *
     * Critical for seat booking:
     * - Updates seat status from AVAILABLE → BLOCKED
     * - Updates seat status from BLOCKED → OCCUPIED (after payment)
     *
     * Transaction Safety:
     * When called inside @Transactional(SERIALIZABLE):
     * - Changes are not visible to other transactions until commit
     * - If transaction fails, changes are rolled back
     * - Prevents dirty reads and lost updates
     *
     * Typical Flow:
     * 1. Fetch ShowSeat (status: AVAILABLE)
     * 2. Check if available
     * 3. Set status to BLOCKED
     * 4. Call save() → Persists change
     * 5. Transaction commits → Change becomes permanent
     *
     * SQL Generated:
     * UPDATE show_seat
     * SET seat_id=?, show_id=?, seat_status=?, updated_at=?
     * WHERE id=?
     *
     * @param showSeat ShowSeat entity to save/update
     * @return Updated ShowSeat entity with timestamp changes
     */
    @Override
    ShowSeat save(ShowSeat showSeat);

    // Potential future methods:
    // - List<ShowSeat> findByShowIdAndSeatStatus(Long showId, SeatStatus status)
    //   → Find all available/blocked/occupied seats for a show
    // - int countByShowIdAndSeatStatus(Long showId, SeatStatus status)
    //   → Count available seats for a show
}
