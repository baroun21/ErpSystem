package com.company.userService.UserService.TenantUtils;

import com.company.erp.erp.entites.TenantContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TenantHibernateFilterAspect {

    @PersistenceContext
    private EntityManager entityManager;

    @Around("@annotation(jakarta.transaction.Transactional)") // OR Spring one (see above)
    public Object enableCompanyFilter(ProceedingJoinPoint pjp) throws Throwable {
        Long companyId = TenantContext.getCompanyId();
        if (companyId != null) {
            Session session = entityManager.unwrap(Session.class);
            session.enableFilter("companyFilter").setParameter("companyId", companyId);
        }
        return pjp.proceed();
    }
}