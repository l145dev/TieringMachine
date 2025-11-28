package be.odoo.tieringmachine.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {

    private String target;

    @JsonProperty("target_points_lost")
    private int targetPointsLost;

    private String reporter;

    @JsonProperty("report_points_earned")
    private int reportPointsEarned;
}
