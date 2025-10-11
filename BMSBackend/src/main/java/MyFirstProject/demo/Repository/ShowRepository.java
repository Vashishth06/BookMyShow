package MyFirstProject.demo.Repository;

import MyFirstProject.demo.models.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Show entity database operations.
 *
 * Purpose:
 * - Manages database operations for movie shows/screenings
 * - Provides CRUD operations for Show entities
 * - Handles queries related to show schedules
 *
 * A Show represents a specific movie screening:
 * Example: "Inception at 7:00 PM on Oct 11, 2025 in Screen 1"
 *
 * Inherited Methods (from JpaRepository):
 * - findById(Long id) → Find show by ID
 * - findAll() → Get all shows
 * - save(Show show) → Create or update show
 * - delete(Show show) → Remove show
 * - count() → Get total number of shows
 * - existsById(Long id) → Check if show exists
 */

@Repository
public interface ShowRepository extends JpaRepository<Show,Long> {
}
