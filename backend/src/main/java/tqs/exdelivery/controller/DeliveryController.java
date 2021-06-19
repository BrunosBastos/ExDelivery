package tqs.exdelivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.exception.UserNotFoundException;
import tqs.exdelivery.pojo.DeliveryPOJO;
import tqs.exdelivery.repository.UserRepository;
import tqs.exdelivery.service.DeliveryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DeliveryController {

  @Autowired private DeliveryService service;

  @Autowired private UserRepository userRepository;

  @PostMapping("/deliveries")
  public ResponseEntity<Delivery> assignDelivery(@Valid @RequestBody DeliveryPOJO deliveryPOJO) {
    var delivery = service.assignDelivery(deliveryPOJO);
    if (delivery == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Already exists a delivery for that purchase.");
    }
    return ResponseEntity.ok().body(delivery);
  }

  @GetMapping("/deliveries/me")
  public ResponseEntity<List<Delivery>> getMyDeliveries(
      Authentication authentication, @RequestParam int page, @RequestParam boolean recent)
      throws UserNotFoundException {
    var user =
        userRepository
            .findByEmail(authentication.getName())
            .orElseThrow(UserNotFoundException::new);
    if (user.getCourier() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Courier does not exist");
    }

    var deliveries = service.getCourierDeliveries(user.getCourier(), page, recent);

    return ResponseEntity.ok().body(deliveries);
  }

  @GetMapping("/deliveries")
  public ResponseEntity<List<Delivery>> getDeliveries(
      Authentication authentication,
      @RequestParam int page,
      @RequestParam boolean recent,
      @RequestParam(required = false) String courierEmail)
      throws UserNotFoundException {
    var user =
        userRepository
            .findByEmail(authentication.getName())
            .orElseThrow(UserNotFoundException::new);
    if (!user.isSuperUser()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not allowed.");
    }

    List<Delivery> deliveries = service.getDeliveries(courierEmail, page, recent);
    return ResponseEntity.ok().body(deliveries);
  }
}
