package be.odoo.tieringmachine.web.dto;

import be.odoo.tieringmachine.domain.Citizen;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CitizenDTO {

    private String username;
    private int totalPoints;
    private String tier;
    private int rank;

    public CitizenDTO(Citizen citizen, int rank) {
        this.username = citizen.getUsername();
        this.totalPoints = citizen.getTotalPoints();
        this.tier = citizen.getTier() != null ? citizen.getTier().getName() : "UNKNOWN";
        this.rank = rank;
    }
}
