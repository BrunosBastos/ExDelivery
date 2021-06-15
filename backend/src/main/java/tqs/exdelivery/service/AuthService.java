package tqs.exdelivery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tqs.exdelivery.entity.Courier;
import tqs.exdelivery.entity.User;
import tqs.exdelivery.exception.EmailAlreadyInUseException;
import tqs.exdelivery.pojo.JwtAuthenticationResponse;
import tqs.exdelivery.pojo.LoginRequest;
import tqs.exdelivery.pojo.RegisterRequest;
import tqs.exdelivery.repository.CourierRepository;
import tqs.exdelivery.repository.UserRepository;
import tqs.exdelivery.security.JwtTokenProvider;

import java.util.Optional;

@Service
public class AuthService {

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private UserRepository userRepository;

  @Autowired private CourierRepository courierRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private JwtTokenProvider tokenProvider;

  public JwtAuthenticationResponse authenticateUser(LoginRequest request) {

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = tokenProvider.generateToken(authentication);
    User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();

    return new JwtAuthenticationResponse(jwt);
  }

  public User registerUser(RegisterRequest request) throws EmailAlreadyInUseException {

    Optional<User> dbUser = userRepository.findByEmail(request.getEmail());
    if (dbUser.isPresent()) {
      throw new EmailAlreadyInUseException();
    }

    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    Courier courier = new Courier();
    courier.setLat(request.getLat());
    courier.setLon(request.getLon());
    user = userRepository.save(user);

    courier.setUser(user);
    courierRepository.save(courier);

    return user;
  }
}
