package trainticket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "dicounts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_age")
    private int fromAge;
    @Column(name = "to_age")
    private int toAge;
    @Column(name = "discount_value")
    private int discountValue;

    @OneToMany
    private List<Ticket> ticket;
}
