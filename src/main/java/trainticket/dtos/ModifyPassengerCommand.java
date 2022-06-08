package trainticket.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ModifyPassengerCommand {

    @NotBlank
    private String name;

    @Past
    private java.time.LocalDate dateOfBirth;
}
