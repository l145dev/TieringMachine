package be.odoo.tieringmachine.service;

import be.odoo.tieringmachine.domain.Citizen;
import be.odoo.tieringmachine.repository.CitizenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CitizenRepository citizenRepository;

    public Citizen login(String username, String password) {
        return citizenRepository.findByUsername(username)
                .filter(citizen -> citizen.getPasswordHash().equals(password))
                .orElse(null);
    }
}
