package tqs.exdelivery.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tqs.exdelivery.entity.Courier;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.entity.User;
import tqs.exdelivery.pojo.DeliveryPOJO;
import tqs.exdelivery.repository.UserRepository;
import tqs.exdelivery.service.DeliveryService;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class DeliveryControllerTest {
  private static final String HOST_URL = "http://localhost:8080/medex";
  Delivery del1;
  DeliveryPOJO delPojo1;
  User validCourier;
  User invalidCourier;
  @Autowired private MockMvc mvc;
  @MockBean private DeliveryService deliveryService;
  @MockBean private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mvc);
    Courier courier = new Courier();
    courier.setId(1L);
    del1 = new Delivery(HOST_URL, 1L, 40.5050, 50.0321);
    del1.setCourier(courier);
    delPojo1 = new DeliveryPOJO(HOST_URL, 1L, 40.5050, 50.0321);

    validCourier = new User();
    validCourier.setCourier(courier);

    invalidCourier = new User();

  }

  @Test
  @WithMockUser(value = "test")
  void whenAssignDeliveryWithAnAvailableCourier_thenReturnAssignedDelivery() {
    when(deliveryService.assignDelivery(delPojo1)).thenReturn(del1);

    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .body(delPojo1)
        .post("api/v1/deliveries")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .and()
        .body("purchaseHost", is(del1.getPurchaseHost()))
        .and()
        .body("purchaseId", is(del1.getPurchaseId().intValue()))
        .and()
        .body("lat", is((float) del1.getLat()))
        .and()
        .body("lon", is((float) del1.getLon()));

    verify(deliveryService, times(1)).assignDelivery(delPojo1);
  }

  @Test
  @WithMockUser(value = "test")
  void whenIAmCourierAndIGetMyDeliveries_thenReturnMyDelivery() {
    Page<Delivery> page = new PageImpl<>(Arrays.asList(del1));
    when(deliveryService.getCourierDeliveries(any(), any())).thenReturn(page);
    when(userRepository.findByEmail(any())).thenReturn(Optional.of(validCourier));

    RestAssuredMockMvc.given()
            .header("Content-Type", "application/json")
            .get("api/v1/deliveries/me?page=1&recent=true")
            .then()
            .assertThat()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .and().body("$.size()",is(1))
            .and().body("[0].courier.id", is((int)validCourier.getCourier().getId()));

    verify(deliveryService, times(1)).getCourierDeliveries(any(), any());
  }

  @Test
  @WithMockUser(value = "test")
  void whenIAmNotCourierAndIGetMyDeliveries_thenReturnError() {
    when(userRepository.findByEmail(any())).thenReturn(Optional.of(invalidCourier));
    RestAssuredMockMvc.given()
            .header("Content-Type", "application/json")
            .get("api/v1/deliveries/me?page=1&recent=true")
            .then()
            .assertThat()
            .statusCode(400).statusLine("400 Courier does not exist");
    verify(deliveryService, times(0)).getCourierDeliveries(any(), any());
    verify(userRepository, times(1)).findByEmail(any());

  }

  @Test
  @WithMockUser(value = "test")
  void whenIAmAdminAndIGetAllDeliveries_thenReturnAllDelivery() {

  }

  @Test
  @WithMockUser(value = "test")
  void whenIAmNotAdminAndIGetAllDeliveries_thenReturnError() {

  }
}
