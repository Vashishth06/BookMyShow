package MyFirstProject.demo.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a category of seats with specific pricing.
 *
 * Table: seat_type
 *
 * Examples:
 * - Normal: Basic seats, lowest price (back rows)
 * - Premium: Better view, mid-range price (middle rows)
 * - VIP/Recliner: Best seats, highest price (front rows, recliners)
 *
 * Purpose:
 * Different seat types allow dynamic pricing based on seat quality.
 * Same show can have different prices for different seat types.
 */
@Getter
@Setter
@Entity
public class SeatType extends BaseModel {

    /**
     * Name of the seat type (e.g., "Normal", "Premium", "VIP").
     *
     * Common types:
     * - "Normal" / "Regular"
     * - "Premium" / "Executive"
     * - "VIP" / "Recliner" / "Sofa"
     * - "Couple Seat" / "Wheelchair"
     *
     * Used in:
     * - Seat selection UI
     * - Price calculation
     * - Show seat type pricing
     */
    private String name;
}
