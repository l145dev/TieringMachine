package be.odoo.tieringmachine.web;

import be.odoo.tieringmachine.domain.Citizen;
import be.odoo.tieringmachine.repository.CitizenRepository;
import be.odoo.tieringmachine.service.AuthService;
import be.odoo.tieringmachine.service.RankingService;
import be.odoo.tieringmachine.service.SocialCreditService;
import be.odoo.tieringmachine.web.dto.LeaderboardDTO;
import be.odoo.tieringmachine.web.dto.LoginRequest;
import be.odoo.tieringmachine.web.dto.LoginResponse;
import be.odoo.tieringmachine.web.dto.PointsChangeRequest;
import be.odoo.tieringmachine.web.dto.PointsChangeResponse;
import be.odoo.tieringmachine.web.dto.TierResponse;
import java.util.Optional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Citizen", description = "Citizen management and social credit operations")
@RequiredArgsConstructor
public class CitizenController {

    private final CitizenRepository citizenRepository;
    private final SocialCreditService socialCreditService;
    private final AuthService authService;
    private final RankingService rankingService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate a citizen with username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Citizen citizen = authService.login(request.getCitizenNumber(), request.getPassword());
        if (citizen != null) {
            int rank = rankingService.calculateRank(citizen);

            LoginResponse response = new LoginResponse(
                    citizen.getId(),
                    rank,
                    citizen.getUsername(),
                    citizen.getTotalPoints(),
                    citizen.getTier() != null ? citizen.getTier().getName() : "UNKNOWN");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

    }

    @PostMapping("/report")
    @Operation(summary = "Report Citizen", description = "Report another citizen for suspicious behavior (incentivized snitching)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report submitted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = be.odoo.tieringmachine.web.dto.ReportResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reporter or target not found")
    })
    public ResponseEntity<be.odoo.tieringmachine.web.dto.ReportResponse> reportCitizen(
            @Parameter(description = "Reporter's Citizen ID", required = true) @RequestHeader("X-Citizen-Id") Long reporterId,
            @RequestBody ReportRequest request) {
        return ResponseEntity
                .ok(socialCreditService.reportCitizen(reporterId, request.getTargetId(), request.getReason()));
    }

    @GetMapping("/leaderboard")
    @Operation(summary = "Get Leaderboard", description = "Get the global leaderboard ranking all citizens by total points")
    @ApiResponse(responseCode = "200", description = "Leaderboard retrieved successfully")
    public ResponseEntity<List<LeaderboardDTO>> getLeaderboard() {
        List<Citizen> citizens = citizenRepository.findAllByOrderByTotalPointsDesc();
        List<LeaderboardDTO> leaderboard = IntStream.range(0, citizens.size())
                .mapToObj(i -> new LeaderboardDTO(i + 1, citizens.get(i)))
                .toList();
        return ResponseEntity.ok(leaderboard);
    }

    @GetMapping("/tier")
    @Operation(summary = "Get Tier by Username", description = "Get the tier classification of a citizen by their username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tier retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TierResponse.class))),
            @ApiResponse(responseCode = "200", description = "User not found (returns null tier)")
    })
    public ResponseEntity<TierResponse> getTierByUsername(
            @Parameter(description = "Username of the citizen", required = true) @RequestParam String username) {
        Optional<Citizen> citizen = citizenRepository.findByUsername(username);
        String tierName = citizen
                .map(Citizen::getTier)
                .map(tier -> tier.getName().toLowerCase())
                .orElse(null);
        return ResponseEntity.ok(new TierResponse(tierName));
    }

    @PostMapping("/points")
    @Operation(summary = "Adjust Citizen Points", description = "Manually adjust a citizen's social credit points")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Points adjusted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PointsChangeResponse.class))),
            @ApiResponse(responseCode = "404", description = "Citizen not found")
    })
    public ResponseEntity<PointsChangeResponse> adjustPoints(@RequestBody PointsChangeRequest request) {
        Citizen citizen = socialCreditService.adjustPoints(request.getCitizenId(), request.getPointsChange());

        PointsChangeResponse response = new PointsChangeResponse(
                citizen.getId(),
                citizen.getUsername(),
                citizen.getTotalPoints(),
                request.getPointsChange());

        return ResponseEntity.ok(response);
    }

    public static class ReportRequest {
        private Long targetId;
        private String reason;

        public Long getTargetId() {
            return targetId;
        }

        public void setTargetId(Long targetId) {
            this.targetId = targetId;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
