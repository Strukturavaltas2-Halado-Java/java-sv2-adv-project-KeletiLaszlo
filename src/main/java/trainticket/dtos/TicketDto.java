package trainticket.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketDto {

    @Schema(description="ticket id", example = "1")
    private Long id;

    @Schema(description="full price of the ticket", example = "5000")
    private int fullPrice;

    @Schema(description="discount price of the ticket", example = "5000")
    private int priceWithDiscount;

    @Schema(description="train")
    private TrainDto train;

    @Schema(description="passenger")
    private PassengerDto passenger;

    @Schema(description="discount")
    private DiscountDto discount;
}
