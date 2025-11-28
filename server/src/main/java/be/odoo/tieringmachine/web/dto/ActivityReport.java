package be.odoo.tieringmachine.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityReport {

    private String citizenId;
    private String activeWindow;
    private String gazeStatus; // "Looking at Screen" or "Not Looking"
    private boolean mouseMoving;
    private long timestamp;
}