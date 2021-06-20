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
import tqs.exdelivery.repository.DeliveryRepository;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class CourierControllerIT {

  @Autowired private MockMvc mvc;
  @Autowired private DeliveryRepository deliveryRepository;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mvc);
  }

  @Test
  @WithMockUser(value = "leandro@gmail.com")
  @Order(1)
  void whenGetAllCouriersWithAdmin_thenReturnAllCouriers() {
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .get("api/v1/couriers?page=0")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .and()
        .body("$.size()", is(4));
  }

  @Test
  @WithMockUser(value = "tiago@gmail.com")
  @Order(2)
  void whenGetAllCouriersWithNotAdmin_thenReturnAllCouriers() {
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .get("api/v1/couriers?page=0")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  @WithMockUser(value = "tiago@gmail.com")
  @Order(3)
  void whenFireCouriersWithNoAdmin_thenReturnError() {
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .put("api/v1/couriers/1")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  @WithMockUser(value = "leandro@gmail.com")
  @Order(4)
  void whenFireCouriersWithInvalidCourier_thenReturnError() {
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .put("api/v1/couriers/-99")
        .then()
        .assertThat()
        .statusCode(400);
  }

  @Test
  @WithMockUser(value = "leandro@gmail.com")
  @Order(5)
  void whenFireCouriers_thenReturnCourier() {
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .put("api/v1/couriers/1")
        .then()
        .assertThat()
        .statusCode(200)
        .body("id", is(1))
        .body("active", is(false));

    var deliverydb = deliveryRepository.findById(3L).orElse(null);
    assertThat(deliverydb).isNotNull();
    assertThat(deliverydb.getCourier().getId()).isNotEqualTo(1);
  }
}
