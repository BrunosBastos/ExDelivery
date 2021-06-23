package tqs.exdelivery.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tqs.exdelivery.entity.Courier;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.entity.Review;
import tqs.exdelivery.pojo.ReviewPOJO;
import tqs.exdelivery.pojo.ReviewRequestPOJO;
import tqs.exdelivery.service.ReviewService;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {

  ReviewRequestPOJO reviewRequestPOJO;
  Review review;
  Delivery delivery;
  Courier courier;
  @Autowired private MockMvc mvc;
  @MockBean private ReviewService reviewService;

  @Value("${app.MY_HOST:localhost}")
  private String DELIVERY_HOST;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mvc);
    courier = new Courier();
    courier.setId(2L);
    delivery = new Delivery();
    delivery.setId(1L);
    delivery.setCourier(courier);
    String deliveryHost = "http://" + DELIVERY_HOST + ":8080/api/v1/purchases";
    reviewRequestPOJO = new ReviewRequestPOJO(
            deliveryHost, 4L, 3, "test");
    review = new Review(1L, 3, "test", courier, delivery);
  }

  @Test
  @WithMockUser(value = "test")
  void whenGetExistentReview_thenReturnReview() {
    when(reviewService.getReview(any())).thenReturn(review);
    RestAssuredMockMvc.given()
            .header("Content-Type", "application/json")
            .get("api/v1/deliveries/1/reviews")
            .then()
            .assertThat()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("rating", is(3))
            .and()
            .body("comment", is("test"))
            .and()
            .body("delivery.id", is(delivery.getId().intValue()))
            .and()
            .body("courier.id", is((int) courier.getId()));
  }

  @Test
  @WithMockUser(value = "test")
  void whenGetNonExistentReview_thenReturnNotFound() {
    when(reviewService.getReview(any())).thenReturn(null);
    RestAssuredMockMvc.given()
            .header("Content-Type", "application/json")
            .get("api/v1/deliveries/1/reviews")
            .then()
            .assertThat()
            .statusCode(404)
            .statusLine("404 Review not found");
  }

  @Test
  @WithMockUser(value = "test")
  void whenCreateInvalidReview_thenReturnError() {

    when(reviewService.createReview(any())).thenReturn(null);
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
  void whenCreateValidReview_thenReturnReview() {
    when(reviewService.createReview(any())).thenReturn(review);
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(reviewRequestPOJO)
        .post("api/v1/deliveries/reviews")
        .then()
        .assertThat()
        .statusCode(200)
        .body("rating", is(3))
        .and()
        .body("comment", is("test"))
        .and()
        .body("delivery.id", is(delivery.getId().intValue()))
        .and()
        .body("courier.id", is((int) courier.getId()));
  }
}
