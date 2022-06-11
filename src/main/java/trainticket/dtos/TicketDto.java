package trainticket.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketDto {

    private Long id;

    private int fullPrice;

    private int priceWithDiscount;

    private TrainDto train;

    private PassengerDto passenger;

    private DiscountDto discount;
}
