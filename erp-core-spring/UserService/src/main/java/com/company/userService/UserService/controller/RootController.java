package com.company.userService.UserService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "ERP System Running");
        response.put("version", "1.0.0");
        response.put("endpoints", Map.of(
                "auth", "/api/auth/login, /api/auth/register",
                "employees", "/api/hr/employees",
                "departments", "/api/hr/departments",
                "jobs", "/api/hr/jobs",
                "attendance", "/api/hr/attendance",
                "payroll", "/api/hr/payroll",
                "leave_requests", "/api/hr/leave-requests",
                "bonuses", "/api/hr/bonuses",
                "users", "/api/users",
                "roles", "/api/roles"
        ));
        return ResponseEntity.ok(response);
    }
}
