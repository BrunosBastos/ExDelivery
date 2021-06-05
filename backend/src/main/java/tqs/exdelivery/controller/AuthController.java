package tqs.exdelivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tqs.exdelivery.entity.User;
import tqs.exdelivery.exception.EmailAlreadyInUseException;
import tqs.exdelivery.pojo.JwtAuthenticationResponse;
import tqs.exdelivery.pojo.LoginRequest;
import tqs.exdelivery.pojo.RegisterRequest;
import tqs.exdelivery.service.AuthService;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

  @Autowired private AuthService service;

  @PostMapping("/register")
  public ResponseEntity<User> register(@RequestBody RegisterRequest request) {

    try {
      User user = service.registerUser(request);
      return ResponseEntity.status(HttpStatus.OK).body(user);
    } catch (EmailAlreadyInUseException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginRequest request) {
    JwtAuthenticationResponse jwt = service.authenticateUser(request);
    return ResponseEntity.status(HttpStatus.OK).body(jwt);
  }
}
