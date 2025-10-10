package MyFirstProject.demo.DTO;

import MyFirstProject.demo.models.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookMovieResponseDTO {
    private int totalAmount;
    private Long bookingId;
    private ResponseStatus responseStatus;
}
