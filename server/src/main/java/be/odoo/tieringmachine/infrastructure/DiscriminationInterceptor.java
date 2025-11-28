package be.odoo.tieringmachine.infrastructure;

import be.odoo.tieringmachine.domain.Citizen;
import be.odoo.tieringmachine.exception.AccessDeniedException;
import be.odoo.tieringmachine.repository.CitizenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiscriminationInterceptor implements HandlerInterceptor {

    private final CitizenRepository citizenRepository;
    private final Random random = new Random();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String citizenIdHeader = request.getHeader("X-Citizen-Id");

        if (citizenIdHeader == null) {
            log.debug("No X-Citizen-Id header found, skipping discrimination");
            return true;
        }

        log.debug("Processing request with X-Citizen-Id: {}", citizenIdHeader);

        Long citizenId;
        try {
            citizenId = Long.parseLong(citizenIdHeader);
        } catch (NumberFormatException e) {
            return true;
        }

        Optional<Citizen> citizenOpt = citizenRepository.findById(citizenId);
        if (citizenOpt.isEmpty()) {
            log.debug("Citizen ID {} not found in database", citizenId);
            return true;
        }

        Citizen citizen = citizenOpt.get();

        if (citizen.getTier() == null) {
            log.debug("Citizen {} has no tier assigned", citizenId);
            return true;
        }

        String tierName = citizen.getTier().getName().toUpperCase();
        log.info("Applying discrimination for citizen {} with tier: {}", citizen.getUsername(), tierName);

        switch (tierName) {
            case "ELITE":
                log.info("ELITE tier detected - adding VIP headers");
                response.addHeader("X-Status", "VIP");
                response.addHeader("X-VIP-Access", "GRANTED");
                break;
            case "CITIZEN":
                if (random.nextInt(100) < 5) {
                    log.info("CITIZEN tier - applying 1s delay (5% chance triggered)");
                    Thread.sleep(1000);
                } else {
                    log.debug("CITIZEN tier - no delay applied");
                }
                break;
            case "DREG":
                log.info("DREG tier detected - applying discrimination");
                applyDregDiscrimination(response);
                break;
            default:
                log.warn("Unknown tier: {}", tierName);
        }

        return true;
    }

    private void applyDregDiscrimination(HttpServletResponse response) throws Exception {
        long delay = 1000 + random.nextInt(4001);
        log.info("DREG discrimination - applying {}ms delay", delay);
        Thread.sleep(delay);

        int chance = random.nextInt(100);
        log.debug("DREG discrimination chance roll: {}", chance);

        if (chance < 30) {
            log.warn("DREG discrimination - denying access (chance: {})", chance);
            throw new AccessDeniedException("Network bandwidth reserved for valuable citizens.");
        }

        if (chance >= 30 && chance < 50) {
            log.warn("DREG discrimination - service unavailable (chance: {})", chance);
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,
                    "Service temporarily unavailable for your tier.");
        }

        log.info("DREG discrimination - degraded service allowed (chance: {})", chance);
        response.addHeader("X-Tier-Status", "DREG - PRIORITY: LOWEST");
    }
}
