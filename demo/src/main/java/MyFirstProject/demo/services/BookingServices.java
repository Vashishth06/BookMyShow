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

@Service
public class BookingServices {
    private UserRepository userRepository;
    private ShowRepository showRepository;
    private ShowSeatRepository showSeatRepository;
    private PriceCalculator priceCalculator;

    @Autowired
    public BookingServices(UserRepository userRepository, ShowSeatRepository showSeatRepository, ShowRepository showRepository,  PriceCalculator priceCalculator) {
        this.userRepository = userRepository;
        this.showSeatRepository = showSeatRepository;
        this.showRepository = showRepository;
        this.priceCalculator = priceCalculator;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Booking bookMovie(Long userId, Long showId, List<Long> showSeatList) throws InvalidUserException, ShowSeatNotAvailableException, InvalidShowException {
//        ---Take a lock--- (Approach 1)
//        Get optionalUser with userId
//        Get show with showId
//        Get showseats with showseatsId
//        ----Take a lock--- (Approach 2)
//        Check if the seats are available
//        if not throw an exception
//        if yes, mark the seat status as Blocked
//        Save the updated status in DB
//        ---Release the lock--- (Approach 2)
//        Create the booking class with Pending status
//        Return the booking object
//        ---Release the lock---(Approach 1)

        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new InvalidUserException("Invalid user. Please enter a valid user");
        }
        User user = optionalUser.get();

        Optional<Show> optionalShow = showRepository.findById(showId);
        if(optionalShow.isEmpty()){
            throw new InvalidShowException("Invalid Show. Please enter a valid Show");
        }
        Show show = optionalShow.get();

        List<ShowSeat> showSeats = showSeatRepository.findAllById(showSeatList);

        for(ShowSeat showSeat : showSeats){
            if(!showSeat.getSeatStatus().equals(SeatStatus.AVAILABLE)){
                throw new ShowSeatNotAvailableException("Seat not available. Please select different seat");
            }
        }

        List<ShowSeat> finalShowSeats = new ArrayList<>();
        for (ShowSeat showSeat : showSeats) {
            showSeat.setSeatStatus(SeatStatus.BLOCKED);

            finalShowSeats.add(showSeatRepository.save(showSeat));
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setTimeOfBooking(new Date());
        booking.setPayments(new ArrayList<>());
        booking.setSeats(finalShowSeats);
        booking.setPrice(priceCalculator.calculatePrice(show, finalShowSeats));

        return booking;
    }
}
