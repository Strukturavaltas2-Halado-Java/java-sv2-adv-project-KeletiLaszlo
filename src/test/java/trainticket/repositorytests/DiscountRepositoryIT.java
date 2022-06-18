package trainticket.repositorytests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import trainticket.model.Discount;
import trainticket.repositories.DiscountRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DiscountRepositoryIT {

    @Autowired
    DiscountRepository repository;

    @Test
    void testFindDiscountByAgeUnderSix() {
        Discount discount = repository.findDiscountByAge(5);
        assertThat(discount.getDiscountValue()).isEqualTo(100);
    }

    @Test
    void testFindDiscountByAgeUnderFourteen() {
        Discount discount = repository.findDiscountByAge(13);
        assertThat(discount.getDiscountValue()).isEqualTo(50);
    }

    @Test
    void testFindDiscountByAgeUnderTwentySix() {
        Discount discount = repository.findDiscountByAge(25);
        assertThat(discount.getDiscountValue()).isEqualTo(33);
    }

    @Test
    void testFindDiscountByAgeUnderSixtyFive() {
        Discount discount = repository.findDiscountByAge(64);
        assertThat(discount.getDiscountValue()).isZero();
    }

    @Test
    void testFindDiscountByAgeSixtyFive() {
        Discount discount = repository.findDiscountByAge(65);
        assertThat(discount.getDiscountValue()).isEqualTo(100);
    }
}
