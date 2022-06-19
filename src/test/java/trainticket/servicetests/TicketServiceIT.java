package trainticket.servicetests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import trainticket.dtos.CreateTicketCommand;
import trainticket.dtos.PassengerDto;
import trainticket.dtos.TicketDto;
import trainticket.exceptions.TicketNotFoundException;
import trainticket.model.*;
import trainticket.repositories.DiscountRepository;
import trainticket.repositories.PassengerRepository;
import trainticket.repositories.TicketRepository;
import trainticket.repositories.TrainRepository;
import trainticket.services.TicketService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TicketServiceIT {

    @Mock
    TicketRepository ticketRepository;

    @Mock
    TrainRepository trainRepository;

    @Mock
    PassengerRepository passengerRepository;

    @Mock
    DiscountRepository discountRepository;

    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    TicketService service;

    Train trainOne;
    Train trainTwo;
    Passenger passengerOne;
    Passenger passengerTwo;
    Discount discount;

    @BeforeEach
    void init() {
        trainOne = new Train(1L, "Fecske", TrainType.PASSENGER_TRAIN, LocalDateTime.now().plusYears(1), "Cegléd",
                LocalDateTime.now().plusYears(1).plusHours(1), "Zugló", 60, new ArrayList<>());
        trainTwo = new Train(2L, "Z56", TrainType.FAST_TRAIN, LocalDateTime.now().plusYears(1), "Szolnok",
                LocalDateTime.now().plusYears(1).plusHours(1).plusMinutes(30), "Budapest", 100, new ArrayList<>());

        passengerOne = new Passenger(1L, "John Doe", LocalDate.of(1990, 1, 1), new ArrayList<>());
        passengerTwo = new Passenger(2L, "Jane Doe", LocalDate.of(2010, 1, 1), new ArrayList<>());

        discount = new Discount(1L, 26, 65, 0, null);
    }

    @Test
    @DisplayName("List all tickets test")
    void testListAllTickets() {
        when(ticketRepository.findAll())
                .thenReturn(
                List.of(
                        new Ticket(1L, 1000, 1000, trainOne, passengerOne, discount),
                        new Ticket(2L, 1000, 1000, trainOne, passengerTwo, discount)
                )
        );

        assertThat(service.listAllTickets())
                .hasSize(2)
                .extracting(TicketDto::getPassenger)
                .extracting(PassengerDto::getName)
                .containsExactly("John Doe", "Jane Doe");

        verify(ticketRepository).findAll();
    }

    @Test
    @DisplayName("Find ticket by id test")
    void testFindTicketById() {
        when(ticketRepository.findById(1L)).thenReturn(
                Optional.of(
                        new Ticket(1L, 1000, 1000, trainOne, passengerOne, discount)
                )
        );

        assertThat(service.findTicketById(1L).getPassenger().getName())
                .isEqualTo("John Doe");

        verify(ticketRepository).findById(1L);
    }

    @Test
    @DisplayName("Find ticket by invalid id test")
    void testFindTicketByIdInvalid() {
        when(ticketRepository.findById(10L)).thenReturn(
                Optional.empty());

        TicketNotFoundException tnfe = assertThrows(TicketNotFoundException.class,
                () -> service.findTicketById(10L));

        assertThat(tnfe.getDetail())
                .isEqualTo("Ticket not found by id: 10");

        verify(ticketRepository).findById(10L);
    }

    @Test
    @DisplayName("Buy ticket test")
    void testBuyTicket() {
        when(trainRepository.findById(1L)).thenReturn(
                Optional.of(trainOne)
        );

        when(passengerRepository.findById(1L)).thenReturn(
                Optional.of(passengerOne)
        );

        int age = Period.between(passengerOne.getDateOfBirth(), LocalDate.now()).getYears();
        when(discountRepository.findDiscountByAge(age)).thenReturn(discount);

        when(ticketRepository.save(any())).thenReturn(
                new Ticket(1L, 1000,1000,trainOne, passengerOne,discount)
        );

        assertThat(service.buyTicket(new CreateTicketCommand(1L,1L)).getFullPrice())
                .isEqualTo(1000);

        verify(trainRepository).findById(1L);
        verify(passengerRepository).findById(1L);
        verify(discountRepository).findDiscountByAge(age);
        verify(ticketRepository).save(any());
    }
}
