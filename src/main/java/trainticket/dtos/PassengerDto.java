package trainticket.dtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PassengerDto {

    @Schema(description = "passenger ID", example = "1")
    private Long id;

    @Schema(description = "passenger name", example = "John Doe")
    private String name;

    @Schema(description="passenger's birth date (to computing discounts)", example = "1990-01-01")
    private LocalDate dateOfBirth;

    @JsonBackReference
    @Schema(description="list of the passenger's tickets")
    private List<TicketDto> tickets = new ArrayList<>();
}
