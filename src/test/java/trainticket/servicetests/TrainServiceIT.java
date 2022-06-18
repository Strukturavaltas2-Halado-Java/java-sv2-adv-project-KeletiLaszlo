package trainticket.servicetests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import trainticket.dtos.CreateTrainCommand;
import trainticket.dtos.TrainDto;
import trainticket.exceptions.IllegalTimesGivenExceptions;
import trainticket.model.Train;
import trainticket.model.TrainType;
import trainticket.repositories.TrainRepository;
import trainticket.services.TrainService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainServiceIT {

    @Mock
    TrainRepository repository;

    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    TrainService service;

    @Test
    @DisplayName("List all trains")
    void testListAllTrains() {
        when(repository.findAllAndFilter(any(), any(), any())).thenReturn(
                List.of(
                        new Train("Fecske", TrainType.PASSENGER_TRAIN, LocalDateTime.now().plusYears(1), "Cegléd",
                                LocalDateTime.now().plusYears(1).plusHours(1), "Zugló", 60),
                        new Train("Z56", TrainType.FAST_TRAIN, LocalDateTime.now().plusYears(1), "Szolnok",
                                LocalDateTime.now().plusYears(1).plusHours(1).plusMinutes(30), "Budapest", 100)
                )
        );

        assertThat(service.listAllTrains(Optional.empty(), Optional.empty(), Optional.empty()))
                .hasSize(2)
                .extracting(TrainDto::getNameOfTrain)
                .containsExactly("Fecske", "Z56");

        verify(repository).findAllAndFilter(any(), any(), any());
    }

    @Test
    @DisplayName("Find train by id")
    void testFindTrainById() {
        when(repository.findById(1L)).thenReturn(
                Optional.of(
                        new Train(1L, "Fecske", TrainType.PASSENGER_TRAIN, LocalDateTime.now().plusYears(1), "Cegléd",
                                LocalDateTime.now().plusYears(1).plusHours(1), "Zugló", 60, null)
                )
        );

        assertThat(service.findTrainById(1L).getNameOfTrain())
                .isEqualTo("Fecske");

        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Delete train by id")
    void testDeleteTrainById() {
        service.deleteTrainById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("Create new train")
    void testCreateTrain() {
        when(repository.save(any())).thenReturn(
                new Train(1L, "Fecske", TrainType.PASSENGER_TRAIN, LocalDateTime.now().plusYears(1), "Cegléd",
                        LocalDateTime.now().plusYears(1).plusHours(1), "Zugló", 60, null)
        );

        assertThat(service.createTrain(
                new CreateTrainCommand(
                        "Fecske", TrainType.PASSENGER_TRAIN, LocalDateTime.now().plusYears(1), "Cegléd",
                        LocalDateTime.now().plusYears(1).plusHours(1), "Zugló", 60
                )
        ).getNameOfTrain())
                .isEqualTo("Fecske");

        verify(repository).save(any());
    }

    @Test
    @DisplayName("Create new train with bad times")
    void testCreateTrainInvalid() {
        IllegalTimesGivenExceptions itge = assertThrows(IllegalTimesGivenExceptions.class, () ->
                service.createTrain(
                        new CreateTrainCommand(
                                "Fecske", TrainType.PASSENGER_TRAIN, LocalDateTime.now().plusYears(1), "Cegléd",
                                LocalDateTime.now().plusYears(1).minusHours(1), "Zugló", 60
                        )
                ));
        assertThat(itge.getTitle()).isEqualTo("Invalid times given.");
    }
}
