package tqs.exdelivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.List;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class DeliveryController {

  private final int PAGE_SIZE = 10;
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
  public ResponseEntity<Page<Delivery>> getMyDeliveries(Authentication authentication, @RequestParam int page, @RequestParam boolean recent) throws UserNotFoundException {
    var user = userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);
    if(user.getCourier() == null) {
      throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST, "Courier does not exist");
    }
    Pageable paging = PageRequest.of(page, PAGE_SIZE, recent ? Sort.by("id").descending() : Sort.by("id").ascending());

    var deliveries = service.getCourierDeliveries(user.getCourier(), paging);
    return ResponseEntity.ok().body(deliveries);
  }

  @GetMapping("/deliveries")
  public ResponseEntity<List<Delivery>> getDeliveries(@Valid @RequestBody DeliveryPOJO deliveryPOJO, Authentication authentication) throws UserNotFoundException{
    var user = userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);
    if (!user.isSuperUser()) {
      throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST, "User does not exist");
    }
    List<Delivery> deliveries = service.getAllDeliveries();
    return ResponseEntity.ok().body(deliveries);
  }
}
