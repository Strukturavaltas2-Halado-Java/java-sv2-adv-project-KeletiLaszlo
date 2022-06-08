package trainticket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import trainticket.model.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    @Query("SELECT d FROM Discount d WHERE d.fromAge <= :age AND d.toAge > :age")
    Discount findDiscountByAge(int age);
}
