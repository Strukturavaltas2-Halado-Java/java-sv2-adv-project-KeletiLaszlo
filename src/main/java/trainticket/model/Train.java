package trainticket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="trains")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_of_train")
    private String nameOfTrain;

    @Enumerated(EnumType.STRING)
    private TrainType trainType;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "departure_place")
    private String departurePlace;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @Column(name = "arrival_place")
    private String arrivalPlace;

    private int distance;

    @OneToMany(mappedBy = "train")
    private List<Ticket> tickets = new ArrayList<>();


    public Train(String nameOfTrain, TrainType trainType, LocalDateTime departureTime, String departurePlace, LocalDateTime arrivalTime, String arrivalPlace, int distance) {
        this.nameOfTrain = nameOfTrain;
        this.trainType = trainType;
        this.departureTime = departureTime;
        this.departurePlace = departurePlace;
        this.arrivalTime = arrivalTime;
        this.arrivalPlace = arrivalPlace;
        this.distance = distance;
    }
}
