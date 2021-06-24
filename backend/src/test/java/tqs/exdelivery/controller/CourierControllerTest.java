package tqs.exdelivery.controller;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import tqs.exdelivery.entity.Courier;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.entity.User;
import tqs.exdelivery.pojo.DeliveryPOJO;
import tqs.exdelivery.repository.UserRepository;
import tqs.exdelivery.service.CourierService;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class CourierControllerTest {

  private final String DELIVERY_HOST = "http:localhost:8080/";
  User notAdmin;
  User admin;
  Courier c1;
  Courier c2;
  Courier c3;
  Delivery d1;
  Delivery d2;
  DeliveryPOJO delPojo1;
  @Autowired private MockMvc mvc;
  @MockBean private CourierService courierService;
  @MockBean private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mvc);

    c1 = new Courier(1L, 5, 0.0, 0.0, null, true);
    c2 = new Courier(2L, 5, 10, 20, null, true);
    c3 = new Courier(3L, 3, 10, 20, null, true);

    d1 = new Delivery(1L, 1L, 0, 0, "delivered", DELIVERY_HOST, c1);
    d2 = new Delivery(2L, 2L, 0, 0, "pending", DELIVERY_HOST, null);

    delPojo1 = new DeliveryPOJO(DELIVERY_HOST, 1L, 0, 0);
    admin = new User();
    admin.setEmail("leandro@gmail.com");
    admin.setSuperUser(true);
    notAdmin = new User();
    notAdmin.setSuperUser(false);
  }

  @Test
  @WithMockUser(value = "tiago@gmail.com")
  void whenGetCouriersAndNotSuperUser_thenReturnError() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(notAdmin));
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .get("api/v1/couriers?page=0")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Not enough permissions for this query.");

    verify(userRepository, times(1)).findByEmail(anyString());
  }

  @Test
  @WithMockUser(value = "leandro@gmail.com")
  void whenGetCouriersAndisSuperUser_thenReturnError() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(admin));
    when(courierService.getCouriers(0)).thenReturn(Arrays.asList(c1, c2, c3));
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .get("api/v1/couriers?page=0")
        .then()
        .assertThat()
        .statusCode(200)
        .body("$.size()", is(3));

    verify(userRepository, times(1)).findByEmail(anyString());
    verify(courierService, times(1)).getCouriers(0);
  }

  @Test
  @WithMockUser(value = "tiago@gmail.com")
  void whenFireCourierAndNotSuperUser_thenReturnError() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(notAdmin));
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .put("api/v1/couriers/1")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Not enough permissions for this query.");

    verify(userRepository, times(1)).findByEmail(anyString());
  }

  @Test
  @WithMockUser(value = "tiago@gmail.com")
  void whenFireCourierAndisSuperUserAndInvalidCourier_thenReturnError() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(admin));
    when(courierService.fireCourier(-99L)).thenReturn(null);
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .put("api/v1/couriers/-99")
        .then()
        .assertThat()
        .statusCode(400)
        .statusLine("400 Couldn't find this courier");

    verify(userRepository, times(1)).findByEmail(anyString());
    verify(courierService, times(1)).fireCourier(-99L);
  }

  @Test
  @WithMockUser(value = "leandro@gmail.com")
  void whenFireCourierAndisSuperUser_thenReturnValidResponse() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(admin));
    c1.setActive(false);
    when(courierService.fireCourier(1L)).thenReturn(c1);
    RestAssuredMockMvc.given()
        .header("Content-Type", "application/json")
        .put("api/v1/couriers/1")
        .then()
        .assertThat()
        .statusCode(200)
        .body("active", is(false))
        .and()
        .body("id", is((int) c1.getId()));

    verify(userRepository, times(1)).findByEmail(anyString());
    verify(courierService, times(1)).fireCourier(1L);
  }
}
