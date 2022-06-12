package trainticket.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateTicketCommand {

    @Positive
    @NotNull
    @Schema(description="train ID", example = "1")
    private Long trainId;

    @Positive
    @NotNull
    @Schema(description="passenger ID", example = "1")
    private Long passengerId;
}
