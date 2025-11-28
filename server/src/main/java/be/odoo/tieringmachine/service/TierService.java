package be.odoo.tieringmachine.service;

import be.odoo.tieringmachine.domain.Citizen;
import be.odoo.tieringmachine.domain.Tier;
import be.odoo.tieringmachine.repository.TierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TierService {

    private final TierRepository tierRepository;

    private static final int ELITE_THRESHOLD = 50000;
    private static final int CITIZEN_THRESHOLD = 10000;

    @Transactional
    public void updateCitizenTier(Citizen citizen) {
        int points = citizen.getTotalPoints();
        String currentTierName = citizen.getTier() != null ? citizen.getTier().getName() : "NONE";
        String newTierName = determineTierName(points);

        if (!currentTierName.equalsIgnoreCase(newTierName)) {
            Tier newTier = tierRepository.findByName(newTierName)
                    .or(() -> tierRepository.findByName(newTierName.toUpperCase()))
                    .or(() -> tierRepository.findByName(newTierName.toLowerCase()))
                    .orElseGet(() -> {
                        log.warn("Tier '{}' not found in database, creating it", newTierName);
                        Tier tier = new Tier();
                        tier.setName(newTierName.toLowerCase());
                        return tierRepository.save(tier);
                    });

            log.info("Updating citizen {} tier from {} to {} (points: {})",
                    citizen.getUsername(), currentTierName, newTier.getName(), points);

            citizen.setTier(newTier);
        }
    }

    private String determineTierName(int points) {
        if (points >= ELITE_THRESHOLD) {
            return "elite";
        } else if (points >= CITIZEN_THRESHOLD) {
            return "citizen";
        } else {
            return "dreg";
        }
    }

    public String getTierNameForPoints(int points) {
        return determineTierName(points);
    }
}
