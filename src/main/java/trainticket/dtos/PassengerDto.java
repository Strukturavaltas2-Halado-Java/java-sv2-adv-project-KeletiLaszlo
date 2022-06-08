package trainticket.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import trainticket.model.Ticket;

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

    private List<Ticket> tickets = new ArrayList<>();
}
