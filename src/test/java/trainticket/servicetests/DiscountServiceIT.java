package trainticket.servicetests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import trainticket.dtos.DiscountDto;
import trainticket.model.Discount;
import trainticket.repositories.DiscountRepository;
import trainticket.services.DiscountService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test DiscountService")
class DiscountServiceIT {

    @Mock
    DiscountRepository repository;

    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    DiscountService service;

    @Test
    @DisplayName("List all discounts")
    void testListAllDiscounts() {
        when(repository.findAll()).thenReturn(
                List.of(
                        new Discount(0, 6, 100),
                        new Discount(6, 14, 50),
                        new Discount(14, 26, 33),
                        new Discount(26, 65, 0),
                        new Discount(65, 999, 100)
                )
        );

        assertThat(service.listAllDiscounts()).hasSize(5)
                .extracting(DiscountDto::getDiscountValue)
                .containsExactly(100, 50, 33, 0, 100);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Find discounts by id")
    void TestFindDiscountById() {
        when(repository.findById(1L)).thenReturn(
                Optional.of(
                        new Discount(1L, 0, 6, 100, null)
                )
        );
        assertThat(service.findDiscountById(1L))
                .extracting(DiscountDto::getDiscountValue)
                .isEqualTo(100);
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Find discounts by id")
    void TestFindDiscountByAge() {
        when(repository.findDiscountByAge(10)).thenReturn(
                new Discount(1L, 6, 14, 50, null)
        );
        assertThat(service.findDiscountByAge(10))
                .extracting(DiscountDto::getDiscountValue)
                .isEqualTo(50);
        verify(repository).findDiscountByAge(10);
    }
}
