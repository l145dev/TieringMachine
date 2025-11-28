package be.odoo.tieringmachine.service;

import be.odoo.tieringmachine.domain.Citizen;
import be.odoo.tieringmachine.repository.CitizenRepository;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final CitizenRepository citizenRepository;

    /**
     * Calculate the rank of a citizen based on their total points.
     * Rank 1 = highest points
     */
    public int calculateRank(Long citizenId) {
        List<Citizen> allCitizens = citizenRepository.findAllByOrderByTotalPointsDesc();
        return IntStream.range(0, allCitizens.size())
                .filter(i -> allCitizens.get(i).getId().equals(citizenId))
                .map(i -> i + 1)
                .findFirst()
                .orElse(0);
    }

    /**
     * Calculate the rank of a citizen based on their total points.
     * Rank 1 = highest points
     */
    public int calculateRank(Citizen citizen) {
        return calculateRank(citizen.getId());
    }
}
