package MyFirstProject.demo.Repository;

import MyFirstProject.demo.models.Show;
import MyFirstProject.demo.models.ShowSeatType;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ShowSeatTypeRespository extends Repository<ShowSeatType, Long> {

    List<ShowSeatType> findAllByShow(Show show);
}
