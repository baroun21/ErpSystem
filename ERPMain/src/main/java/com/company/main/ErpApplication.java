package com.company.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaRepositories(basePackages ={"com.company.userService.repository",
        "com.company.userService.HrModule.repository"})
@EntityScan(basePackages = "com.company.erp.erp.entites")
@ComponentScan(basePackages = {
        "com.company.main",         // main app module
        "com.company.userService", // your UserService module
        "com.company.erp.erp",       // Model
        "com.company.userService.HrModule"      // Hr module
})
public class ErpApplication {
    public static void main(String[] args) {
        SpringApplication.run(ErpApplication.class, args);
    }
}
