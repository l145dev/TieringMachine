package be.odoo.tieringmachine.web;

import be.odoo.tieringmachine.domain.Citizen;
import be.odoo.tieringmachine.repository.CitizenRepository;
import be.odoo.tieringmachine.service.BettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bets")
@Tag(name = "Betting", description = "Betting operations on citizen outcomes")
@RequiredArgsConstructor
public class BetController {

    private final BettingService bettingService;
    private final CitizenRepository citizenRepository;
    private final be.odoo.tieringmachine.service.TierService tierService;

    @GetMapping
    @Operation(summary = "Get All Bets", description = "Retrieve all bets in the system")
    @ApiResponse(responseCode = "200", description = "Bets retrieved successfully")
    public ResponseEntity<List<be.odoo.tieringmachine.web.dto.BetDTO>> getAllBets() {
        return ResponseEntity.ok(bettingService.getAllBets());
    }

    @PostMapping("/update-score")
    public ResponseEntity<Void> setScore(@RequestParam Long citizenId, @RequestParam int score) {
        Citizen citizen = citizenRepository.findById(citizenId)
                .orElseThrow(() -> new IllegalArgumentException("Citizen not found"));
        citizen.setTotalPoints(citizen.getTotalPoints() + score);

        // Update tier based on new score
        tierService.updateCitizenTier(citizen);

        citizenRepository.save(citizen);
        return ResponseEntity.ok().build();
    }
}
