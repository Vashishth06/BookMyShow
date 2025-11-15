package MyFirstProject.demo.controller;

import MyFirstProject.demo.dtos.BookMovieRequestDTO;
import MyFirstProject.demo.dtos.BookMovieResponseDTO;
import MyFirstProject.demo.models.Booking;
import MyFirstProject.demo.models.ResponseStatus;
import MyFirstProject.demo.services.BookingServices;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Controller class that handles HTTP requests related to movie bookings.
 *
 * Responsibilities:
 * - Receives booking requests from clients (web/mobile apps)
 * - Validates and transforms request data using DTOs
 * - Delegates business logic to BookingServices
 * - Handles exceptions and converts them to user-friendly responses
 * - Returns structured response DTOs to clients
 *
 * Architecture Pattern: MVC (Model-View-Controller)
 * - This is the Controller layer
 * - Separates HTTP handling from business logic
 * - Uses DTOs (Data Transfer Objects) for request/response
 */
@Getter
@Setter
@Controller
public class BookingController {

    // Service layer dependency for booking business logic
    private BookingServices bookingServices;

    /**
     * Constructor-based dependency injection for BookingServices.
     *
     * @param bookingServices Service that contains booking business logic and concurrency control
     */
    @Autowired
    BookingController(BookingServices bookingServices) {
        this.bookingServices = bookingServices;
    }

    /**
     * Handles movie booking requests from clients.
     *
     * Flow:
     * 1. Receives BookMovieRequestDTO from client (contains userId, showId, seatIds)
     * 2. Extracts data from request DTO
     * 3. Calls service layer to process the booking
     * 4. Constructs response DTO based on success or failure
     * 5. Returns response DTO to client
     *
     * Why use DTOs:
     * - Decouples API contract from internal domain models
     * - Allows different data structures for request/response
     * - Provides flexibility to change internal models without breaking API
     * - Reduces data exposure (only send necessary fields to client)
     *
     * Error Handling:
     * - All exceptions are caught and converted to FAILURE response
     * - Prevents internal error details from being exposed to client
     * - Client receives structured error response instead of stack traces
     *
     * Example Request:
     * {
     *   "userId": 123,
     *   "showId": 456,
     *   "showsSeatId": [789, 790, 791]
     * }
     *
     * Example Success Response:
     * {
     *   "bookingId": 999,
     *   "totalAmount": 900,
     *   "responseStatus": "SUCCESS"
     * }
     *
     * Example Failure Response:
     * {
     *   "bookingId": null,
     *   "totalAmount": 0,
     *   "responseStatus": "FAILURE"
     * }
     *
     * @param bookMovieRequestDTO DTO containing booking request details
     * @return BookMovieResponseDTO containing booking result and status
     */
    BookMovieResponseDTO bookMovie(BookMovieRequestDTO bookMovieRequestDTO) {

        // Create response DTO object to be populated and returned
        BookMovieResponseDTO bookMovieResponseDTO = new BookMovieResponseDTO();

        try {
            // Step 1: Extract data from request DTO
            // This separates external API structure from internal method signatures
            Booking booking = bookingServices.bookMovie(
                    bookMovieRequestDTO.getUserId(),
                    bookMovieRequestDTO.getShowId(),
                    bookMovieRequestDTO.getShowsSeatId()
            );

            // Step 2: Populate response DTO with successful booking details
            bookMovieResponseDTO.setBookingId(booking.getId());
            bookMovieResponseDTO.setResponseStatus(ResponseStatus.SUCCESS);
            bookMovieResponseDTO.setTotalAmount(booking.getPrice());

        } catch (Exception e){
            // Step 3: Handle any exceptions from the service layer
            // Exceptions could be:
            // - InvalidUserException: User ID doesn't exist
            // - InvalidShowException: Show ID doesn't exist
            // - ShowSeatNotAvailableException: Seats already booked
            // - Database exceptions: Connection issues, constraint violations

            // Convert exception to user-friendly failure response
            bookMovieResponseDTO.setResponseStatus(ResponseStatus.FAILURE);

            // Note: In production, you might want to:
            // - Log the exception for debugging
            // - Return specific error messages based on exception type
            // - Use proper HTTP status codes (400, 404, 500, etc.)
        }

        // Return the response DTO to the client
        return bookMovieResponseDTO;
    }
}
