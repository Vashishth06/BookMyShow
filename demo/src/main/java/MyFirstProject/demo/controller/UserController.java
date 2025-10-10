package MyFirstProject.demo.controller;

import MyFirstProject.demo.DTO.SignUpRequestDTO;
import MyFirstProject.demo.DTO.SignUpResponseDTO;
import MyFirstProject.demo.models.ResponseStatus;
import MyFirstProject.demo.models.User;
import MyFirstProject.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) {
        User user;
        SignUpResponseDTO signUpResponseDTO = new SignUpResponseDTO();;
        try {
            user = userService.signUp(signUpRequestDTO.getEmail(), signUpRequestDTO.getPassword());
            signUpResponseDTO.setResponseStatus(ResponseStatus.SUCCESS);
            signUpResponseDTO.setUserId(user.getId());

        } catch (Exception e) {
            signUpResponseDTO.setResponseStatus(ResponseStatus.FAILURE);
            signUpResponseDTO.setUserId(null);
        }


        return signUpResponseDTO;
    }
}
