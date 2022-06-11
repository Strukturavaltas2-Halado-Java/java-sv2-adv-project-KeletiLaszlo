package trainticket.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateTicketCommand {

    @Positive
    private Long trainId;
    @Positive
    private Long passengerId;
}
