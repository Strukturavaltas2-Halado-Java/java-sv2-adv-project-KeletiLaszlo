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
import trainticket.dtos.CreateTrainCommand;
import trainticket.dtos.ModifyTrainCommand;
import trainticket.dtos.TrainDto;
import trainticket.model.Train;
import trainticket.model.TrainType;
import trainticket.repositories.TrainRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Test TrainController")
@Sql(statements = {"DELETE FROM tickets;", "DELETE FROM trains;"})
class TrainControllerIT {


    @Autowired
    WebTestClient webTestClient;

    @Autowired
    TrainRepository trainRepository;

    @Autowired
    ModelMapper modelMapper;

    TrainDto trainOneDto;
    TrainDto trainTwoDto;
    TrainDto trainThreeDto;

    @BeforeEach
    void init() {
        Train trainOne = new Train("Fecske", TrainType.PASSENGER_TRAIN, LocalDateTime.of(2023,1,1,9,0),"Cegléd",
                LocalDateTime.of(2023,1,1,10,0),"Zugló",60);
        Train trainTwo = new Train("Z56", TrainType.FAST_TRAIN, LocalDateTime.of(2023,1,1,9,0),"Szolnok",
                LocalDateTime.of(2023,1,1,10,30),"Budapest",100);
        Train trainThree = new Train("PAVA", TrainType.INTERCITY, LocalDateTime.of(2023,1,1,9,0),"Debrecen",
                LocalDateTime.of(2023,1,1,12,0),"Budapest",221);

        trainOneDto = modelMapper.map(trainRepository.save(trainOne), TrainDto.class);
        trainTwoDto = modelMapper.map(trainRepository.save(trainTwo), TrainDto.class);
        trainThreeDto = modelMapper.map(trainRepository.save(trainThree), TrainDto.class);
    }

    @Test
    @DisplayName("Testing get all trains")
    void testListAllTrains() {
        webTestClient.get()
                .uri("/api/trains")
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(TrainDto.class)
                .hasSize(3)
                .value(t->assertThat(t).extracting(TrainDto::getNameOfTrain).containsExactly("Fecske","Z56","PAVA"));
    }

