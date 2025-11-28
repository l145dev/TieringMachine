package be.odoo.tieringmachine.web.dto;

import be.odoo.tieringmachine.domain.Citizen;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LeaderboardDTO {

    private String id;
    private int rank;
    private String name;
    private int points;
    private String tier;

    public LeaderboardDTO(int rank, Citizen citizen) {
        this.id = citizen.getId().toString();
        this.rank = rank;
        this.name = citizen.getUsername();
        this.points = citizen.getTotalPoints();
        this.tier = citizen.getTier() != null ? citizen.getTier().getName().toLowerCase() : null;
    }
}
