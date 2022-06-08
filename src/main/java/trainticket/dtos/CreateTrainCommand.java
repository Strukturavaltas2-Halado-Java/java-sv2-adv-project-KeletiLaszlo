package trainticket.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import trainticket.model.TrainType;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateTrainCommand {

    @NotBlank
    private String nameOfTrain;

    @NotNull
    private TrainType trainType;

    @FutureOrPresent
    private LocalDateTime departureTime;

    @NotBlank
    private String departurePlace;

    @FutureOrPresent
    private LocalDateTime arrivalTime;

    @NotBlank
    private String arrivalPlace;

    @Positive
    private int distance;
}
