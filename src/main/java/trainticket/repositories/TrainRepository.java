package trainticket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import trainticket.model.Train;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrainRepository extends JpaRepository<Train, Long> {
    @Query("SELECT t FROM Train t WHERE " +
            "(:departurePlace is null or t.departurePlace = :departurePlace) AND " +
            "(:arrivalPlace is null or t.arrivalPlace = :arrivalPlace) AND" +
            "(:departureTime is null or t.departureTime >= :departureTime)")
    List<Train> findAllAndFilter(Optional<String> departurePlace, Optional<String> arrivalPlace, Optional<LocalDateTime> departureTime);
}
