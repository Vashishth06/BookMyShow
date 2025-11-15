package MyFirstProject.demo.repositories;

import MyFirstProject.demo.models.Show;
import MyFirstProject.demo.models.ShowSeatType;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Repository interface for ShowSeatType entity database operations.
 *
 * Note: Uses Repository instead of JpaRepository
 * - Repository: Base interface with no predefined methods
 * - JpaRepository: Comes with findAll(), save(), delete(), etc.
 * - Here we only need custom query methods, so Repository is sufficient
 *
 * Purpose:
 * ShowSeatType stores pricing information for different seat types
 * for each show. Same seat type can have different prices for different shows.
 *
 * Example Data:
 * Show: "Inception 7 PM"
 * - Normal seats: ₹200
 * - Premium seats: ₹350
 * - VIP seats: ₹500
 *
 * Show: "Inception 10 PM" (Prime time pricing)
 * - Normal seats: ₹250
 * - Premium seats: ₹400
 * - VIP seats: ₹600
 */
public interface ShowSeatTypeRespository extends Repository<ShowSeatType, Long> {

    /**
     * Retrieves all seat type pricing for a specific show.
     *
     * Use Case:
     * Used by PriceCalculator to determine ticket prices when booking
     *
     * How it works:
     * 1. Takes a Show entity as parameter
     * 2. Finds all ShowSeatType records where show_id matches
     * 3. Returns list of seat types with their prices for this show
     *
     * Spring Data JPA Magic:
     * - Method name "findAllByShow" is parsed automatically
     * - "findAllBy" → SELECT * FROM
     * - "Show" → WHERE show_id = ?
     *
     * SQL Generated:
     * SELECT * FROM show_seat_type WHERE show_id = ?
     *
     * Usage Example:
     * Show show = showRepository.findById(123L).get();
     * List<ShowSeatType> pricing = repository.findAllByShow(show);
     *
     * Result:
     * [
     *   {id: 1, show: Show#123, seatType: "Normal", price: 200},
     *   {id: 2, show: Show#123, seatType: "Premium", price: 350},
     *   {id: 3, show: Show#123, seatType: "VIP", price: 500}
     * ]
     *
     * Performance Note:
     * This query is called once per booking to get all pricing info,
     * then prices are looked up in memory (no additional DB calls needed).
     *
     * @param show The Show entity for which to retrieve pricing
     * @return List of ShowSeatType containing seat types and prices
     */
    List<ShowSeatType> findAllByShow(Show show);

    // Potential future methods:
    // - ShowSeatType findByShowAndSeatType(Show show, SeatType seatType)
    //   → Get price for specific seat type in a show
    // - List<ShowSeatType> findByShowAndPriceGreaterThan(Show show, int price)
    //   → Find premium seats above certain price
}
