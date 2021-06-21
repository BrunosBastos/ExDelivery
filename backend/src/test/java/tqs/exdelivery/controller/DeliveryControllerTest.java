package tqs.exdelivery.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tqs.exdelivery.entity.Courier;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.entity.User;
import tqs.exdelivery.pojo.DeliveryPOJO;
import tqs.exdelivery.repository.UserRepository;
import tqs.exdelivery.service.DeliveryService;

import java.net.ConnectException;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class DeliveryControllerTest {
  private static final String HOST_URL = "http://localhost:8080/medex";
  Delivery del1;
  DeliveryPOJO delPojo1;
  User validUser;
  User admin;
  Courier courier;
  @Autowired private MockMvc mvc;
  @MockBean private DeliveryService deliveryService;
  @MockBean private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mvc);
    courier = new Courier();
    courier.setId(1L);
    del1 = new Delivery(HOST_URL, 1L, 40.5050, 50.0321);
    del1.setCourier(courier);
    del1.setId(1L);
    delPojo1 = new DeliveryPOJO(HOST_URL, 1L, 40.5050, 50.0321);

    validUser = new User();
    validUser.setCourier(courier);

    admin = new User();
    admin.setSuperUser(true);
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
    when(deliveryService.getCourierDeliveries(validUser.getCourier(), 0, true))
        .thenReturn(page.getContent());
    when(userRepository.findByEmail(any())).thenReturn(Optional.of(validUser));

    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .get("api/v1/deliveries/me?page=0&recent=true")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .and()
        .body("$.size()", is(1))
        .and()
        .body("[0].courier.id", is((int) validUser.getCourier().getId()));

    verify(deliveryService, times(1)).getCourierDeliveries(validUser.getCourier(), 0, true);
  }

  @Test
  @WithMockUser(value = "test")
  void whenIAmNotCourierAndIGetMyDeliveries_thenReturnError() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(admin));
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .get("api/v1/deliveries/me?page=0&recent=true")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Courier does not exist");
    verify(userRepository, times(1)).findByEmail(anyString());
  }

  @Test
  @WithMockUser(value = "test")
  void whenIAmAdminAndIGetAllDeliveries_thenReturnAllDelivery() {
    when(userRepository.findByEmail(any())).thenReturn(Optional.of(admin));
    when(deliveryService.getDeliveries(null, 0, true)).thenReturn(Arrays.asList(del1));

    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .get("api/v1/deliveries?page=0&recent=true")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .and()
        .body("$.size()", is(1));
    verify(deliveryService, times(1)).getDeliveries(null, 0, true);
    verify(userRepository, times(1)).findByEmail(any());
  }

  @Test
  @WithMockUser(value = "test")
  void whenIAmAdminAndIGetCourierDeliveries_thenReturnAllDelivery() {
    when(userRepository.findByEmail(any())).thenReturn(Optional.of(admin));
    when(deliveryService.getDeliveries("tiago@gmail.com", 0, true)).thenReturn(Arrays.asList(del1));

    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .get("api/v1/deliveries?page=0&recent=true&courierEmail=tiago@gmail.com")
        .then()
        .assertThat()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .and()
        .body("$.size()", is(1));
    verify(deliveryService, times(1)).getDeliveries("tiago@gmail.com", 0, true);
    verify(userRepository, times(1)).findByEmail(any());
  }

  @Test
  @WithMockUser(value = "test")
  void whenIAmNotAdminAndIGetAllDeliveries_thenReturnError() {
    when(userRepository.findByEmail(any())).thenReturn(Optional.of(validUser));
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .get("api/v1/deliveries?page=0&recent=true")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 User not allowed.");
    verify(userRepository, times(1)).findByEmail(any());
  }

  @Test
  @WithMockUser(value = "test")
  void whenIAmNotCourierAndIConfirmDelivery_thenReturnError() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(admin));
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .put("api/v1/deliveries/1")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Courier does not exist");
    verify(userRepository, times(1)).findByEmail(anyString());
  }

  @Test
  @WithMockUser(value = "test")
  void whenICannotConfirmDelivery_thenReturnError() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(validUser));
    when(deliveryService.confirmDelivery(any(), any())).thenReturn(null);
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .put("api/v1/deliveries/1")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Can't confirm this delivery");
    verify(userRepository, times(1)).findByEmail(anyString());
  }

  @Test
  @WithMockUser(value = "test")
  void whenIConfirmDelivery_thenReturnDelivery() throws ConnectException {
    del1.setState("delivered");
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(validUser));
    when(deliveryService.confirmDelivery(any(), any())).thenReturn(del1);
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .put("api/v1/deliveries/1")
        .then()
        .assertThat()
        .statusCode(200)
        .body("state", is("delivered"))
        .and()
        .body("id", is(del1.getId().intValue()))
        .and()
        .body("courier.id", is((int) validUser.getCourier().getId()));
    verify(userRepository, times(1)).findByEmail(anyString());
    verify(deliveryService, times(1)).confirmDelivery(any(), any());
    verify(deliveryService, times(1)).checkDeliveriesToAssign();
    verify(deliveryService, times(1)).notifyHost(any());
  }

  @Test
  @WithMockUser(value = "test")
  void whenGetInvalidDelivery_thenReturnError() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(validUser));
    when(deliveryService.getDelivery(any(), any())).thenReturn(null);
    RestAssuredMockMvc.given()
            .header("Content-Type", "application/json")
            .get("api/v1/deliveries/1")
            .then()
            .assertThat()
            .statusCode(400)
            .statusLine("400 Can't find this delivery");
    verify(userRepository, times(1)).findByEmail(anyString());
    verify(deliveryService, times(1)).getDelivery(any(), any());
  }

  @Test
  @WithMockUser(value = "tiago@gmail.com")
  void whenGetDeliveryAndValidCourier_thenReturnValidResponse() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(validUser));
    when(deliveryService.getDelivery(any(), any())).thenReturn(del1);
    RestAssuredMockMvc.given()
            .header("Content-Type", "application/json")
            .get("api/v1/deliveries/1")
            .then()
            .assertThat()
            .statusCode(200)
            .body("id", is(del1.getId().intValue()))
            .and()
            .body("courier.id", is((int) del1.getCourier().getId()));
    verify(userRepository, times(1)).findByEmail(anyString());
  }

  @Test
  @WithMockUser(value = "leandro@gmail.com")
  void whenGetDeliveryWithAdmin_thenReturnDelivery() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(admin));
    when(deliveryService.getDelivery(any(), any())).thenReturn(del1);
    RestAssuredMockMvc.given()
            .header("Content-Type", "application/json")
            .get("api/v1/deliveries/1")
            .then()
            .assertThat()
            .statusCode(200)
            .body("id", is(del1.getId().intValue()));
    verify(userRepository, times(1)).findByEmail(anyString());
    verify(deliveryService, times(1)).getDelivery(any(), any());

  }


  @Test
  @WithMockUser(value = "leandro@gmail.com")
  void whenGetDeliveryAndInvalidCourier_thenReturnError() {
    courier.setId(99L);
    validUser.setCourier(courier);
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(validUser));
    when(deliveryService.getDelivery(any(), any())).thenReturn(null);
    RestAssuredMockMvc.given()
            .header("Content-Type", "application/json")
            .get("api/v1/deliveries/1")
            .then()
            .assertThat()
            .statusCode(400)
            .statusLine("400 Can't find this delivery");
    verify(userRepository, times(1)).findByEmail(anyString());
    verify(deliveryService, times(1)).getDelivery(any(), any());
  }

}
