package com.company.userService.HrModule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.company.userService.HrModule", // your service
        "com.company.erp.erp"                     // model (entities, requests, responses, mappers)
})
@EntityScan(basePackages = {
        "com.company.erp.erp.entites"             // tells JPA where to find @Entity classes
})
@EnableJpaRepositories(basePackages = {
        "com.company.userService.HrModule.repositories"
})

public class LookUpsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LookUpsApplication.class, args);
    }
}

