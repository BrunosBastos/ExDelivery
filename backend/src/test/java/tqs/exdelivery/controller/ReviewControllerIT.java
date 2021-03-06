package tqs.exdelivery.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import tqs.exdelivery.pojo.ReviewPOJO;
import tqs.exdelivery.pojo.ReviewRequestPOJO;
import tqs.exdelivery.repository.CourierRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class ReviewControllerIT {

  ReviewPOJO reviewPOJO;
  ReviewRequestPOJO reviewRequestPOJO;
  ReviewRequestPOJO invalidReviewRequestPOJO;
  ReviewRequestPOJO nonExistentReviewRequestPOJO;
  @Autowired private MockMvc mvc;
  @Autowired private CourierRepository courierRepository;

  @Value("${app.MY_HOST:localhost}")
  private String DELIVERY_HOST;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mvc);
    reviewPOJO = new ReviewPOJO(3, "test");
    String deliveryHost = "http://" + DELIVERY_HOST + ":8080/api/v1/purchases";
    reviewRequestPOJO = new ReviewRequestPOJO(deliveryHost, 4L, 3, "test");
    invalidReviewRequestPOJO = new ReviewRequestPOJO(deliveryHost, 3L, 3, "test");
    nonExistentReviewRequestPOJO = new ReviewRequestPOJO(deliveryHost, 1000L, 3, "test");
  }

  @Test
  @WithMockUser(value = "test")
  @Order(1)
  void whenCreateReviewAndNotDelivered_thenReturnError() {
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(invalidReviewRequestPOJO)
        .post("api/v1/deliveries/reviews")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Could not create review");
  }

  @Test
  @WithMockUser(value = "test")
  @Order(2)
  void whenCreateReviewAndInvalidDelivery_thenReturnError() {
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(nonExistentReviewRequestPOJO)
        .post("api/v1/deliveries/reviews")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Could not create review");
  }

  @Test
  @WithMockUser(value = "test")
  @Order(3)
  void whenCreateReview_thenReturnReview() {

    var rating = courierRepository.findById(2L).get().getReputation();

    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(reviewRequestPOJO)
        .post("api/v1/deliveries/reviews")
        .then()
        .assertThat()
        .contentType(ContentType.JSON)
        .statusCode(200)
        .body("rating", is(reviewPOJO.getRating()))
        .and()
        .body("comment", is(reviewPOJO.getComment()))
        .and()
        .body("delivery.id", is(4));
    ;

    var newRating = courierRepository.findById(2L).get().getReputation();

    assertThat(newRating).isNotEqualTo(rating);
  }

  @Test
  @WithMockUser(value = "test")
  @Order(4)
  void whenCreateReviewAndAlreadyReviewed_thenReturnError() {
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(reviewRequestPOJO)
        .post("api/v1/deliveries/reviews")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Could not create review");
  }

  @Test
  @WithMockUser(value = "test")
  @Order(5)
  void whenGetExistentReview_thenReturnReview() {
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .get("api/v1/deliveries/4/reviews")
        .then()
        .assertThat()
        .contentType(ContentType.JSON)
        .statusCode(200)
        .body("rating", is(reviewPOJO.getRating()))
        .and()
        .body("comment", is(reviewPOJO.getComment()))
        .and()
        .body("delivery.id", is(4));
  }

  @Test
  @WithMockUser(value = "test")
  @Order(6)
  void whenGetNonExistentReview_thenReturnNotFound() {
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .get("api/v1/deliveries/1000/reviews")
        .then()
        .assertThat()
        .statusCode(404)
        .statusLine("404 Review not found");
  }
}
