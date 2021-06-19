package tqs.exdelivery.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import tqs.exdelivery.pojo.DeliveryPOJO;

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
    @Order(1)
    void whenCreateDeliveryAndAvailableCouriers_thenReturnAssignedDelivery() {
        var delPojo1 = new DeliveryPOJO(DELIVERY_HOST, 1L, 0, 0);

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

        RestAssuredMockMvc.given()
                .header("Content-Type", "application/json")
                .body(delPojo1)
                .post("api/v1/deliveries")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .and()
                .body("state", is("assigned"));

        RestAssuredMockMvc.given()
                .header("Content-Type", "application/json")
                .body(delPojo1)
                .post("api/v1/deliveries")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .and()
                .body("state", is("assigned"));
    }

    @Test
    @Order(2)
    void whenCreateDeliveryAndNoAvailableCourier_thenReturnPendingDelivery() {
        var delPojo1 = new DeliveryPOJO(DELIVERY_HOST, 1L, 0, 0);

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
                .body("state", is("pending"))
                .and()
                .body("courier", is(nullValue()));
    }
}
