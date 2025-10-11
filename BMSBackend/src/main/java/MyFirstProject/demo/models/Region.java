package MyFirstProject.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents a geographic region/city.
 *
 * Table: region
 *
 * Example: Rajkot, Mumbai, Delhi, Bangalore
 *
 * Purpose:
 * - Organize theaters by location
 * - Allow users to filter theaters by city
 * - Support multi-city operations
 *
 * A region contains multiple theaters.
 */
@Getter
@Setter
@Entity
public class Region extends BaseModel {

    /**
     * All theaters in this region.
     *
     * @OneToMany: One region has many theaters
     *
     * Example:
     * Region "Mumbai" has theaters:
     * - PVR Juhu
     * - INOX Nariman Point
     * - Cinepolis Andheri
     * - PVR Phoenix Mills
     */
    @OneToMany
    private List<Theatre> theatres;

    /**
     * Region name (e.g., "Rajkot", "Mumbai", "Delhi").
     *
     * Naming conventions:
     * - City name: "Rajkot", "Mumbai"
     * - State (for smaller cities): "Rajkot, Gujarat"
     * - Metro area: "Delhi NCR"
     */
    private String name;
}