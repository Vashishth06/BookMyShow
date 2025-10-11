package MyFirstProject.demo.DTO;

import MyFirstProject.demo.models.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for user registration/sign-up responses.
 *
 * Purpose:
 * - Inform client whether registration was successful
 * - Provide user ID for immediate use (auto-login after signup)
 * - Hide sensitive user information from response
 *
 * Dual Purpose:
 * This DTO is returned for both:
 * 1. New user registration
 * 2. Existing user login (if email already exists)
 *
 * Why return userId:
 * - Allows client to auto-login after registration
 * - Client can make authenticated requests immediately
 * - Reduces need for separate login call
 */
@Getter
@Setter
public class SignUpResponseDTO {

    /**
     * Status of the registration operation.
     *
     * Possible scenarios:
     *
     * SUCCESS:
     * - New user created successfully
     * - OR existing user logged in successfully
     *
     * FAILURE:
     * - Email format invalid
     * - Password too weak
     * - Existing user with wrong password
     * - Database error
     *
     * Security Note:
     * Generic FAILURE status prevents user enumeration attacks
     * (attacker can't tell if email exists or password is wrong)
     */
    private ResponseStatus responseStatus;

    /**
     * ID of the user (newly created or existing).
     *
     * Success scenario:
     * - Contains the user's ID from database
     * - Client can use this for subsequent authenticated requests
     *
     * Failure scenario:
     * - Contains null
     *
     * Use Cases:
     * - Store in client session/localStorage
     * - Include in future API requests
     * - Track user across application
     *
     * Security Consideration:
     * In production, use JWT token instead of raw user ID
     * Token contains: userId + expiry + signature
     */
    private Long userId;

    // Production enhancements:
    // - String token (JWT for authentication)
    // - String message (specific success/error message)
    // - User userProfile (basic user info for display)
    // - boolean isNewUser (distinguish new signup vs login)
}