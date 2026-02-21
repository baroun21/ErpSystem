package com.company.erp.shared.infrastructure.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configures async processing for @Async annotated methods and @EventListener methods.
 * Provides a thread pool for non-blocking background processing.
 *
 * Usage:
 *   @Async
 *   public void processPayroll() { ... }
 *
 *   @EventListener
 *   @Async
 *   public void onEmployeeHired(EmployeeHiredEvent event) { ... }
 */
@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        log.info("Initializing async thread pool executor");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);           // Min threads
        executor.setMaxPoolSize(20);           // Max threads
        executor.setQueueCapacity(100);        // Queue size before rejection
        executor.setThreadNamePrefix("erp-async-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();

        return executor;
    }
}

