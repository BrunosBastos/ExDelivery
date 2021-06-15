package tqs.exdelivery.pojo;

import lombok.Getter;
import lombok.Setter;
import tqs.exdelivery.entity.User;

@Getter
@Setter
public class JwtAuthenticationResponse {
  private String accessToken;
  private String tokenType = "Bearer";
  private User user;

  public JwtAuthenticationResponse(String accessToken, User user) {
    this.accessToken = accessToken;
    this.user = user;
  }
}
