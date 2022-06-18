package trainticket.repositorytests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import trainticket.model.Train;
import trainticket.model.TrainType;
import trainticket.repositories.TrainRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TrainRepositoryIT {

    @Autowired
    TrainRepository repository;

    Train trainOne;
    Train trainTwo;
    Train trainThree;

    @BeforeEach
    void init() {
        trainOne = new Train("Fecske", TrainType.PASSENGER_TRAIN, LocalDateTime.now().plusYears(1),"Cegléd",
                LocalDateTime.now().plusYears(1).plusHours(1),"Zugló",60);
        trainTwo = new Train("Z56", TrainType.FAST_TRAIN, LocalDateTime.now().plusYears(1),"Szolnok",
                LocalDateTime.now().plusYears(1).plusHours(1).plusMinutes(30),"Budapest",100);
        trainThree = new Train("PAVA", TrainType.INTERCITY, LocalDateTime.now().plusYears(2),"Debrecen",
                LocalDateTime.now().plusYears(2).plusHours(3),"Budapest",221);

        repository.save(trainOne);
        repository.save(trainTwo);
        repository.save(trainThree);
    }

    @Test
    @DisplayName("Find all and no filter use")
    void findAllAndFilterNoFilter() {
        List<Train> trains = repository.findAllAndFilter(Optional.empty(),Optional.empty(),Optional.empty());
        assertThat(trains).hasSize(3)
                .extracting(Train::getNameOfTrain).containsExactly("Fecske","Z56","PAVA");
    }

    @Test
    @DisplayName("Find all and filter to departure place")
    void findAllAndFilterOnlyDeparturePlace() {
        List<Train> trains = repository.findAllAndFilter(Optional.of("Szolnok"),Optional.empty(),Optional.empty());
        assertThat(trains).hasSize(1)
                .extracting(Train::getNameOfTrain).containsExactly("Z56");
    }

    @Test
    @DisplayName("Find all and filter to arrival place")
    void findAllAndFilterOnlyArrival() {
        List<Train> trains = repository.findAllAndFilter(Optional.empty(), Optional.of("Budapest"),Optional.empty());
        assertThat(trains).hasSize(2)
                .extracting(Train::getNameOfTrain).containsExactly("Z56","PAVA");
    }

    @Test
    @DisplayName("Find all and filter to departure and arrival places")
    void findAllAndFilterOnlyDeparturePlaceAndArrival() {
        List<Train> trains = repository.findAllAndFilter(Optional.of("Szolnok"), Optional.of("Budapest"),Optional.empty());
        assertThat(trains).hasSize(1)
                .extracting(Train::getNameOfTrain).containsExactly("Z56");
    }

    @Test
    @DisplayName("Find all and filter to departure time")
    void findAllAndFilterOnlyDepartureTime() {
        List<Train> trains = repository.findAllAndFilter(Optional.empty(), Optional.empty(),Optional.of(trainThree.getDepartureTime().minusDays(1)));
        assertThat(trains).hasSize(1)
                .extracting(Train::getNameOfTrain).containsExactly("PAVA");
    }

    @Test
    @DisplayName("Find all and filter to departure place and time")
    void findAllAndFilterOnlyDeparturePlaceAndDepartureTime() {
        List<Train> trains = repository.findAllAndFilter(Optional.of("Szolnok"), Optional.empty(),Optional.of(trainOne.getDepartureTime().minusDays(1)));
        assertThat(trains).hasSize(1)
                .extracting(Train::getNameOfTrain).containsExactly("Z56");
    }

    @Test
    @DisplayName("Find all and filter to arrival place and departure time")
    void findAllAndFilterOnlyArrivalAndDepartureTime() {
        List<Train> trains = repository.findAllAndFilter(Optional.empty(), Optional.of("Budapest"),Optional.of(trainOne.getDepartureTime().minusDays(1)));
        assertThat(trains).hasSize(2)
                .extracting(Train::getNameOfTrain).containsExactly("Z56","PAVA");
    }

    @Test
    @DisplayName("Find all and filter to all options")
    void findAllAndFilter() {
        List<Train> trains = repository.findAllAndFilter(Optional.of("Debrecen"), Optional.of("Budapest"),Optional.of(trainOne.getDepartureTime().minusDays(1)));
        assertThat(trains).hasSize(1)
                .extracting(Train::getNameOfTrain).containsExactly("PAVA");
    }
}