    @Test
    @DisplayName("Testing get all trains filter by departure place")
    void testListAllTrainsByDeparture() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/trains").queryParam("departurePlace","Szolnok").build())
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(TrainDto.class)
                .hasSize(1)
                .value(t->assertThat(t).extracting(TrainDto::getNameOfTrain).containsExactly("Z56"));
    }

    @Test
    @DisplayName("Testing get all trains filter by arrival place")
    void testListAllTrainsByArrival() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/trains").queryParam("arrivalPlace","Zugló").build())
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(TrainDto.class)
                .hasSize(1)
                .value(t->assertThat(t).extracting(TrainDto::getNameOfTrain).containsExactly("Fecske"));
    }

    @Test
    @DisplayName("Testing get all trains filter by departure time")
    void testListAllTrainsByDepartureTime() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/trains").queryParam("departureTime","2023-01-01T08:00").build())
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(TrainDto.class)
                .hasSize(3)
                .value(t->assertThat(t).extracting(TrainDto::getNameOfTrain).containsExactly("Fecske","Z56","PAVA"));
    }

    @Test
    @DisplayName("Testing get all trains filter by departure place and arrival place")
    void testListAllTrainsByDepartureAndArrival() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/trains").queryParam("departurePlace","Szolnok").queryParam("arrivalPlace","Budapest").build())
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(TrainDto.class)
                .hasSize(1)
                .value(t->assertThat(t).extracting(TrainDto::getNameOfTrain).containsExactly("Z56"));
    }

    @Test
    @DisplayName("Testing get all trains filter by departure place, arrival place and departure time")
    void testListAllTrainsByDeparturePlaceAndTimeAndArrival() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/trains").queryParam("departurePlace","Cegléd").queryParam("arrivalPlace","Zugló")
                        .queryParam("departureTime","2023-01-01T08:00").build())
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(TrainDto.class)
                .hasSize(1)
                .value(t->assertThat(t).extracting(TrainDto::getNameOfTrain).containsExactly("Fecske"));
    }

    @Test
    @DisplayName("Testing get all trains filter by departure place and departure time")
    void testListAllTrainsByDepartureAndDepartureTime() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/trains").queryParam("departurePlace","Szolnok").queryParam("departureTime","2023-01-01T08:00").build())
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(TrainDto.class)
                .hasSize(1)
                .value(t->assertThat(t).extracting(TrainDto::getNameOfTrain).containsExactly("Z56"));
    }

    @Test
    @DisplayName("Testing get all trains filter by departure place and departure time")
    void testListAllTrainsByArrivalAndDepartureTime() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/trains").queryParam("arrivalPlace","Budapest").queryParam("departureTime","2023-01-01T08:00").build())
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(TrainDto.class)
                .hasSize(2)
                .value(t->assertThat(t).extracting(TrainDto::getNameOfTrain).containsExactly("Z56","PAVA"));
    }

    @Test
    @DisplayName("Testing get train by ID")
    void testGetTrainById() {
        TrainDto result = webTestClient.get()
                .uri("/api/trains/{id}", trainOneDto.getId())
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(TrainDto.class)
                .returnResult().getResponseBody();

        assert result != null;
        assertThat(result.getNameOfTrain()).isEqualTo(trainOneDto.getNameOfTrain());
    }

    @Test
    @DisplayName("Testing get train by ID with invalid value")
    void testGetTrainByInvalidId() {
        Problem problem = webTestClient.get()
                .uri("/api/trains/{id}",-1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(Problem.class)
                .returnResult().getResponseBody();

        assert problem != null;
        assertThat(problem.getDetail()).isEqualTo("Train not found by id: -1");
    }

    @Test
    @DisplayName("Test create new train")
    void testCreateNewTrain() {
        CreateTrainCommand command = new CreateTrainCommand(
                "Test train", TrainType.PASSENGER_TRAIN,LocalDateTime.now().plusDays(1),"Vác",
                LocalDateTime.now().plusDays(1).plusMinutes(40),"Budapest",34
        );

        webTestClient.post()
                .uri("/api/trains")
                .bodyValue(command)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TrainDto.class)
                .value(t->assertThat(t.getNameOfTrain()).isEqualTo(command.getNameOfTrain()));
    }

    @Test
    @DisplayName("Test create new train with invalid name")
    void testCreateNewTrainInvalidName() {
        CreateTrainCommand command = new CreateTrainCommand(
                "  ", TrainType.PASSENGER_TRAIN,LocalDateTime.now().plusDays(1),"Vác",
                LocalDateTime.now().plusDays(1).plusMinutes(40),"Budapest",34
        );

        ConstraintViolationProblem cvp =
        webTestClient.post()
                .uri("/api/trains")
                .bodyValue(command)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ConstraintViolationProblem.class)
                .returnResult().getResponseBody();

        assert cvp != null;
        assertThat(cvp.getViolations().get(0).getMessage()).isEqualTo("Name cannot be empty");
    }

    @Test
    @DisplayName("Test create new train with invalid trainType")
    void testCreateNewTrainInvalidTrainType() {
        CreateTrainCommand command = new CreateTrainCommand(
                "Test Train", null ,LocalDateTime.now().plusDays(1),"Vác",
                LocalDateTime.now().plusDays(1).plusMinutes(40),"Budapest",34
        );

        ConstraintViolationProblem cvp =
                webTestClient.post()
                        .uri("/api/trains")
                        .bodyValue(command)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody(ConstraintViolationProblem.class)
                        .returnResult().getResponseBody();

        assert cvp != null;
        assertThat(cvp.getViolations().get(0).getMessage()).isEqualTo("Train type cannot be empty");
    }

    @Test
    @DisplayName("Test create new train with departure time is null")
    void testCreateNewTrainDepartureTimeNull() {
        CreateTrainCommand command = new CreateTrainCommand(
                "Test Train", TrainType.PASSENGER_TRAIN ,null,"Vác",
                LocalDateTime.now().plusDays(1).plusMinutes(40),"Budapest",34
        );

        ConstraintViolationProblem cvp =
                webTestClient.post()
                        .uri("/api/trains")
                        .bodyValue(command)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody(ConstraintViolationProblem.class)
                        .returnResult().getResponseBody();

        assert cvp != null;
        assertThat(cvp.getViolations().get(0).getMessage()).isEqualTo("Departure time cannot be null");
    }

    @Test
    @DisplayName("Test create new train with departure time is in the past")
    void testCreateNewTrainDepartureTimePast() {
        CreateTrainCommand command = new CreateTrainCommand(
                "Test Train", TrainType.PASSENGER_TRAIN ,LocalDateTime.now().minusDays(1),"Vác",
                LocalDateTime.now().plusDays(1).plusMinutes(40),"Budapest",34
        );

        ConstraintViolationProblem cvp =
                webTestClient.post()
                        .uri("/api/trains")
                        .bodyValue(command)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody(ConstraintViolationProblem.class)
                        .returnResult().getResponseBody();

        assert cvp != null;
        assertThat(cvp.getViolations().get(0).getMessage()).isEqualTo("Departure time cannot be int he past");
    }

    @Test
    @DisplayName("Test create new train with empty departure place")
    void testCreateNewTrainInvalidDeparture() {
        CreateTrainCommand command = new CreateTrainCommand(
                "Test Train", TrainType.PASSENGER_TRAIN,LocalDateTime.now().plusDays(1),"  ",
                LocalDateTime.now().plusDays(1).plusMinutes(40),"Budapest",34
        );

        ConstraintViolationProblem cvp =
                webTestClient.post()
                        .uri("/api/trains")
                        .bodyValue(command)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody(ConstraintViolationProblem.class)
                        .returnResult().getResponseBody();

        assert cvp != null;
        assertThat(cvp.getViolations().get(0).getMessage()).isEqualTo("Departure place cannot be empty");
    }

    @Test
    @DisplayName("Test create new train with arrival time is null")
    void testCreateNewTrainArrivalTimeNull() {
        CreateTrainCommand command = new CreateTrainCommand(
                "Test Train", TrainType.PASSENGER_TRAIN ,LocalDateTime.now().plusDays(1),"Vác",
                null,"Budapest",34
        );

        ConstraintViolationProblem cvp =
                webTestClient.post()
                        .uri("/api/trains")
                        .bodyValue(command)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody(ConstraintViolationProblem.class)
                        .returnResult().getResponseBody();

        assert cvp != null;
        assertThat(cvp.getViolations().get(0).getMessage()).isEqualTo("Arrival time cannot be null");
    }

    @Test
    @DisplayName("Test create new train with arrival time is in the past")
    void testCreateNewTrainArrivalTimePast() {
        CreateTrainCommand command = new CreateTrainCommand(
                "Test Train", TrainType.PASSENGER_TRAIN ,LocalDateTime.now().plusDays(1),"Vác",
                LocalDateTime.now().minusDays(1),"Budapest",34
        );

        ConstraintViolationProblem cvp =
                webTestClient.post()
                        .uri("/api/trains")
                        .bodyValue(command)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody(ConstraintViolationProblem.class)
                        .returnResult().getResponseBody();

        assert cvp != null;
        assertThat(cvp.getViolations().get(0).getMessage()).isEqualTo("Arrival time cannot be int he past");
    }

    @Test
    @DisplayName("Test create new train with empty arrival place")
    void testCreateNewTrainInvalidArrival() {
        CreateTrainCommand command = new CreateTrainCommand(
                "Test Train", TrainType.PASSENGER_TRAIN,LocalDateTime.now().plusDays(1),"Vác",
                LocalDateTime.now().plusDays(1).plusMinutes(40),"  ",34
        );

        ConstraintViolationProblem cvp =
                webTestClient.post()
                        .uri("/api/trains")
                        .bodyValue(command)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody(ConstraintViolationProblem.class)
                        .returnResult().getResponseBody();

        assert cvp != null;
        assertThat(cvp.getViolations().get(0).getMessage()).isEqualTo("Arrival place cannot be empty");
    }

    @Test
    @DisplayName("Test create new train with negative distance")
    void testCreateNewTrainInvalidDistance() {
        CreateTrainCommand command = new CreateTrainCommand(
                "Test Train", TrainType.PASSENGER_TRAIN,LocalDateTime.now().plusDays(1),"Vác",
                LocalDateTime.now().plusDays(1).plusMinutes(40),"Budapest",-34
        );

        ConstraintViolationProblem cvp =
                webTestClient.post()
                        .uri("/api/trains")
                        .bodyValue(command)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody(ConstraintViolationProblem.class)
                        .returnResult().getResponseBody();

        assert cvp != null;
        assertThat(cvp.getViolations().get(0).getMessage()).isEqualTo("Distance cannot be negative");
    }

    @Test
    @DisplayName("Test create new train with invalid times")
    void testCreateNewTrainInvalidTimes() {
        CreateTrainCommand command = new CreateTrainCommand(
                "Test Train", TrainType.PASSENGER_TRAIN,LocalDateTime.now().plusDays(1).plusHours(2),"Vác",
                LocalDateTime.now().plusDays(1).plusHours(1),"Budapest",34
        );

        Problem problem =
                webTestClient.post()
                        .uri("/api/trains")
                        .bodyValue(command)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody(Problem.class)
                        .returnResult().getResponseBody();


        assert problem != null;
        assertThat(problem.getTitle()).isEqualTo("Invalid times given.");
    }

    @Test
    @DisplayName("Update an existing train")
    void testModifyTrain() {
        ModifyTrainCommand command = new ModifyTrainCommand(
                "Test Train", TrainType.FAST_TRAIN, LocalDateTime.of(2023,1,1,9,0),"Szolnok",
                LocalDateTime.of(2023,1,1,10,30),"Budapest",100
        );
        webTestClient.put()
                .uri("/api/trains/{id}", trainTwoDto.getId())
                .bodyValue(command)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(TrainDto.class)
                .value(t->assertThat(t.getNameOfTrain()).isEqualTo("Test Train"));

        TrainDto result =
                webTestClient.get()
                        .uri("/api/trains/{id}", trainTwoDto.getId())
                        .exchange()
                        .expectBody(TrainDto.class)
                        .returnResult().getResponseBody();


        assert result != null;
        assertThat(result.getNameOfTrain()).isEqualTo("Test Train");
    }

    @Test
    @DisplayName("Delete train by id")
    void testDeleteTrainById() {
        webTestClient.delete()
                .uri("/api/trains/{id}",trainThreeDto.getId())
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri("/api/trains")
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(TrainDto.class)
                .hasSize(2)
                .value(t-> assertThat(t).extracting(TrainDto::getNameOfTrain).containsExactly("Fecske","Z56"));
    }

    @Test
    @DisplayName("Delete train by invalid id")
    void testDeleteTrainByInvalidId() {
        webTestClient.delete()
                .uri("/api/trains/{id}",-1)
                .exchange()
                .expectStatus().isNotFound()
                        .expectBody(Problem.class)
                                .value(p->assertThat(p.getDetail()).isEqualTo("Train not found by id: -1"));
    }

    @Test
    @DisplayName("Delete all trains")
    void testDeleteAllTrains() {
        webTestClient.delete()
                .uri("/api/trains")
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri("/api/trains")
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(TrainDto.class)
                .hasSize(0);
    }
}
