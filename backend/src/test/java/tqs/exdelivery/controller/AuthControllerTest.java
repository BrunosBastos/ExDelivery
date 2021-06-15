package tqs.exdelivery.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tqs.exdelivery.entity.User;
import tqs.exdelivery.exception.EmailAlreadyInUseException;
import tqs.exdelivery.pojo.JwtAuthenticationResponse;
import tqs.exdelivery.pojo.LoginRequest;
import tqs.exdelivery.pojo.RegisterRequest;
import tqs.exdelivery.service.AuthService;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class AuthControllerTest {
    RegisterRequest registerRequest;
    LoginRequest loginRequest;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private AuthService authService;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setPassword("password");
        registerRequest.setEmail("test@email.com");
        registerRequest.setName("Test");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@email.com");
        loginRequest.setPassword("password");

        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    void whenRegisterWithValidData_thenReturnData() throws EmailAlreadyInUseException {

        JwtAuthenticationResponse jwt = setUpResponse();

        when(authService.registerUser(any(RegisterRequest.class))).thenReturn(jwt);

        RestAssured.defaultParser = Parser.JSON;
        RestAssuredMockMvc.given()
                .header("Content-Type", "application/json")
                .body(registerRequest)
                .post("api/v1/register")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .and()
                .body("accessToken", is(jwt.getAccessToken()))
                .and()
                .body("tokenType", is(jwt.getTokenType()))
                .and()
                .body("user.superUser", is(jwt.getUser().isSuperUser()))
                .and()
                .body("user.email", is(jwt.getUser().getEmail()))
                .and()
                .body("user.name", is(jwt.getUser().getName()))
                .and()
                .body("$", not(hasKey("user.password")));

        verify(authService, times(1)).registerUser(any());
    }

   /** @Test
    void whenRegisterWithInvalidData_thenReturnEmailAlreadyInUseException()
            throws EmailAlreadyInUseException {

        when(authService.registerUser(any(RegisterRequest.class)))
                .thenThrow(new EmailAlreadyInUseException());

        RestAssured.defaultParser = Parser.JSON;
        RestAssuredMockMvc.given()
                .header("Content-Type", "application/json")
                .body(registerRequest)
                .post("api/v1/register")
                .then()
                .assertThat()
                .statusCode(400)
                .statusLine("400 This email is already in use");

        verify(authService, times(1)).registerUser(any());
    } **/

    @Test
    void whenLoginWithValidCredentials_thenReturnToken() {

        JwtAuthenticationResponse jwt = setUpResponse();

        when(authService.authenticateUser(any(LoginRequest.class))).thenReturn(jwt);

        RestAssured.defaultParser = Parser.JSON;
        RestAssuredMockMvc.given()
                .header("Content-Type", "application/json")
                .body(loginRequest)
                .post("api/v1/login")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .and()
                .body("accessToken", is(jwt.getAccessToken()))
                .and()
                .body("tokenType", is(jwt.getTokenType()))
                .and()
                .body("user.superUser", is(jwt.getUser().isSuperUser()))
                .and()
                .body("user.email", is(jwt.getUser().getEmail()))
                .and()
                .body("user.name", is(jwt.getUser().getName()))
                .and()
                .body("$", not(hasKey("user.password")));

        verify(authService, times(1)).authenticateUser(any());
    }

    @Test
    void whenLoginWithInvalidCredentials_thenReturnToken() {

        when(authService.authenticateUser(any(LoginRequest.class))).thenThrow(RuntimeException.class);

        RestAssured.defaultParser = Parser.JSON;
        RestAssuredMockMvc.given()
                .header("Content-Type", "application/json")
                .body(loginRequest)
                .post("api/v1/login")
                .then()
                .assertThat()
                .statusCode(401)
                .statusLine("401 The credentials provided are incorrect");

        verify(authService, times(1)).authenticateUser(any());
    }

    JwtAuthenticationResponse setUpResponse() {
        User user = new User();
        user.setEmail("test@email.com");
        user.setPassword("password");
        user.setUserId(1L);
        user.setName("Test");
        user.setSuperUser(true);

        return new JwtAuthenticationResponse("valid token", user);
    }
}

