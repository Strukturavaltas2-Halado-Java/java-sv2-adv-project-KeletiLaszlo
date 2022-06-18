package trainticket.servicetests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import trainticket.dtos.CreatePassengerCommand;
import trainticket.dtos.PassengerDto;
import trainticket.model.Passenger;
import trainticket.repositories.PassengerRepository;
import trainticket.services.PassengerService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {

    @Mock
    PassengerRepository repository;

    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    PassengerService service;

    @Test
    @DisplayName("List all passengers")
    void testListAllPassengers() {
        when(repository.findAll()).thenReturn(
                List.of(
                        new Passenger("John Doe", LocalDate.of(1990, 1, 1)),
                        new Passenger("Jane Doe", LocalDate.of(2010, 1, 1))
                )
        );

        assertThat(service.listAllPassengers())
                .hasSize(2)
                .extracting(PassengerDto::getName)
                .containsExactly("John Doe", "Jane Doe");
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Find passenger by id")
    void testFindPassengerById() {
        when(repository.findById(1L)).thenReturn(
                Optional.of(
                        new Passenger(1L, "John Doe", LocalDate.of(1990, 1, 1), null)
                )
        );

        assertThat(service.findPassengerById(1L).getName())
                .isEqualTo("John Doe");
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Create new passenger")
    void testCreatePassenger() {
        when(repository.save(any())).thenReturn(
                new Passenger(1L, "John Doe", LocalDate.of(1990, 1, 1), null)
        );

        assertThat(service.createPassenger(new CreatePassengerCommand("John Doe", LocalDate.of(1990, 1, 1))).getName())
                .isEqualTo("John Doe");
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Delete passenger")
    void testDeletePassengerById() {

        service.deletePassengerById(1L);
        verify(repository).deleteById(1L);
    }
}
