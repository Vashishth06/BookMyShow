package MyFirstProject.demo.services;

import MyFirstProject.demo.Exceptions.InvalidShowException;
import MyFirstProject.demo.Exceptions.InvalidUserException;
import MyFirstProject.demo.Exceptions.ShowSeatNotAvailableException;
import MyFirstProject.demo.Repository.ShowRepository;
import MyFirstProject.demo.Repository.ShowSeatRepository;
import MyFirstProject.demo.Repository.UserRepository;
import MyFirstProject.demo.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for handling movie ticket booking operations.
 * This service implements concurrency control to prevent double-booking of seats
 * using database transaction isolation levels.
 *
 * Key Features:
 * - Thread-safe seat booking with SERIALIZABLE isolation level
 * - Atomic operations for seat status updates
 * - Price calculation integration
 * - Comprehensive exception handling
 */
@Service
public class BookingServices {

    // Repository dependencies for database operations
    private UserRepository userRepository;
    private ShowRepository showRepository;
    private ShowSeatRepository showSeatRepository;
    private PriceCalculator priceCalculator;

    /**
     * Constructor-based dependency injection for all required repositories and services.
     * Spring automatically injects these dependencies at runtime.
     *
     * @param userRepository Repository for user-related database operations
     * @param showSeatRepository Repository for show seat operations
     * @param showRepository Repository for show/movie operations
     * @param priceCalculator Service to calculate total booking price
     */
    @Autowired
    public BookingServices(UserRepository userRepository,
                           ShowSeatRepository showSeatRepository,
                           ShowRepository showRepository,
                           PriceCalculator priceCalculator) {
        this.userRepository = userRepository;
        this.showSeatRepository = showSeatRepository;
        this.showRepository = showRepository;
        this.priceCalculator = priceCalculator;
    }

    /**
     * Books movie tickets for a user with specified seats for a show.
     *
     * CONCURRENCY CONTROL:
     * This method uses @Transactional with SERIALIZABLE isolation level to handle concurrent booking requests.
     * SERIALIZABLE is the highest isolation level and prevents:
     * - Dirty reads: Reading uncommitted data from other transactions
     * - Non-repeatable reads: Getting different values when reading the same data twice
     * - Phantom reads: New rows appearing in a range query
     *
     * How it prevents double-booking:
     * 1. When multiple users try to book the same seat simultaneously, the database
     *    serializes these transactions - only one executes at a time
     * 2. The first transaction checks seat availability, blocks the seat, and commits
     * 3. Subsequent transactions will see the seat as BLOCKED and throw an exception
     * 4. If any error occurs, the entire transaction is rolled back automatically
     *
     * Transaction Flow:
     * 1. Validate user exists
     * 2. Validate show exists
     * 3. Fetch all requested seats
     * 4. Check if all seats are available (CRITICAL SECTION - protected by transaction)
     * 5. Mark seats as BLOCKED (prevents other transactions from booking)
     * 6. Create booking with PENDING status
     * 7. Calculate and set total price
     * 8. Transaction commits (or rolls back on any exception)
     *
     * @param userId The ID of the user making the booking
     * @param showId The ID of the movie show to book
     * @param showSeatList List of seat IDs the user wants to book
     * @return Booking object containing booking details with PENDING status
     * @throws InvalidUserException If the user ID doesn't exist in the database
     * @throws InvalidShowException If the show ID doesn't exist in the database
     * @throws ShowSeatNotAvailableException If any requested seat is already booked or blocked
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Booking bookMovie(Long userId, Long showId, List<Long> showSeatList)
            throws InvalidUserException, ShowSeatNotAvailableException, InvalidShowException {

        // Step 1: Validate and retrieve the user
        // Optional pattern is used to handle cases where the user might not exist
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new InvalidUserException("Invalid user. Please enter a valid user");
        }
        User user = optionalUser.get();

        // Step 2: Validate and retrieve the show
        Optional<Show> optionalShow = showRepository.findById(showId);
        if(optionalShow.isEmpty()){
            throw new InvalidShowException("Invalid Show. Please enter a valid Show");
        }
        Show show = optionalShow.get();

        // Step 3: Retrieve all requested seats in a single database call
        // This is more efficient than fetching seats one by one
        List<ShowSeat> showSeats = showSeatRepository.findAllById(showSeatList);

        // Step 4: CRITICAL SECTION - Check seat availability
        // This check is protected by the SERIALIZABLE transaction
        // No other transaction can modify these seats until this transaction completes
        for(ShowSeat showSeat : showSeats){
            if(!showSeat.getSeatStatus().equals(SeatStatus.AVAILABLE)){
                // If any seat is not available, throw exception and rollback entire transaction
                throw new ShowSeatNotAvailableException("Seat not available. Please select different seat");
            }
        }

        // Step 5: Mark all seats as BLOCKED
        // BLOCKED status prevents other users from booking these seats
        // The seats will remain BLOCKED until payment is completed or booking expires
        List<ShowSeat> finalShowSeats = new ArrayList<>();
        for (ShowSeat showSeat : showSeats) {
            showSeat.setSeatStatus(SeatStatus.BLOCKED);
            // Persist the updated seat status immediately
            finalShowSeats.add(showSeatRepository.save(showSeat));
        }

        // Step 6: Create the booking object with all necessary details
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setBookingStatus(BookingStatus.PENDING); // Payment yet to be completed
        booking.setTimeOfBooking(new Date()); // Record booking timestamp
        booking.setPayments(new ArrayList<>()); // Initialize empty payment list
        booking.setSeats(finalShowSeats); // Associate blocked seats with booking

        // Step 7: Calculate total price based on seat types
        // Different seat types (VIP, Normal, Premium) have different prices
        booking.setPrice(priceCalculator.calculatePrice(show, finalShowSeats));

        // Return the booking object (will be persisted by the controller/caller)
        // Transaction commits here if no exceptions were thrown
        return booking;
    }
}