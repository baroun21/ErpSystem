package com.company.userService.finance.exception;

public class FinanceServiceException extends RuntimeException {
    
    public FinanceServiceException(String message) {
        super(message);
    }
    
    public FinanceServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
