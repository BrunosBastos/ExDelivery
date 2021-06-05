package tqs.exdelivery.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tqs.exdelivery.pojo.LoginRequest;
import tqs.exdelivery.pojo.RegisterRequest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerIT {

  @LocalServerPort private int port;

  private static RegisterRequest registerRequest;
  private static LoginRequest validLoginRequest;
  private static LoginRequest invalidLoginRequest;

  private static String baseUrl;

  @BeforeAll
  static void init() {

    baseUrl = "http://127.0.0.1:";

    registerRequest = new RegisterRequest();
    registerRequest.setEmail("test@example.com");
    registerRequest.setPassword("password");
    registerRequest.setName("Test");
    registerRequest.setLat(10);
    registerRequest.setLon(20);

    validLoginRequest = new LoginRequest();
    validLoginRequest.setEmail(registerRequest.getEmail());
    validLoginRequest.setPassword(registerRequest.getPassword());

    invalidLoginRequest = new LoginRequest();
    invalidLoginRequest.setEmail(registerRequest.getEmail());
    invalidLoginRequest.setPassword("invalid password");
  }

  @Test
  @Order(1)
  void whenRegisterWithValidCredentials_thenReturnRegisteredUser() {
    given()
        .when()
        .header("Content-Type", "application/json")
        .and()
        .body(registerRequest)
        .when()
        .post(baseUrl + port + "/api/v1/register")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .and()
        .body("email", is(registerRequest.getEmail()))
        .and()
        .body("password", not(registerRequest.getPassword()))
        .and()
        .body("name", is(registerRequest.getName()));
  }

  @Test
  @Order(2)
  void whenRegisterWithSameEmail_thenReturnBadRequest() {
    given()
        .when()
        .header("Content-Type", "application/json")
        .and()
        .body(registerRequest)
        .when()
        .post(baseUrl + port + "/api/v1/register")
        .then()
        .statusCode(400);
  }

  @Test
  @Order(3)
  @WithMockUser()
  void whenLoginWithValidCredentials_thenReturnToken() {

    given()
        .when()
        .header("Content-Type", "application/json")
        .and()
        .body(validLoginRequest)
        .when()
        .post(baseUrl + port + "/api/v1/login")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .and()
        .body("$", hasKey("accessToken"))
        .and()
        .body("tokenType", is("Bearer"));
  }

  @Test
  @Order(4)
  void whenLoginWithInvalidCredentials_thenReturnUnauthorized() {

    given()
        .when()
        .header("Content-Type", "application/json")
        .and()
        .body(invalidLoginRequest)
        .when()
        .post(baseUrl + port + "/api/v1/login")
        .then()
        .statusCode(401);
  }
}
