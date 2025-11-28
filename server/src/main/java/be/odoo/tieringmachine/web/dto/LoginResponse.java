package be.odoo.tieringmachine.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private Long id;
    private int rank;
    private String username;

    @JsonProperty("total_points")
    private int totalPoints;

    private String tier;
}
