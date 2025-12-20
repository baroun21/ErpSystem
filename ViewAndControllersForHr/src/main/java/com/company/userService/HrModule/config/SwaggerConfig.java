package com.company.userService.HrModule.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // Single OpenAPI bean for general API info
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ERP System & User Service API")
                        .version("1.0.0")
                        .description("API documentation for ERP System (HR, Inventory, Sales Modules) and User Service (Password Reset)")
                        .contact(new Contact()
                                .name("ERP Dev")
                                .email("support@company.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }

    // Optional: group endpoints for User Service module
    @Bean
    public GroupedOpenApi userModuleApi() {
        return GroupedOpenApi.builder()
                .group("User Service")
                .packagesToScan("com.company.userService.UserService")
                .build();
    }

    // Optional: group endpoints for HR Module
    @Bean
    public GroupedOpenApi hrModuleApi() {
        return GroupedOpenApi.builder()
                .group("HR Module")
                .packagesToScan("com.company.userService.HrModule.controller")
                .build();
    }
}
