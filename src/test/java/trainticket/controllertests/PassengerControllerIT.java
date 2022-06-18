package trainticket.controllertests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import trainticket.dtos.CreatePassengerCommand;
import trainticket.dtos.ModifyPassengerCommand;
import trainticket.dtos.PassengerDto;
import trainticket.model.Passenger;
import trainticket.repositories.PassengerRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Test PassengerController")
@Sql(statements = {"DELETE FROM tickets;", "DELETE FROM passengers;"})
class PassengerControllerIT {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    ModelMapper modelMapper;

    PassengerDto passengerOneDto;
    PassengerDto passengerTwoDto;
    PassengerDto passengerThreeDto;

    @BeforeEach
    void init() {
        Passenger passengerOne = new Passenger("John Doe", LocalDate.of(1990, 1, 1));
        Passenger passengerTwo = new Passenger("Jane Doe", LocalDate.of(2020, 1, 1));
        Passenger passengerThree = new Passenger("Jack Doe", LocalDate.of(2009, 1, 1));

        passengerOneDto = modelMapper.map(passengerRepository.save(passengerOne), PassengerDto.class);
        passengerTwoDto = modelMapper.map(passengerRepository.save(passengerTwo), PassengerDto.class);
        passengerThreeDto = modelMapper.map(passengerRepository.save(passengerThree), PassengerDto.class);
    }

    @Test
    @DisplayName("List all passengers")
    void testListAllPassengers() {
        webTestClient.get()
                .uri("/api/passengers")
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(PassengerDto.class)
                .hasSize(3)
                .value(t->assertThat(t).extracting(PassengerDto::getName).containsExactly("John Doe", "Jane Doe", "Jack Doe"));
    }

    @Test
    @DisplayName("Find passenger by id")
    void testFindPassengerById() {
        webTestClient.get()
                .uri("/api/passengers/{id}", passengerOneDto.getId())
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(PassengerDto.class)
                .value(t->assertThat(t.getName()).isEqualTo(passengerOneDto.getName()));
    }

    @Test
    @DisplayName("Find passenger by invalid id")
    void testFindPassengerByInvalidId() {
        webTestClient.get()
                .uri("/api/passengers/{id}", -1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Problem.class)
                .value(p->assertThat(p.getDetail()).isEqualTo("Passenger not found by id: -1"));
    }

    @Test
    @DisplayName("Modify an existing passenger")
    void testModifyPassenger() {
        ModifyPassengerCommand command = new ModifyPassengerCommand(
                "Joe Doe",
                LocalDate.of(1990,10,11)
        );

        webTestClient.put()
                .uri("/api/passengers/{id}",passengerTwoDto.getId())
                .bodyValue(command)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(PassengerDto.class)
                .value(p-> assertThat(p.getName()).isEqualTo("Joe Doe"))
                .value(p-> assertThat(p.getDateOfBirth()).isEqualTo(LocalDate.of(1990,10,11)));

        webTestClient.get()
                .uri("/api/passengers/{id}", passengerTwoDto.getId())
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(PassengerDto.class)
                .value(p-> assertThat(p.getName()).isEqualTo("Joe Doe"))
                .value(p-> assertThat(p.getDateOfBirth()).isEqualTo(LocalDate.of(1990,10,11)));
    }

    @Test
    @DisplayName("Modify an existing passenger with blank name")
    void testModifyPassengerInvalidName() {
        ModifyPassengerCommand command = new ModifyPassengerCommand(
                "  ",
                LocalDate.of(1990,10,11)
        );

        webTestClient.put()
                .uri("/api/passengers/{id}",passengerTwoDto.getId())
                .bodyValue(command)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .value(cvp-> assertThat(cvp.getViolations().get(0).getMessage()).isEqualTo("Name cannot be empty"));
    }

    @Test
    @DisplayName("Modify an existing passenger with null birth date")
    void testModifyPassengerNullBirthDate() {
        ModifyPassengerCommand command = new ModifyPassengerCommand(
                "Joe Doe",
                null
        );

        webTestClient.put()
                .uri("/api/passengers/{id}",passengerTwoDto.getId())
                .bodyValue(command)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .value(cvp-> assertThat(cvp.getViolations().get(0).getMessage()).isEqualTo("Birth date cannot be empty"));
    }

