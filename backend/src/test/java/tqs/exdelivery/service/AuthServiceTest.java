package tqs.exdelivery.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import tqs.exdelivery.entity.User;
import tqs.exdelivery.exception.EmailAlreadyInUseException;
import tqs.exdelivery.pojo.JwtAuthenticationResponse;
import tqs.exdelivery.pojo.LoginRequest;
import tqs.exdelivery.pojo.RegisterRequest;
import tqs.exdelivery.repository.CourierRepository;
import tqs.exdelivery.repository.UserRepository;
import tqs.exdelivery.security.JwtTokenProvider;

import javax.security.auth.Subject;
import java.util.Collection;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  LoginRequest valid;
  RegisterRequest validRegister;
  RegisterRequest invalidRegister;
  User registeredUser;

  @Mock(lenient = true)
  private AuthenticationManager authenticationManager;

  @Mock(lenient = true)
  private PasswordEncoder passwordEncoder;

  @Mock(lenient = true)
  private JwtTokenProvider tokenProvider;

  @Mock(lenient = true)
  private UserRepository repository;

  @Mock(lenient = true)
  private CourierRepository courierRepository;

  @InjectMocks private AuthService authService;

  @BeforeEach
  void setUp() {

    valid = new LoginRequest();
    valid.setEmail("valid@test.com");
    valid.setPassword("test");

    validRegister = new RegisterRequest();
    validRegister.setName("Test");
    validRegister.setEmail("valid@test.com");
    validRegister.setPassword("test");

    invalidRegister = new RegisterRequest();
    invalidRegister.setName("Test");
    invalidRegister.setEmail("usedemail@test.com");
    invalidRegister.setPassword("test");

    registeredUser = new User();
    registeredUser.setEmail("valid@test.com");
    registeredUser.setPassword("test");

    Authentication authentication =
        new Authentication() {
          @Override
          public String getName() {
            return null;
          }

          @Override
          public boolean implies(Subject subject) {
            return false;
          }

          @Override
          public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
          }

          @Override
          public Object getCredentials() {
            return null;
          }

          @Override
          public Object getDetails() {
            return null;
          }

          @Override
          public Object getPrincipal() {
            return null;
          }

          @Override
          public boolean isAuthenticated() {
            return false;
          }

          @Override
          public void setAuthenticated(boolean b) throws IllegalArgumentException {}
        };

    when(authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(valid.getEmail(), valid.getPassword())))
        .thenReturn(authentication);

    when(tokenProvider.generateToken(authentication)).thenReturn("valid token");

    when(repository.findByEmail(validRegister.getEmail())).thenReturn(Optional.empty());
    when(repository.save(any())).thenReturn(registeredUser);

    when(repository.findByEmail("usedemail@test.com")).thenReturn(Optional.of(registeredUser));
  }

  @Test
  void whenLoginWithValidCredentials_thenReturnValidToken() {

    JwtAuthenticationResponse response = authService.authenticateUser(valid);

    assertThat(response.getAccessToken()).contains("valid token");
    assertThat(response.getTokenType()).contains("Bearer");

    verify(authenticationManager, VerificationModeFactory.times(1)).authenticate(any());
    verify(tokenProvider, VerificationModeFactory.times(1)).generateToken(any());
  }

  @Test
  void whenRegisterWithValidData_thenReturnSuccess() throws EmailAlreadyInUseException {

    assertThat(authService.registerUser(validRegister)).isEqualTo(registeredUser);

    verify(repository, VerificationModeFactory.times(1)).findByEmail(any());
    verify(repository, VerificationModeFactory.times(1)).save(any());
    verify(courierRepository, VerificationModeFactory.times(1)).save(any());
  }

  @Test
  void whenRegisterWithInvalidData_thenThrowEmailAlreadyInUse() {

    assertThrows(
        EmailAlreadyInUseException.class,
        () -> authService.registerUser(invalidRegister),
        "Expected registerUser to throw, but it didn't");

    verify(repository, VerificationModeFactory.times(1)).findByEmail(any());
  }
}
