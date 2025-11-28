package be.odoo.tieringmachine.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointsChangeResponse {

    private Long citizenId;
    private String username;
    private int newTotalPoints;
    private int pointsChanged;
}
