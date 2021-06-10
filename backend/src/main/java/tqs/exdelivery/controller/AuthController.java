package tqs.exdelivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tqs.exdelivery.exception.EmailAlreadyInUseException;
import tqs.exdelivery.pojo.JwtAuthenticationResponse;
import tqs.exdelivery.pojo.LoginRequest;
import tqs.exdelivery.pojo.RegisterRequest;
import tqs.exdelivery.service.AuthService;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

  @Autowired
  private AuthService service;

  @PostMapping("/register")
  public ResponseEntity<JwtAuthenticationResponse> register(@RequestBody RegisterRequest request) {
    try {
      JwtAuthenticationResponse jwt = service.registerUser(request);
      return ResponseEntity.status(HttpStatus.OK).body(jwt);
    } catch (EmailAlreadyInUseException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email is already in use");
    }
  }

  @PostMapping("/login")
  public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginRequest request) {
    try {
      JwtAuthenticationResponse jwt = service.authenticateUser(request);
      return ResponseEntity.status(HttpStatus.OK).body(jwt);
    } catch (RuntimeException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The credentials provided are incorrect");
    }
  }
}
