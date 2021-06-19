package tqs.exdelivery.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.pojo.DeliveryPOJO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DeliveryControllerIT {

  private final String DELIVERY_HOST = "http:localhost:8080/";

  @Autowired private MockMvc mvc;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mvc);
  }

  @Test
  @WithMockUser(value = "leandro@gmail.com")
  @Order(1)
  void whenGetAllDeliveries_thenReturnAllDeliveries() {
    RestAssuredMockMvc.given()
            .header("Content-Type", "application/json")
            .get("api/v1/deliveries?page=0&recent=true")
            .then()
            .assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .and()
            .body("$.size()", is(5));
  }

  @Test
  @WithMockUser(value = "tiago@gmail.com")
  @Order(2)
  void whenGetMyDeliveriesDescending_thenReturnDeliveries() {
    var deliveries = RestAssuredMockMvc.given()
            .header("Content-Type", "application/json")
            .get("api/v1/deliveries/me?page=0&recent=true")
            .then()
            .assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .and()
            .body("$.size()", is(3))
            .extract()
            .jsonPath()
            .getList("", Delivery.class);

    assertThat(deliveries.get(0).getId() > deliveries.get(1).getId()).isTrue();
  }

  @Test
  @WithMockUser(value = "tiago@gmail.com")
  @Order(3)
  void whenGetMyDeliveriesAscending_thenReturnDeliveries() {
    var deliveries = RestAssuredMockMvc.given()
            .header("Content-Type", "application/json")
            .get("api/v1/deliveries/me?page=0&recent=false")
            .then()
            .assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .and()
            .body("$.size()", is(3))
            .extract()
            .jsonPath()
            .getList("", Delivery.class);

    assertThat(deliveries.get(0).getId() < deliveries.get(1).getId()).isTrue();
  }

  @Test
  @WithMockUser(value = "leandro@gmail.com")
  @Order(4)
  void whenGetCourierDeliveries_thenReturnCourierDeliveries() {
    RestAssuredMockMvc.given()
            .header("Content-Type", "application/json")
            .get("api/v1/deliveries?page=0&recent=true&courierEmail=tiago@gmail.com")
            .then()
            .assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .and()
            .body("$.size()", is(3));
  }
  @Test
  @Order(5)
  void whenCreateDeliveryAndAvailableCouriers_thenReturnAssignedDelivery() {
    var delPojo1 = new DeliveryPOJO(DELIVERY_HOST, 10L, 0, 0);
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(delPojo1)
        .post("api/v1/deliveries")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .and()
        .body("purchaseHost", is(delPojo1.getPurchaseHost()))
        .and()
        .body("purchaseId", is(delPojo1.getPurchaseId().intValue()))
        .and()
        .body("lat", is((float) delPojo1.getLat()))
        .and()
        .body("lon", is((float) delPojo1.getLon()))
        .and()
        .body("state", is("assigned"))
        .and()
        .body("courier", is(notNullValue()));

    var delPojo2 = new DeliveryPOJO(DELIVERY_HOST, 11L, 0, 0);
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(delPojo2)
        .post("api/v1/deliveries")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .and()
        .body("state", is("assigned"));

    var delPojo3 = new DeliveryPOJO(DELIVERY_HOST, 12L, 0, 0);
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(delPojo3)
        .post("api/v1/deliveries")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .and()
        .body("state", is("assigned"));
  }

  @Test
  @Order(6)
  void whenCreateDeliveryAndNoAvailableCourier_thenReturnPendingDelivery() {
    var delPojo4 = new DeliveryPOJO(DELIVERY_HOST, 13L, 0, 0);

    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(delPojo4)
        .post("api/v1/deliveries")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .and()
        .body("purchaseHost", is(delPojo4.getPurchaseHost()))
        .and()
        .body("purchaseId", is(delPojo4.getPurchaseId().intValue()))
        .and()
        .body("lat", is((float) delPojo4.getLat()))
        .and()
        .body("lon", is((float) delPojo4.getLon()))
        .and()
        .body("state", is("pending"))
        .and()
        .body("courier", is(nullValue()));
  }

  @Test
  @Order(7)
  void whenCreateAlreadyExistingDelivery_thenReturnError() {
    var delPojo1 = new DeliveryPOJO(DELIVERY_HOST, 10L, 0, 0);

    RestAssuredMockMvc.given()
            .header("Content-Type", "application/json")
            .body(delPojo1)
            .post("api/v1/deliveries")
            .then()
            .assertThat()
            .statusCode(400)
            .and()
            .statusLine("400 Already exists a delivery for that purchase.");
  }
}
