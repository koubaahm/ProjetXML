package fr.univrouen.ProjetXML.configs;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {



    @Bean
    public GroupedOpenApi apiCV24() {
        return GroupedOpenApi.builder()
                .group("CV24 API")
                .packagesToScan("fr.univrouen.ProjetXML.controller")
                .pathsToMatch("/cv24/**", "/acceuil/**", "/auth/**","/help/**")
                .build();
    }


}

