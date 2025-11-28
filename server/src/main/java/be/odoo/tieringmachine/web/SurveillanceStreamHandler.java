package be.odoo.tieringmachine.web;

import be.odoo.tieringmachine.web.dto.ActivityReport;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SurveillanceStreamHandler {

    private final SimpMessageSendingOperations messagingTemplate;

    private final Map<String, Long> lastLogTime = new ConcurrentHashMap<>();

    /**
     * Endpoint 1: Video Stream
     * Receives Base64 frames and forwards them to the frontend for live viewing.
     */
    @MessageMapping("/stream/video")
    public void handleVideoStream(@Payload String frameData,
            @Header("citizenNumber") String citizenId) {
        messagingTemplate.convertAndSend("/topic/video/" + citizenId, frameData);
    }

    /**
     * Endpoint 2: Activity Report
     * Receives JSON data about what the user is doing (Window, Gaze, Mouse).
     */
    @MessageMapping("/stream/activity")
    public void handleActivityReport(@Payload ActivityReport report) {
        if (report == null || report.getCitizenId() == null) {
            return;
        }

        String citizenId = report.getCitizenId();
        String window = report.getActiveWindow() != null ? report.getActiveWindow().toLowerCase() : "";
        boolean isDistracted = "Not Looking".equals(report.getGazeStatus());

        if (shouldLog(citizenId)) {
            log.info("🕵️ [SPY] {}: Window='{}', Gaze='{}'",
                    citizenId, report.getActiveWindow(), report.getGazeStatus());
        }

        if (window.contains("game") || window.contains("steam") || window.contains("netflix")) {
            punish(citizenId, 50, "Unauthorized Entertainment (" + report.getActiveWindow() + ")");
        }

        if (isDistracted && (window.contains("chrome") || window.contains("firefox"))) {
            if (Math.random() < 0.1) {
                punish(citizenId, 10, "Lack of Focus Detected");
            }
        }
    }

    private void punish(String citizenId, int points, String reason) {
        log.warn("🚨 PUNISHING {}: -{} pts for '{}'", citizenId, points, reason);

        messagingTemplate.convertAndSend("/topic/alerts/" + citizenId,
                "VIOLATION: " + reason + ". -" + points + " Credits.");
    }

    private boolean shouldLog(String citizenId) {
        long now = System.currentTimeMillis();
        if (now - lastLogTime.getOrDefault(citizenId, 0L) > 2000) {
            lastLogTime.put(citizenId, now);
            return true;
        }
        return false;
    }
}
