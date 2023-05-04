package com.example.frisoerprojektbackend.config;


import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;


//@Configuration annotationen og ApplicationRunner klassen gør at run metoden køre når applikationen starter.
@Configuration
public class StartupDataConfig implements ApplicationRunner {

    // Indsæt autowired her for ønsket funktionalitet.

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("LARS");
    }
}