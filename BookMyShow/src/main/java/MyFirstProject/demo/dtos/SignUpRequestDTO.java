package MyFirstProject.demo.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for user registration/sign-up requests.
 *
 * Purpose:
 * - Receive user credentials from registration form
 * - Keep registration simple with minimal required fields
 * - Separate authentication data from user profile data
 *
 * Security Considerations:
 * - Password is received in plain text but NEVER stored as plain text
 * - UserService hashes password with BCrypt before storage
 * - HTTPS should be used in production to protect data in transit
 *
 * Minimal Design Philosophy:
 * Start with minimal fields (email + password) to reduce friction
 * Additional profile info (name, phone) can be collected later
 */
@Getter
@Setter
public class SignUpRequestDTO {

    /**
     * User's email address (serves as unique identifier).
     *
     * Why email as identifier:
     * - Natural unique identifier users already have
     * - Easy for password reset workflows
     * - Can be used for communication
     *
     * Validation needed (in production):
     * - @NotBlank: Email must not be empty
     * - @Email: Must be valid email format
     * - @Size(max=255): Limit length
     *
     * Example: "user@example.com"
     */
    private String email;

    /**
     * User's chosen password (plain text in request).
     *
     * CRITICAL SECURITY NOTE:
     * This field receives the password in plain text, but:
     * 1. Should only be transmitted over HTTPS
     * 2. NEVER logged or stored as plain text
     * 3. Immediately hashed with BCrypt in UserService
     * 4. Only the hash is stored in database
     *
     * Validation needed (in production):
     * - @NotBlank: Password required
     * - @Size(min=8, max=100): Enforce length requirements
     * - Custom validation: Check password strength
     *   (uppercase, lowercase, number, special char)
     *
     * Password Storage:
     * Plain text "myPassword123" → BCrypt → "$2a$10$N9qo8..." (stored)
     */
    private String password;

    // Future enhancements:
    // - String confirmPassword (ensure user typed correctly)
    // - String name (user's display name)
    // - String phone (for SMS notifications)
    // - boolean acceptTerms (legal requirement)
}
