package tqs.exdelivery.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tqs.exdelivery.entity.Courier;
import tqs.exdelivery.exception.UserNotFoundException;
import tqs.exdelivery.repository.UserRepository;
import tqs.exdelivery.service.CourierService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CourierController {

    @Autowired private CourierService service;

    @Autowired private UserRepository userRepository;

    @GetMapping("/couriers")
    public ResponseEntity<List<Courier>> getCouriers(
            Authentication authentication, @RequestParam int page) throws UserNotFoundException {
        var user =
                userRepository
                        .findByEmail(authentication.getName())
                        .orElseThrow(UserNotFoundException::new);
        if (!user.isSuperUser()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough permissions for this query.");
        }
        var couriers = service.getCouriers(page);
        return ResponseEntity.ok().body(couriers);
    }

    @DeleteMapping("/couriers/{id}")
    public ResponseEntity<Courier> fireCourier(
            Authentication authentication, @Valid @PathVariable Long id) throws UserNotFoundException {
        var user =
                userRepository
                        .findByEmail(authentication.getName())
                        .orElseThrow(UserNotFoundException::new);
        if (!user.isSuperUser()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough permissions for this query.");
        }
        var courier = service.fireCourier(id);
        if (courier == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't find this courier");
        }
        return ResponseEntity.ok().body(courier);
    }
}
