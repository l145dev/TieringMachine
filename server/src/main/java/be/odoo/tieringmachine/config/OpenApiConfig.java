package be.odoo.tieringmachine.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI tieringMachineOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Tiering Machine API")
                                                .description(
                                                                "API for the Dystopian Social Credit System - A satirical backend implementing discrimination based on social scores. "
                                                                                +
                                                                                "Users are categorized into tiers (ELITE, CITIZEN, DREG) based on their total points, affecting their service quality.")
                                                .version("v2.0")
                                                .contact(new Contact()
                                                                .name("Tiering Machine Team")
                                                                .url("https://github.com/your-repo/tiering-machine")))
                                .servers(List.of(
                                                new Server()
                                                                .url("http://localhost:8080")
                                                                .description("Local Development Server"),
                                                new Server()
                                                                .url("http://0.0.0.0:8080")
                                                                .description("Network Accessible Server")))
                                .components(new Components()
                                                .addSecuritySchemes("citizenId", new SecurityScheme()
                                                                .type(SecurityScheme.Type.APIKEY)
                                                                .in(SecurityScheme.In.HEADER)
                                                                .name("X-Citizen-Id")
                                                                .description("Citizen ID (Long) for authentication")));
        }
}
