package com.breach.engine;

import com.breach.engine.service.PublicBreachScraperService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BreachEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(BreachEngineApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(PublicBreachScraperService scraperService) {
        return args -> {
            System.out.println(">>> CYPR NAINA ENGINE: Auto-seeding H2 Database with 8.98B+ Historical Breach Disclosures...");
            scraperService.scrapeWikipediaBreaches();
            System.out.println(">>> CYPR NAINA ENGINE: H2 Database initialization complete!");
        };
    }
}
