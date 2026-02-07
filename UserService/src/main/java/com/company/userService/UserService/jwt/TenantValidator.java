//package com.company.userService.UserService.jwt;
//
//import com.company.erp.erp.entites.BaseEntity;
//import com.company.erp.erp.entites.TenantContext;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TenantValidator {
//
//    public void verifyOwnership(BaseEntity entity) {
//        Long currentCompanyId = TenantContext.getCompanyId();
//        if (entity != null && !entity.getCompanyId().equals(currentCompanyId)) {
//            throw new AccessDeniedException("Access Denied: You do not own this resource.");
//        }
//    }
//}