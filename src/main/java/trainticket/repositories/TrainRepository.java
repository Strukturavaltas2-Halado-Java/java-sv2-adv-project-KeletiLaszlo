package trainticket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import trainticket.model.Train;

public interface TrainRepository extends JpaRepository<Train, Long> {
}
