package be.odoo.tieringmachine.web.dto.ai;

import java.util.List;

public record ChatResponse(List<Choice> choices) {

}
