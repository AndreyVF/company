package com.company.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class SwaggerConfig {

    private static final String API_TITLE = "Company Project API";
    private static final String API_DESCRIPTION = "Company - API Description";

    @Value("${server.servlet.context-path:/}")
    private String contextPath;

    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .addServersItem(new Server().url(contextPath))
            .info(new Info().title(API_TITLE)
                .description(API_DESCRIPTION)
                .version("1.0.0"));
    }
}
