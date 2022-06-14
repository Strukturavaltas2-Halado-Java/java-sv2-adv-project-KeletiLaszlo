package trainticket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import trainticket.dtos.CreateTicketCommand;
import trainticket.dtos.PassengerDto;
import trainticket.dtos.TicketDto;
import trainticket.dtos.TrainDto;
import trainticket.model.*;
import trainticket.repositories.DiscountRepository;
import trainticket.repositories.PassengerRepository;
import trainticket.repositories.TicketRepository;
import trainticket.repositories.TrainRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Test TicketController")
@Sql(statements = {"DELETE FROM tickets;", "DELETE FROM trains;", "DELETE FROM passengers;"})
class TicketControllerIT {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    TrainRepository trainRepository;

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    ModelMapper modelMapper;

    TrainDto trainOneDto;
    TrainDto trainTwoDto;

    PassengerDto passengerOneDto;
    PassengerDto passengerTwoDto;

    TicketDto ticketDtoOneOne;
    TicketDto ticketDtoTwoOne;

    @BeforeEach
    void init() {
        Train trainOne = new Train("Fecske", TrainType.PASSENGER_TRAIN, LocalDateTime.now().plusHours(1), "Cegléd",
                LocalDateTime.now().plusHours(2), "Zugló", 60);
        Train trainTwo = new Train("Z56", TrainType.FAST_TRAIN, LocalDateTime.now().plusHours(1), "Szolnok",
                LocalDateTime.now().plusHours(1).plusMinutes(30), "Budapest", 100);
        trainOneDto = modelMapper.map(trainRepository.save(trainOne), TrainDto.class);
        trainTwoDto = modelMapper.map(trainRepository.save(trainTwo), TrainDto.class);

        Passenger passengerOne = new Passenger("John Doe", LocalDate.of(1990, 1, 1));
        Passenger passengerTwo = new Passenger("Jane Doe", LocalDate.of(2020, 1, 1));

        passengerOneDto = modelMapper.map(passengerRepository.save(passengerOne), PassengerDto.class);
        passengerTwoDto = modelMapper.map(passengerRepository.save(passengerTwo), PassengerDto.class);

        Discount discountOne = discountRepository.findDiscountByAge(30);

        Ticket ticketOne = new Ticket(100, 100, trainOne, passengerOne, discountOne);
        Ticket ticketTwo = new Ticket(100, 100, trainTwo, passengerOne, discountOne);

        ticketDtoOneOne = modelMapper.map(ticketRepository.save(ticketOne), TicketDto.class);
        ticketDtoTwoOne = modelMapper.map(ticketRepository.save(ticketTwo), TicketDto.class);
    }

    @Test
    @DisplayName("List all tickets")
    void testListAllTickets() {
        webTestClient.get()
                .uri("/api/tickets")
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(TicketDto.class)
                .hasSize(2)
                .value(t -> assertThat(t).extracting(TicketDto::getPassenger).extracting(PassengerDto::getName).containsExactly("John Doe", "John Doe"));
    }

    @Test
    @DisplayName("Find ticket by id")
    void testFindTicketById() {
        webTestClient.get()
                .uri("/api/tickets/{id}", ticketDtoOneOne.getId())
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(TicketDto.class)
                .value(t -> assertThat(t.getTrain().getDistance()).isEqualTo(60));
    }

    @Test
    @DisplayName("Find ticket by invalid id")
    void testFindTicketByInvalidId() {
        webTestClient.get()
                .uri("/api/tickets/{id}", -1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Problem.class)
                .value(p -> assertThat(p.getDetail()).isEqualTo("Ticket not found by id: -1"));
    }

    @Test
    @DisplayName("Buy ticket")
    void testBuyTicket() {
        webTestClient.post()
                .uri("/api/tickets")
                .bodyValue(new CreateTicketCommand(trainTwoDto.getId(), passengerTwoDto.getId()))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TicketDto.class)
                .value(t-> assertThat(t.getPassenger().getName()).isEqualTo("Jane Doe"));
    }

    @Test
    @DisplayName("Buy ticket with invalid trainId")
    void testBuyTicketInvalidTrainId() {
        webTestClient.post()
                .uri("/api/tickets")
                .bodyValue(new CreateTicketCommand(Long.MAX_VALUE, passengerTwoDto.getId()))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Problem.class)
                .value(p-> assertThat(p.getDetail()).isEqualTo("Train not found by id: "+ Long.MAX_VALUE));
    }

    @Test
    @DisplayName("Buy ticket with invalid passengerId")
    void testBuyTicketInvalidPassengerId() {
        webTestClient.post()
                .uri("/api/tickets")
                .bodyValue(new CreateTicketCommand(trainTwoDto.getId(), Long.MAX_VALUE))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Problem.class)
                .value(p-> assertThat(p.getDetail()).isEqualTo("Passenger not found by id: "+ Long.MAX_VALUE));
    }

    @Test
    @DisplayName("Delete ticket by id")
    void testDeleteTicketById() {
        webTestClient.delete()
                .uri("/api/tickets/{id}", ticketDtoOneOne.getId())
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri("/api/tickets")
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(TicketDto.class)
                .hasSize(1)
                .value(t -> assertThat(t).extracting(TicketDto::getTrain).extracting(TrainDto::getDistance).containsExactly(100));
    }

    @Test
    @DisplayName("Delete ticket by invalid id")
    void testDeleteTicketByInvalidId() {
        webTestClient.delete()
                .uri("/api/tickets/{id}", -1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Problem.class)
                .value(p-> assertThat(p.getDetail()).isEqualTo("Ticket not found by id: -1"));
    }

    @Test
    @DisplayName("Delete all tickets")
    void testDeleteAllTicket() {
        webTestClient.delete()
                .uri("/api/tickets")
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri("/api/tickets")
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(TicketDto.class)
                .hasSize(0);
    }

    @Test
    @DisplayName("Delete train but there is a bought ticket")
    void testDeleteTrainConstraint() {
        webTestClient.delete()
                .uri("/api/trains/{id}", trainOneDto.getId())
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(Problem.class)
                .value(p-> assertThat(p.getDetail()).isEqualTo(String.format("Cannot delete a train (id: %d) as long as someone has a ticket for it.",trainOneDto.getId())));
    }

    @Test
    @DisplayName("Delete all trains but there is a bought ticket")
    void testDeleteAllTrainConstraint() {
        webTestClient.delete()
                .uri("/api/trains")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(Problem.class)
                .value(p-> assertThat(p.getDetail()).isEqualTo("Cannot delete trains as long as someone has a ticket for it."));
    }

    @Test
    @DisplayName("Delete passenger but the passenger has ticket")
    void testDeletePassengerConstraint() {
        webTestClient.delete()
                .uri("/api/passengers/{id}", passengerOneDto.getId())
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(Problem.class)
                .value(p-> assertThat(p.getDetail()).isEqualTo(String.format("Cannot delete a passenger (id: %d) until it has tickets.",passengerOneDto.getId())));
    }

    @Test
    @DisplayName("Delete all passengers but someone has ticket")
    void testDeleteAllPassengerConstraint() {
        webTestClient.delete()
                .uri("/api/passengers")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(Problem.class)
                .value(p-> assertThat(p.getDetail()).isEqualTo("Cannot delete passengers until it has tickets."));
    }
}
