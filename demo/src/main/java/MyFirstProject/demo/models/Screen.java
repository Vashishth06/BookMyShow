package MyFirstProject.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents a movie screen/hall/auditorium in a theater.
 *
 * Table: screen
 *
 * Example: Screen 1 in PVR Rajkot with 200 seats, IMAX capability
 *
 * A theater can have multiple screens (Screen 1, Screen 2, etc.).
 * Each screen has its own seating configuration and features.
 */
@Getter
@Setter
@Entity
public class Screen extends BaseModel {

    /**
     * Screen identifier (e.g., "Screen 1", "Audi 2", "Hall A").
     *
     * Naming conventions:
     * - "Screen 1", "Screen 2"
     * - "Audi 1", "Audi 2"
     * - "IMAX Hall", "Platinum Screen"
     */
    private String name;

    /**
     * All seats in this screen.
     *
     * @OneToMany: One screen has many seats
     *
     * Example:
     * Screen 1 has seats:
     * - A1, A2, A3, ..., A20 (Row A)
     * - B1, B2, B3, ..., B20 (Row B)
     * - C1, C2, C3, ..., C20 (Row C)
     * Total: 60 seats
     *
     * Seat configuration is permanent per screen.
     */
    @OneToMany
    private List<Seat> seats;

    /**
     * Special features of this screen.
     *
     * @ElementCollection: Stores enum list as separate table
     * @Enumerated(ORDINAL): Stores as integers
     *
     * Examples of features:
     * - TWO_D: Standard 2D projection
     * - THREE_D: 3D movie capability
     * - IMAX: IMAX screen with larger format
     * - Dolby Atmos: Advanced sound system
     * - 4DX: Motion seats with environmental effects
     *
     * Usage:
     * Can filter shows by screen features:
     * "Show me all IMAX shows for Inception"
     */
    @Enumerated(EnumType.ORDINAL)
    @ElementCollection
    private List<ScreenFeatures> screenFeatures;
}
