package trainticket.dtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PassengerDto {

    private Long id;

    private String name;

    private java.time.LocalDate dateOfBirth;

    @JsonBackReference
    private List<TicketDto> tickets = new ArrayList<>();
}
