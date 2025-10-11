package MyFirstProject.demo.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a movie/film.
 *
 * Table: movie
 *
 * Example: "Inception", "Avengers", "3 Idiots"
 *
 * A movie is the film itself (not a screening).
 * The same movie can have multiple shows across different theaters and times.
 *
 * Current implementation is minimal.
 * Production version would have many more fields.
 */
@Getter
@Setter
@Entity
public class Movie extends BaseModel {

    /**
     * Movie title.
     *
     * Examples:
     * - "Inception"
     * - "The Dark Knight"
     * - "3 Idiots"
     */
    private String title;

    // Additional fields for production:
    // - String description (plot summary)
    // - String genre (Action, Comedy, Drama)
    // - int duration (in minutes)
    // - String rating (U, UA, A, R)
    // - String cast (comma-separated actor names)
    // - String director
    // - String posterUrl (image URL)
    // - String trailerUrl (video URL)
    // - Date releaseDate
    // - String language (Hindi, English, Tamil)
}