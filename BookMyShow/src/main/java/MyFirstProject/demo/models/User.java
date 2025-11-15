package MyFirstProject.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Represents a user in the BookMyShow system.
 * Users can sign up, log in, and make movie bookings.
 *
 * Table: user
 * Primary Key: id (inherited from BaseModel)
 *
 * Security:
 * - Password is stored as BCrypt hash, never plain text
 * - Email is used as unique identifier for login
 *
 * Relationships:
 * - One user can have many bookings (OneToMany)
 */
@Getter
@Setter
@Entity  // JPA annotation: Maps this class to 'user' table
public class User extends BaseModel {

    /**
     * All bookings made by this user.
     *
     * @OneToMany: One user can have many bookings
     * Bidirectional relationship with Booking entity
     *
     * Example:
     * User john has bookings: [Booking#1, Booking#2, Booking#3]
     *
     * Lazy loading: Bookings are loaded only when accessed
     * user.getBookings() → triggers database query
     */
    @OneToMany
    private List<Booking> bookings;

    /**
     * User's display name.
     * Currently optional, can be null during signup.
     *
     * In production, consider:
     * - @Column(nullable = false) to make it required
     * - @Size(min = 2, max = 100) for validation
     */
    private String name;

    /**
     * User's email address (unique identifier).
     * Used for login and communication.
     *
     * Best practices for production:
     * - Add @Column(unique = true) to enforce uniqueness at DB level
     * - Add @Email validation
     * - Store in lowercase for case-insensitive comparison
     *
     * Example: "user@example.com"
     */
    private String email;

    /**
     * User's password stored as BCrypt hash.
     *
     * CRITICAL SECURITY:
     * - NEVER store plain text passwords!
     * - This field contains BCrypt hash (e.g., "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy")
     * - BCrypt automatically includes salt in the hash
     * - Cannot be reversed to get original password
     *
     * Password Flow:
     * 1. User signs up with "myPassword123"
     * 2. BCryptPasswordEncoder.encode("myPassword123") → hash
     * 3. Hash is stored here
     * 4. During login, BCrypt compares input with stored hash
     */
    private String password;
}
