package be.odoo.tieringmachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TieringMachineApplication {

    public static void main(String[] args) {
        SpringApplication.run(TieringMachineApplication.class, args);
    }

}
