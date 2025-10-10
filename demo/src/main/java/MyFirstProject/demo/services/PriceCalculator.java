package MyFirstProject.demo.services;
import MyFirstProject.demo.Repository.ShowSeatTypeRespository;
import MyFirstProject.demo.models.Show;
import MyFirstProject.demo.models.ShowSeat;
import MyFirstProject.demo.models.ShowSeatType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceCalculator {

    private ShowSeatTypeRespository showSeatTypeRespository;

    @Autowired
    public PriceCalculator(ShowSeatTypeRespository showSeatTypeRespository) {
        this.showSeatTypeRespository = showSeatTypeRespository;
    }

    public int calculatePrice(Show show, List<ShowSeat> showSeatList) {

        List<ShowSeatType> showSeatTypeList = showSeatTypeRespository.findAllByShow(show);

        int amount = 0;
        for(ShowSeat showSeat : showSeatList){
            for (ShowSeatType showSeatType : showSeatTypeList) {
                if(showSeat.getSeat().getSeatType().equals(showSeatType.getSeatType())){
                    amount += showSeatType.getPrice();
                    break;
                }
            }
        }

        return amount;
    }
}
