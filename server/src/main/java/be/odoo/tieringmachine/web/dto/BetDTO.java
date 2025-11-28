package be.odoo.tieringmachine.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BetDTO {

    private Long id;
    private String description;
    private String creator;
    private String target;
    private int wagerPoints;
    private Integer payoutPoints;
    private Integer lossPoints;
    private Boolean actualOutcome;
    private String time;
}
