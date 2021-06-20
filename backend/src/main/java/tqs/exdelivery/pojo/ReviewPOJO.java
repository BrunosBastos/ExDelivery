package tqs.exdelivery.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPOJO {

    @Min(value = 0, message = "Rating is a number between 0 and 5")
    @Max(value = 5, message = "Rating is a number between 0 and 5")
    int rating;

    @NotBlank
    @NotEmpty
    String comment;
}
