package MyFirstProject.demo.DTO;

import MyFirstProject.demo.models.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpResponseDTO {
    private ResponseStatus responseStatus;
    private Long userId;
}
