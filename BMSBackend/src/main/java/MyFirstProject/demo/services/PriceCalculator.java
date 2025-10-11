package MyFirstProject.demo.services;

import MyFirstProject.demo.Repository.ShowSeatTypeRespository;
import MyFirstProject.demo.models.Show;
import MyFirstProject.demo.models.ShowSeat;
import MyFirstProject.demo.models.ShowSeatType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for calculating the total price of a movie booking.
 *
 * Pricing Logic:
 * - Different seat types (VIP, Premium, Normal) have different prices
 * - Prices can vary based on the show (e.g., special screenings, prime time)
 * - Each show-seat-type combination has a specific price defined in ShowSeatType table
 *
 * Example Pricing Structure:
 * Show A - Normal Seat: ₹200
 * Show A - Premium Seat: ₹350
 * Show A - VIP Seat: ₹500
 * Show B - Normal Seat: ₹250 (different show, different pricing)
 */
@Service
public class PriceCalculator {

    private ShowSeatTypeRespository showSeatTypeRespository;

    /**
     * Constructor-based dependency injection for ShowSeatTypeRepository.
     *
     * @param showSeatTypeRespository Repository to fetch price information for different seat types
     */
    @Autowired
    public PriceCalculator(ShowSeatTypeRespository showSeatTypeRespository) {
        this.showSeatTypeRespository = showSeatTypeRespository;
    }

    /**
     * Calculates the total booking amount based on selected seats and their types.
     *
     * Algorithm:
     * 1. Fetch all seat type prices for the given show
     * 2. For each seat in the booking:
     *    a) Identify the seat's type (Normal/Premium/VIP)
     *    b) Find the corresponding price for that seat type in this show
     *    c) Add the price to the running total
     *
     * Time Complexity: O(n * m) where:
     * - n = number of seats being booked
     * - m = number of different seat types in the show
     *
     * Example Calculation:
     * If booking 2 Normal seats (₹200 each) and 1 VIP seat (₹500):
     * Total = 200 + 200 + 500 = ₹900
     *
     * @param show The movie show for which seats are being booked
     * @param showSeatList List of seats selected by the user
     * @return Total amount to be paid for the booking
     */
    public int calculatePrice(Show show, List<ShowSeat> showSeatList) {

        // Step 1: Retrieve all seat type pricing information for this show
        // ShowSeatType contains the mapping between seat types and their prices for a specific show
        List<ShowSeatType> showSeatTypeList = showSeatTypeRespository.findAllByShow(show);

        // Initialize total amount
        int amount = 0;

        // Step 2: Iterate through each seat selected by the user
        for(ShowSeat showSeat : showSeatList){

            // Step 3: Find the price for this seat's type
            // We need to match the seat's type with the show's pricing structure
            for (ShowSeatType showSeatType : showSeatTypeList) {

                // Step 4: Check if current showSeatType matches this seat's type
                // showSeat.getSeat().getSeatType() gives us the type of seat (Normal/Premium/VIP)
                // showSeatType.getSeatType() gives us the type from the pricing table
                if(showSeat.getSeat().getSeatType().equals(showSeatType.getSeatType())){

                    // Step 5: Add the price to the total
                    amount += showSeatType.getPrice();

                    // Step 6: Break inner loop once we find the matching seat type
                    // No need to check remaining seat types for this seat
                    break;
                }
            }
        }

        // Return the total calculated amount
        return amount;
    }
}