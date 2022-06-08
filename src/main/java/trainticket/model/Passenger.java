package trainticket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "passengers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "passenger_name")
    private String name;

    @Column(name = "date_of_birth")
    private java.time.LocalDate dateOfBirth;

    @OneToMany(mappedBy = "passenger")
    private List<Ticket> tickets = new ArrayList<>();
}
