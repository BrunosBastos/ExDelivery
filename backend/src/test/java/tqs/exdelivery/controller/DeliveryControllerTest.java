package tqs.exdelivery.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.pojo.DeliveryPOJO;
import tqs.exdelivery.service.DeliveryService;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class DeliveryControllerTest {
  private static final String HOST_URL = "http://localhost:8080/medex";
  Delivery del1;
  DeliveryPOJO delPojo1;
  @Autowired private MockMvc mvc;
  @MockBean private DeliveryService deliveryService;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mvc);
    del1 = new Delivery(HOST_URL, 1L, 40.5050, 50.0321);
    delPojo1 = new DeliveryPOJO(HOST_URL, 1L, 40.5050, 50.0321);
  }

  @Test
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
}
