package trainticket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import trainticket.model.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
