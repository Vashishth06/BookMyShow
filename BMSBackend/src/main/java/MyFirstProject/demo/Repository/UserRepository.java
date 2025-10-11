package MyFirstProject.demo.Repository;

import MyFirstProject.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity database operations.
 *
 * Spring Data JPA Magic:
 * - By extending JpaRepository, this interface automatically gets implementations for:
 *   - save(), findAll(), findById(), delete(), etc.
 * - No need to write SQL queries manually
 * - Spring generates implementation at runtime
 *
 * Benefits:
 * - Reduces boilerplate code significantly
 * - Type-safe database operations
 * - Built-in transaction management
 * - Easy to test with mocking
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository<User, Long>:
    // - User: Entity type this repository manages
    // - Long: Type of the primary key (id field in User entity)

    /**
     * Finds a user by their unique ID.
     *
     * Why Optional:
     * - User might not exist in database
     * - Optional forces caller to handle "not found" case
     * - Prevents NullPointerException
     * - Modern Java best practice for nullable returns
     *
     * Usage:
     * Optional<User> optionalUser = userRepository.findById(123L);
     * if (optionalUser.isPresent()) {
     *     User user = optionalUser.get();
     *     // use user
     * }
     *
     * SQL Generated:
     * SELECT * FROM user WHERE id = ?
     *
     * @param userId The unique identifier of the user
     * @return Optional containing User if found, empty Optional otherwise
     */
    @Override
    Optional<User> findById(Long userId);

    /**
     * Finds a user by their email address.
     *
     * Custom Query Method:
     * - Spring Data JPA automatically implements this based on method name
     * - Naming convention: findBy + FieldName
     * - "findByEmail" → queries the "email" field
     *
     * How Spring interprets the method name:
     * - "findBy" → SELECT query
     * - "Email" → WHERE email = ?
     *
     * SQL Generated:
     * SELECT * FROM user WHERE email = ?
     *
     * Use Case:
     * - User login (email is unique identifier)
     * - Check if email already exists during signup
     *
     * @param email Email address to search for
     * @return Optional containing User if found, empty Optional otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Saves a user to the database (insert or update).
     *
     * Smart Behavior:
     * - If user.id is null → INSERT new record
     * - If user.id exists → UPDATE existing record
     *
     * Inherited from JpaRepository:
     * - Don't need to implement this manually
     * - Automatically handles transactions
     * - Returns the saved entity with generated ID
     *
     * SQL Generated:
     * INSERT INTO user (name, email, password, created_at, updated_at)
     * VALUES (?, ?, ?, ?, ?)
     *
     * OR
     *
     * UPDATE user
     * SET name=?, email=?, password=?, updated_at=?
     * WHERE id=?
     *
     * @param user User entity to save
     * @return Saved user with generated ID (if new)
     */
    User save(User user);
}