package tqs.exdelivery.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class RegisterRequest {

  @NotBlank
  @Size(min = 4, max = 30)
  private String name;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(min = 6, max = 25)
  private String password;

  @NotBlank
  @Min(-180)
  @Max(180)
  private double lon;

  @NotBlank
  @Min(-90)
  @Max(90)
  private double lat;
}
