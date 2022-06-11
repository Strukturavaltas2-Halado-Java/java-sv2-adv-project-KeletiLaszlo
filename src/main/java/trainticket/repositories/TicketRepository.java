package trainticket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import trainticket.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
