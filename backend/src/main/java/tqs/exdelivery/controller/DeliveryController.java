package tqs.exdelivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tqs.exdelivery.entity.Delivery;
import tqs.exdelivery.pojo.DeliveryPOJO;
import tqs.exdelivery.service.DeliveryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class DeliveryController {

  @Autowired private DeliveryService service;

  @PostMapping("/deliveries")
  public ResponseEntity<Delivery> assignDelivery(@Valid @RequestBody DeliveryPOJO deliveryPOJO) {
    Delivery delivery = service.assignDelivery(deliveryPOJO);
    if (delivery == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Already exists a delivery for that purchase.");
    }
    return ResponseEntity.ok().body(delivery);
  }
}
