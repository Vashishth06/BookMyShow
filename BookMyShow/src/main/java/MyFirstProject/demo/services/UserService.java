package MyFirstProject.demo.services;

import MyFirstProject.demo.exceptions.InvalidUserException;
import MyFirstProject.demo.repositories.UserRepository;
import MyFirstProject.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for user authentication and registration operations.
 *
 * Security Features:
 * - Password hashing using BCrypt algorithm (prevents storing plain-text passwords)
 * - Login validation with encrypted password comparison
 * - Automatic login after successful registration
 *
 * BCrypt Security:
 * BCrypt is a password hashing function designed to be slow and computationally expensive,
 * making brute-force attacks impractical. Each password gets a unique salt for added security.
 */
@Service
public class UserService {

    private UserRepository userRepository;

    /**
     * Constructor-based dependency injection for UserRepository.
     *
     * @param userRepository Repository for user database operations
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Authenticates a user by verifying their email and password.
     *
     * Security Process:
     * 1. Retrieves user from database using email
     * 2. Extracts the hashed password stored in database
     * 3. Uses BCrypt to compare the provided password with the stored hash
     * 4. BCrypt automatically handles salt extraction and comparison
     *
     * Why BCrypt.matches() and not direct comparison:
     * - Direct string comparison (==) would fail because passwords are hashed
     * - BCrypt.matches() internally:
     *   a) Extracts the salt from the stored hash
     *   b) Hashes the input password with the same salt
     *   c) Compares the two hashes in constant time (prevents timing attacks)
     *
     * @param email User's email address (used as unique identifier)
     * @param password Plain-text password provided by user
     * @return true if authentication successful, false otherwise
     * @throws InvalidUserException if user doesn't exist or password is incorrect
     */
    public boolean login(String email, String password) throws InvalidUserException {

        // Step 1: Check if user exists in database
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()){
            // User not found - throw exception to prevent user enumeration attacks
            throw new InvalidUserException("User not register. Please sign-up first.");
        }

        // Step 2: Retrieve the hashed password from database
        // This is NOT the plain-text password but a BCrypt hash
        String passwordStoredInDB = optionalUser.get().getPassword();

        // Step 3: Create BCrypt encoder instance for password verification
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Step 4: Verify password
        // The matches() method safely compares plain-text input with hashed password
        if(!encoder.matches(password, passwordStoredInDB)){
            throw new InvalidUserException("Invalid password.");
        }

        // Authentication successful
        return encoder.matches(password, passwordStoredInDB);
    }

    /**
     * Registers a new user or logs in an existing user.
     *
     * Smart Behavior:
     * - If email already exists: Attempts to log in with provided password
     * - If email is new: Creates new account with encrypted password
     *
     * Password Encryption Process:
     * 1. BCryptPasswordEncoder generates a random salt
     * 2. Salt is combined with the password
     * 3. The combination is hashed using BCrypt algorithm
     * 4. The hash (which includes the salt) is stored in the database
     *
     * Security Benefits:
     * - Even if two users have the same password, their hashes will be different (due to unique salts)
     * - Database compromise doesn't reveal actual passwords
     * - Hashes cannot be reversed to get the original password
     *
     * @param email User's email address (must be unique)
     * @param password Plain-text password chosen by user
     * @return User object representing the registered or logged-in user
     * @throws InvalidUserException if login fails for existing user
     */
    public User signUp(String email, String password) throws InvalidUserException {

        // Step 1: Check if user already exists
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()){
            // User exists - attempt login instead of creating duplicate account
            // This also validates if the provided password is correct
            login(email, password);
            return optionalUser.get();
        }

        // Step 2: Create new user object
        User user = new User();
        user.setEmail(email);

        // Step 3: Hash the password using BCrypt
        // BCryptPasswordEncoder automatically:
        // - Generates a random salt
        // - Applies the BCrypt hashing algorithm
        // - Combines salt and hash into a single string
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(password));

        // Step 4: Persist the new user to database
        // Password is stored in hashed form, never in plain text
        userRepository.save(user);

        // Return the created user object
        return user;
    }
}
