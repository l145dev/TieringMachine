package be.odoo.tieringmachine.service;

import be.odoo.tieringmachine.domain.Citizen;
import be.odoo.tieringmachine.exception.CitizenDeportedException;
import be.odoo.tieringmachine.repository.CitizenRepository;
import be.odoo.tieringmachine.web.dto.ReportResponse;
import be.odoo.tieringmachine.web.dto.ai.AiVerdict;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SocialCreditService {

        private final CitizenRepository citizenRepository;
        private final AiJudgeService aiJudgeService;
        private final TierService tierService;

        public SocialCreditService(CitizenRepository citizenRepository, AiJudgeService aiJudgeService,
                        TierService tierService) {
                this.citizenRepository = citizenRepository;
                this.aiJudgeService = aiJudgeService;
                this.tierService = tierService;
        }

        @Transactional
        public ReportResponse reportCitizen(Long reporterId, Long targetId, String reason) {
                Citizen reporter = citizenRepository.findById(reporterId)
                                .orElseThrow(() -> new IllegalArgumentException("Reporter not found"));
                Citizen target = citizenRepository.findById(targetId)
                                .orElseThrow(() -> new IllegalArgumentException("Target not found"));

                AiVerdict verdict = aiJudgeService.judgeReport(reason);

                int pointsEarned = verdict.reporterPoints();
                int pointsChangeForTarget = verdict.targetPoints();

                reporter.setTotalPoints(reporter.getTotalPoints() + pointsEarned);
                target.setTotalPoints(target.getTotalPoints() + pointsChangeForTarget);

                tierService.updateCitizenTier(reporter);
                tierService.updateCitizenTier(target);

                citizenRepository.save(reporter);
                citizenRepository.save(target);

                if (target.getTotalPoints() < 0) {
                        throw new CitizenDeportedException(
                                        "Citizen " + target.getUsername()
                                                        + " has been deported due to insufficient social credit.");
                }

                return new be.odoo.tieringmachine.web.dto.ReportResponse(
                                target.getUsername(),
                                Math.abs(pointsChangeForTarget),
                                reporter.getUsername(),
                                pointsEarned);
        }

        @Transactional
        public Citizen adjustPoints(Long citizenId, int pointsChange) {
                Citizen citizen = citizenRepository.findById(citizenId)
                                .orElseThrow(() -> new IllegalArgumentException("Citizen not found"));

                citizen.setTotalPoints(citizen.getTotalPoints() + pointsChange);

                // Update tier based on new point total
                tierService.updateCitizenTier(citizen);

                return citizenRepository.save(citizen);
        }
}
