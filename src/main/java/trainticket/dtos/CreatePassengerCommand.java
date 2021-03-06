package trainticket.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatePassengerCommand {

    @NotBlank(message = "Name cannot be empty")
    @Schema(description="name of the passenger", example = "John Doe")
    private String name;

    @Past(message = "Birth date cannot be in the future")
    @NotNull(message = "Birth date cannot be empty")
    @Schema(description="passenger's birth date (to computing discounts)", example = "1990-01-01")
    private LocalDate dateOfBirth;
}
