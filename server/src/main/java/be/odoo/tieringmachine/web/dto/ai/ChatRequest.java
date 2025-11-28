package be.odoo.tieringmachine.web.dto.ai;

import java.util.List;

public record ChatRequest(String model, List<Message> messages, double temperature, boolean stream) {

}
