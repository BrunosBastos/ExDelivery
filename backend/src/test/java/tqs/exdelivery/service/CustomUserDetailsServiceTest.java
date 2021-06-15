package tqs.exdelivery.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import tqs.exdelivery.entity.User;
import tqs.exdelivery.exception.UserNotFoundException;
import tqs.exdelivery.repository.UserRepository;
import tqs.exdelivery.security.CustomUserDetailsService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

  @Mock(lenient = true)
  private UserRepository repository;

  @InjectMocks private CustomUserDetailsService service;

  @BeforeEach
  void setUp() {
    User user = new User();
    user.setEmail("testValid@email.com");
    user.setPassword("test");

    when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    when(repository.findByEmail("invalid@email.com")).thenReturn(Optional.empty());
    when(repository.findById(1L)).thenReturn(Optional.of(user));
    when(repository.findById(1000L)).thenReturn(Optional.empty());
  }

  @Test
  void whenLoadUserByUsernameWithValidEmail_thenReturnUserDetails() {

    UserDetails user = service.loadUserByUsername("testValid@email.com");

    assertThat(user.getUsername()).isEqualTo("testValid@email.com");
    assertThat(user.getPassword()).isEqualTo("test");
    verify(repository, VerificationModeFactory.times(1)).findByEmail("testValid@email.com");
  }

  @Test
  void whenLoadUserByUsernameWithInvalidEmail_thenThrowUserNotFoundException() {

    assertThrows(
        UserNotFoundException.class,
        () -> service.loadUserByUsername("invalid@email.com"),
        "Expected loadUserByUsername to throw, but it didn't");

    verify(repository, VerificationModeFactory.times(1)).findByEmail("invalid@email.com");
  }

  @Test
  void whenLoadUserByIdWithValidId_thenReturnUserDetails() {

    UserDetails user = service.loadUserById(1);

    assertThat(user.getUsername()).isEqualTo("testValid@email.com");
    assertThat(user.getPassword()).isEqualTo("test");
    verify(repository, VerificationModeFactory.times(1)).findById(1L);
  }

  @Test
  void whenLoadUserByIdWithInvalidId_thenThrowUserNotFoundException() {

    assertThrows(
        UserNotFoundException.class,
        () -> service.loadUserById(1000L),
        "Expected loadUserById to throw, but it didn't");

    verify(repository, VerificationModeFactory.times(1)).findById(1000L);
  }
}
