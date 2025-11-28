package be.odoo.tieringmachine.web.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogDTO {

    private Long id;
    private String username;
    private String details;
    private LocalDateTime logTime;
}
