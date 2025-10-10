package MyFirstProject.demo.services;

import MyFirstProject.demo.Exceptions.InvalidUserException;
import MyFirstProject.demo.Repository.UserRepository;
import MyFirstProject.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(String email, String password) throws InvalidUserException {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()){
            throw new InvalidUserException("User not register. Please sign-up first.");
        }

        String passwordStoredInDB = optionalUser.get().getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(password, passwordStoredInDB)){
            throw new InvalidUserException("Invalid password.");
        }

        return encoder.matches(password, passwordStoredInDB);
    }

    public User signUp(String email, String password) throws InvalidUserException {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()){
            return login(email, password);
        }

        User user = new User();
        user.setEmail(email);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);


        return user;
    }
}
