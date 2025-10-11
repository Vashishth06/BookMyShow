package MyFirstProject.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Represents a specific screening of a movie in a screen.
 *
 * Table: Shows (not Show, to avoid SQL keyword)
 *
 * Example: "Inception" playing at 7:00 PM in Screen 1 of PVR Rajkot
 *
 * A Show is NOT the same as a Movie:
 * - Movie: "Inception" (the film itself)
 * - Show: "Inception at 7:00 PM on Oct 10, 2025 in Screen 1"
 *
 * Multiple shows can exist for the same movie (different times, screens, dates).
 *
 * Relationships:
 * - Many shows can screen the same Movie
 * - Many shows can be in the same Screen (at different times)
 */
@Getter
@Setter
@Entity(name = "Shows")  // Table name "Shows" (avoiding SQL keyword "Show")
public class Show extends BaseModel {

    /**
     * The movie being screened in this show.
     *
     * @ManyToOne: Many shows can screen the same movie
     *
     * Example:
     * Movie "Inception" has shows:
     * - Show#1: 3:00 PM in Screen 1
     * - Show#2: 6:00 PM in Screen 1
     * - Show#3: 9:00 PM in Screen 2
     */
    @ManyToOne
    private Movie movie;

    /**
     * The screen where this show is being screened.
     *
     * @ManyToOne: Many shows can be in the same screen (at different times)
     *
     * Example:
     * Screen 1 has shows:
     * - Show#1: "Inception" 3 PM - 6 PM
     * - Show#2: "Interstellar" 6:30 PM - 9:30 PM
     * - Show#3: "Tenet" 10 PM - 1 AM
     */
    @ManyToOne
    private Screen screen;

    /**
     * When the show starts.
     *
     * Used for:
     * - Displaying show timings to users
     * - Preventing booking after show starts
     * - Scheduling seat cleanup after show ends
     *
     * Example: "2025-10-11 19:00:00" (7 PM)
     */
    private Date startTime;

    /**
     * When the show ends.
     *
     * Calculated as: startTime + movie duration + interval time
     *
     * Used for:
     * - Scheduling next show
     * - Determining when seats become available again
     * - Screen allocation planning
     *
     * Example: "2025-10-11 22:00:00" (10 PM)
     */
    private Date endTime;
}
