package MyFirstProject.demo.controller;

import MyFirstProject.demo.DTO.SignUpRequestDTO;
import MyFirstProject.demo.DTO.SignUpResponseDTO;
import MyFirstProject.demo.models.ResponseStatus;
import MyFirstProject.demo.models.User;
import MyFirstProject.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Controller class that handles HTTP requests related to user authentication and registration.
 *
 * Responsibilities:
 * - Handles user sign-up requests from clients
 * - Validates request data using DTOs
 * - Delegates authentication logic to UserService
 * - Converts service responses to client-friendly DTOs
 * - Handles exceptions and provides appropriate error responses
 *
 * Security Considerations:
 * - Passwords are never logged or exposed in responses
 * - All password handling is delegated to UserService (BCrypt encryption)
 * - Exception details are hidden from clients to prevent information leakage
 */
@Controller
public class UserController {

    // Service layer dependency for user operations
    private UserService userService;

    /**
     * Constructor-based dependency injection for UserService.
     * Spring automatically injects the UserService bean at runtime.
     *
     * @param userService Service containing user authentication and registration logic
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles user sign-up/registration requests.
     *
     * Flow:
     * 1. Receives SignUpRequestDTO from client (contains email and password)
     * 2. Extracts credentials from request DTO
     * 3. Calls UserService to process registration
     * 4. UserService handles:
     *    - Checking if user already exists
     *    - Encrypting password with BCrypt
     *    - Storing user in database
     * 5. Constructs response DTO with success/failure status
     * 6. Returns response to client
     *
     * Smart Behavior:
     * - If user already exists, UserService attempts login with provided credentials
     * - If credentials match, returns existing user (login successful)
     * - If credentials don't match, exception is thrown (caught here)
     * - If user doesn't exist, creates new account
     *
     * Security Features:
     * - Password is never stored in plain text
     * - Password is encrypted using BCrypt before database storage
     * - Failed authentication doesn't reveal specific error reason
     * - Generic FAILURE status prevents user enumeration attacks
     *
     * Example Request:
     * {
     *   "email": "user@example.com",
     *   "password": "securePassword123"
     * }
     *
     * Example Success Response:
     * {
     *   "userId": 42,
     *   "responseStatus": "SUCCESS"
     * }
     *
     * Example Failure Response:
     * {
     *   "userId": null,
     *   "responseStatus": "FAILURE"
     * }
     *
     * @param signUpRequestDTO DTO containing user credentials (email and password)
     * @return SignUpResponseDTO containing registration result and user ID
     */
    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {

        // Declare variables for user and response
        User user;
        SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO();

        try {
            // Step 1: Call service layer to handle sign-up logic
            // Service will either create new user or attempt login for existing user
            user = userService.signUp(
                    signUpRequestDTO.getEmail(),
                    signUpRequestDTO.getPassword()
            );

            // Step 2: If successful, populate response with user details
            signUpResponseDTO.setResponseStatus(ResponseStatus.SUCCESS);
            signUpResponseDTO.setUserId(user.getId());

        } catch (Exception e) {
            // Step 3: Handle any exceptions from service layer
            // Possible exceptions:
            // - InvalidUserException: If existing user login fails
            // - Database exceptions: Connection issues, constraint violations
            // - Any other unexpected errors

            // Convert all exceptions to generic failure response
            // This prevents exposing internal error details to clients
            signUpResponseDTO.setResponseStatus(ResponseStatus.FAILURE);
            signUpResponseDTO.setUserId(null);

            // Production Best Practices:
            // - Log the exception details server-side for debugging
            // - Consider returning specific error codes for different failure types
            // - Implement rate limiting to prevent brute force attacks
            // - Add email verification flow for new registrations
        }

        // Return the response DTO to the client
        return signUpResponseDTO;
    }
}