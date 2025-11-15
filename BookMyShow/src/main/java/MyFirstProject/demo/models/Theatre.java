package MyFirstProject.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents a movie theater/cinema hall/multiplex.
 *
 * Table: theatre
 *
 * Example: PVR Rajkot, INOX Mumbai, Cinepolis Delhi
 *
 * A theater is a physical building containing multiple screens.
 * Each theater belongs to a geographic region.
 */
@Getter
@Setter
@Entity
public class Theatre extends BaseModel {

    /**
     * Geographic region where this theater is located.
     *
     * @ManyToOne: Many theaters can be in one region
     *
     * Example:
     * Region "Rajkot" has theaters:
     * - PVR Rajkot
     * - INOX R21 Mall
     * - Cinepolis Imperial Square
     */
    @ManyToOne
    private Region region;

    /**
     * All screens in this theater.
     *
     * @OneToMany: One theater has many screens
     *
     * Example:
     * PVR Rajkot has screens:
     * - Screen 1: 200 seats, 2D/3D
     * - Screen 2: 150 seats, Regular
     * - Screen 3: 300 seats, IMAX
     *
     * Naming note: Should be "screens" (lowercase) but kept as "Screens" for consistency
     */
    @OneToMany
    private List<Screen> Screens;

    /**
     * Theater name (e.g., "PVR Rajkot", "INOX R21 Mall").
     *
     * Naming conventions:
     * - Brand + Location: "PVR Rajkot"
     * - Brand + Mall: "INOX R21 Mall"
     * - Full name: "Cinepolis Imperial Square"
     */
    private String name;
}
