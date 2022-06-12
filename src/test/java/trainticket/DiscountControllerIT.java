package trainticket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import trainticket.dtos.DiscountDto;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Test DiscountController")
class DiscountControllerIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @DisplayName("Testing get all operations")
    void testListAllDiscounts() {
        webTestClient.get()
                .uri("/api/discounts")
                .exchange()
                .expectStatus().isAccepted()
                .expectBodyList(DiscountDto.class)
                .hasSize(5)
                .value(d -> assertThat(d).extracting(DiscountDto::getFromAge).containsExactly(0, 6, 14, 26, 65));
    }

    @Test
    @DisplayName("Testing find discount by age between: 0 - 6")
    void findDiscountByAgeZeroToSix() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/discounts/age").queryParam("age", 1).build())
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(DiscountDto.class)
                .value(d -> assertThat(d.getDiscountValue()).isEqualTo(100));
    }

    @Test
    @DisplayName("Testing find discount by age between: 6 - 14")
    void findDiscountByAgeSixToFourteen() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/discounts/age").queryParam("age", 13).build())
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(DiscountDto.class)
                .value(d -> assertThat(d.getDiscountValue()).isEqualTo(50));
    }

    @Test
    @DisplayName("Testing find discount by age between: 14 - 26")
    void findDiscountByAgeFourteenToTwentySix() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/discounts/age").queryParam("age", 14).build())
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(DiscountDto.class)
                .value(d -> assertThat(d.getDiscountValue()).isEqualTo(33));
    }

    @Test
    @DisplayName("Testing find discount by age between: 26 - 65")
    void findDiscountByAgeTwentySixToSixtyFive() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/discounts/age").queryParam("age", 47).build())
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(DiscountDto.class)
                .value(d -> assertThat(d.getDiscountValue()).isZero());
    }

    @Test
    @DisplayName("Testing find discount by age above: 65")
    void findDiscountByAgeAboveSixtyFive() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/discounts/age").queryParam("age", 70).build())
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(DiscountDto.class)
                .value(d -> assertThat(d.getDiscountValue()).isEqualTo(100));
    }

    @Test
    @DisplayName("Testing find discount by age with invalid age")
    void findDiscountByAgeInvalidValue() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/discounts/age").queryParam("age", -10).build())
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("title").isEqualTo("Invalid age given.");
    }

    @Test
    @DisplayName("Testing get discount by ID")
    void testFindDiscountById() {
        DiscountDto discountDto = Objects.requireNonNull(
                webTestClient.get()
                        .uri("/api/discounts")
                        .exchange()
                        .expectBodyList(DiscountDto.class)
                        .returnResult().getResponseBody()).get(0);

        webTestClient.get()
                .uri("/api/discounts/{id}", discountDto.getId())
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(DiscountDto.class)
                .value(d -> assertThat(d).extracting(DiscountDto::getDiscountValue).isEqualTo(discountDto.getDiscountValue()));
    }

    @Test
    @DisplayName("Testing get discount by ID with invalid value")
    void testFindDiscountByIdInvalidValue() {
        webTestClient.get()
                .uri("/api/discounts/{id}", 0)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("title").isEqualTo("Discount not found!");
    }
}
