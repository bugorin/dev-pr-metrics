package com.devprmetrics.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI devPrMetricsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Dev PR Metrics API")
                        .description("API para métricas de Pull Requests e revisores")
                        .version("v1.0.0")
                        .contact(new Contact().name("Dev PR Metrics").email("support@devprmetrics.local"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
