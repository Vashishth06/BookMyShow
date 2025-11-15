package MyFirstProject.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the price of a seat type for a specific show.
 *
 * Table: show_seat_type
 *
 * Why needed:
 * Different shows can have different prices for same seat type.
 *
 * Example 1: Time-based pricing
 * Show A (3 PM Matinee): Normal = ₹150, Premium = ₹250
 * Show B (9 PM Prime): Normal = ₹200, Premium = ₹350
 *
 * Example 2: Movie-based pricing
 * "Regular Movie": Normal = ₹150
 * "IMAX 3D Movie": Normal = ₹300
 *
 * This table maps: (Show, SeatType) → Price
 */
@Getter
@Setter
@Entity
public class ShowSeatType extends BaseModel {

    /**
     * The show for which this pricing applies.
     *
     * @ManyToOne: One show can have multiple seat types with different prices
     *
     * Example:
     * Show "Inception 7 PM" has pricing:
     * - ShowSeatType#1: Normal → ₹200
     * - ShowSeatType#2: Premium → ₹350
     * - ShowSeatType#3: VIP → ₹500
     */
    @ManyToOne
    private Show show;

    /**
     * The type of seat (Normal/Premium/VIP).
     *
     * @ManyToOne: One seat type can have different prices for different shows
     *
     * Example:
     * SeatType "Premium" has different prices:
     * - Show "Inception 3 PM": ₹250
     * - Show "Inception 9 PM": ₹350
     * - Show "IMAX Movie": ₹500
     */
    @ManyToOne
    private SeatType seatType;

    /**
     * Price for this seat type in this show.
     * Stored in smallest currency unit (paisa/cents).
     *
     * Example:
     * - ₹350 is stored as: 350
     * - $10.50 is stored as: 1050 (cents)
     *
     * Used by PriceCalculator to compute total booking amount.
     */
    private int price;
}
