package MyFirstProject.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Booking extends BaseModel {

    @ManyToOne
    private Show show;

    @ManyToMany
    private List<ShowSeat> seats;

    @OneToMany
    private List<Payment> payments;

    @ManyToOne
    @CreatedBy
    private User user;

    @Enumerated(EnumType.ORDINAL)
    private BookingStatus bookingStatus;

    private int price;
    private Date timeOfBooking;
}
