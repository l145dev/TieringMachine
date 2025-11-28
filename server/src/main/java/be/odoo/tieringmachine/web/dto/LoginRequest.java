package be.odoo.tieringmachine.web.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String citizenNumber;
    private String password;
}