    @Test
    @DisplayName("Modify an existing passenger with invalid birth date")
    void testModifyPassengerInvalidBirthDate() {
        ModifyPassengerCommand command = new ModifyPassengerCommand(
                "Joe Doe",
                LocalDate.now().plusDays(1)
        );

        webTestClient.put()
                .uri("/api/passengers/{id}",passengerTwoDto.getId())
                .bodyValue(command)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .value(cvp-> assertThat(cvp.getViolations().get(0).getMessage()).isEqualTo("Birth date cannot be in the future"));
    }

    @Test
    @DisplayName("Modify an existing passenger with invalid id")
    void testModifyPassengerInvalidId() {
        ModifyPassengerCommand command = new ModifyPassengerCommand(
                "Joe Doe",
                LocalDate.of(1980,12,24)
        );

        webTestClient.put()
                .uri("/api/passengers/{id}",-1)
                .bodyValue(command)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Problem.class)
                .value(p-> assertThat(p.getDetail()).isEqualTo("Passenger not found by id: -1"));
    }

    @Test
    @DisplayName("Create new passenger")
    void testCreateNewPassenger() {
        CreatePassengerCommand command = new CreatePassengerCommand(
                "Joe Doe",
                LocalDate.of(1990,10,11)
        );

        webTestClient.post()
                .uri("/api/passengers")
                .bodyValue(command)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PassengerDto.class)
                .value(p-> assertThat(p.getName()).isEqualTo("Joe Doe"));
    }

    @Test
    @DisplayName("Create new passenger with blank name")
    void testCreateNewPassengerInvalidName() {
        CreatePassengerCommand command = new CreatePassengerCommand(
                "  ",
                LocalDate.of(1990,10,11)
        );

        webTestClient.post()
                .uri("/api/passengers")
                .bodyValue(command)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .value(cvp-> assertThat(cvp.getViolations().get(0).getMessage()).isEqualTo("Name cannot be empty"));
    }

    @Test
    @DisplayName("Create new passenger with null birth date")
    void testCreateNewPassengerNullBirthDate() {
        CreatePassengerCommand command = new CreatePassengerCommand(
                "Joe Doe",
                null
        );

        webTestClient.post()
                .uri("/api/passengers")
                .bodyValue(command)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .value(cvp-> assertThat(cvp.getViolations().get(0).getMessage()).isEqualTo("Birth date cannot be empty"));
    }

    @Test
    @DisplayName("Create new passenger with invalid birth date")
    void testCreateNewPassengerInvalidBirthDate() {
        CreatePassengerCommand command = new CreatePassengerCommand(
                "Joe Doe",
                LocalDate.now().plusDays(1)
        );

        webTestClient.post()
                .uri("/api/passengers")
                .bodyValue(command)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .value(cvp-> assertThat(cvp.getViolations().get(0).getMessage()).isEqualTo("Birth date cannot be in the future"));
    }

    @Test
    @DisplayName("Delete passenger by id")
    void testDeletePassengerById() {
        webTestClient.delete()
                .uri("/api/passengers/{id}", passengerTwoDto.getId())
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri("/api/passengers")
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(PassengerDto.class)
                .hasSize(2)
                .value(t->assertThat(t).extracting(PassengerDto::getName).containsExactly("John Doe", "Jack Doe"));
    }

    @Test
    @DisplayName("Delete passenger by invalid id")
    void testDeletePassengerByInvalidId() {
        webTestClient.delete()
                .uri("/api/passengers/{id}", -1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Problem.class)
                .value(p-> assertThat(p.getDetail()).isEqualTo("Passenger not found by id: -1"));
    }

    @Test
    @DisplayName("Delete all passengers")
    void testDeleteAllPassengers() {
        webTestClient.delete()
                .uri("/api/passengers")
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri("/api/passengers")
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(PassengerDto.class)
                .hasSize(0);
    }
}
