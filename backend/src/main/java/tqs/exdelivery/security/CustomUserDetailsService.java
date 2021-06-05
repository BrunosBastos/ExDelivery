package tqs.exdelivery.security;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import tqs.exdelivery.entity.CustomUserDetails;
import tqs.exdelivery.entity.User;
import tqs.exdelivery.exception.UserNotFoundException;
import tqs.exdelivery.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired private UserRepository userRepository;

  @SneakyThrows
  @Override
  public UserDetails loadUserByUsername(String email) {

    User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

    return new CustomUserDetails(user);
  }

  @SneakyThrows
  public UserDetails loadUserById(long id) {

    User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

    return new CustomUserDetails(user);
  }
}
