package MyFirstProject.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Payment extends BaseModel {

    @Enumerated(EnumType.ORDINAL)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.ORDINAL)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.ORDINAL)
    private PaymentProvider paymentProvider;

    private int amount;
    private int referenceNumber;
    private Date timeStamp;
}
