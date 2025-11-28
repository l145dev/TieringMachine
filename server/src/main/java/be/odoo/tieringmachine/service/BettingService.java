package be.odoo.tieringmachine.service;

import be.odoo.tieringmachine.domain.Bet;
import be.odoo.tieringmachine.repository.BetRepository;
import be.odoo.tieringmachine.web.dto.BetDTO;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BettingService {

    private final BetRepository betRepository;

    public List<BetDTO> getAllBets() {
        return betRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Bet createBet(Bet bet) {
        return betRepository.save(bet);
    }

    private BetDTO convertToDTO(Bet bet) {
        BetDTO dto = new BetDTO();
        dto.setId(bet.getId());
        dto.setDescription(bet.getBetDetails());
        dto.setCreator(bet.getCreator() != null ? bet.getCreator().getUsername() : "Unknown");
        dto.setTarget(bet.getTarget() != null ? bet.getTarget().getUsername() : "Unknown");
        dto.setWagerPoints(bet.getWagerPoints());
        dto.setPayoutPoints(bet.getPayoutPoints());
        dto.setLossPoints(bet.getLossPoints());
        dto.setActualOutcome(bet.getActualOutcome());
        dto.setTime(formatTimeAgo(bet.getResolutionDate()));

        return dto;
    }

    private String formatTimeAgo(LocalDate resolutionDate) {
        if (resolutionDate == null) {
            return null;
        }

        long daysBetween = ChronoUnit.DAYS.between(resolutionDate, LocalDate.now());

        if (daysBetween < 0) {
            daysBetween = Math.abs(daysBetween);
        }

        if (daysBetween < 30) {
            return daysBetween + "d";
        } else if (daysBetween < 365) {
            long months = daysBetween / 30;
            return months + "m";
        } else {
            long years = daysBetween / 365;
            return years + "y";
        }
    }
}
