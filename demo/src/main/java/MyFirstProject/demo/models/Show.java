package MyFirstProject.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity(name = "Shows")
public class Show extends BaseModel{

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private Screen screen;


    private Date startTime;
    private Date endTime;

}
