package tqs.exdelivery.pojo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestPOJO {
    private String host;
    private Long purchaseId;
    private int rating;
    private String comment;
}

