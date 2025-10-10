package MyFirstProject.demo.Repository;

import MyFirstProject.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

//    make the repository an interface
//    make the repository extend JPARepository interface
//    JPARepository has predefined methods

    @Override
    Optional<User> findById(Long userId); // Finding the user using the userId
//    internally it writes the query to database using Select, From etc query keywords

    Optional<User> findByEmail(String email);

    User save(User user);

}
