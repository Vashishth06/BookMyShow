package MyFirstProject.demo.controller;

import MyFirstProject.demo.DTO.BookMovieRequestDTO;
import MyFirstProject.demo.DTO.BookMovieResponseDTO;
import MyFirstProject.demo.models.Booking;
import MyFirstProject.demo.models.ResponseStatus;
import MyFirstProject.demo.services.BookingServices;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Getter
@Setter
@Controller
public class BookingController {

    private BookingServices bookingServices;

    @Autowired
    BookingController(BookingServices bookingServices) {
        this.bookingServices = bookingServices;
    }

    BookMovieResponseDTO bookMovie(BookMovieRequestDTO bookMovieRequestDTO) {

        BookMovieResponseDTO bookMovieResponseDTO = new BookMovieResponseDTO();

        try {
            Booking booking = bookingServices.bookMovie(
                    bookMovieRequestDTO.getUserId(),
                    bookMovieRequestDTO.getShowId(),
                    bookMovieRequestDTO.getShowsSeatId()
            );

            bookMovieResponseDTO.setBookingId(booking.getId());
            bookMovieResponseDTO.setResponseStatus(ResponseStatus.SUCCESS);
            bookMovieResponseDTO.setTotalAmount(booking.getPrice());

        } catch (Exception e){
            bookMovieResponseDTO.setResponseStatus(ResponseStatus.FAILURE);
        }

        return bookMovieResponseDTO;
    }
}
