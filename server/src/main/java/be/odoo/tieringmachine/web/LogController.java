package be.odoo.tieringmachine.web;

import be.odoo.tieringmachine.domain.Log;
import be.odoo.tieringmachine.repository.LogRepository;
import be.odoo.tieringmachine.web.dto.LogDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
@Tag(name = "Logs", description = "User activity logging operations")
@RequiredArgsConstructor
public class LogController {

    private final LogRepository logRepository;

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get User Logs", description = "Retrieve all logs for a specific user")
    @ApiResponse(responseCode = "200", description = "Logs retrieved successfully")
    public ResponseEntity<List<LogDTO>> getUserLogs(
            @Parameter(description = "User ID", required = true) @PathVariable Long userId) {
        List<Log> logs = logRepository.findByUserIdOrderByLogTimeDesc(userId);
        List<LogDTO> logDTOs = logs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(logDTOs);
    }

    private LogDTO convertToDTO(Log log) {
        return new LogDTO(
                log.getId(),
                log.getUser() != null ? log.getUser().getUsername() : "Unknown",
                log.getDetails(),
                log.getLogTime());
    }
}
