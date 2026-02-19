# Spring Boot ↔ Django Finance Service Integration

## Overview

This document describes the **professional, event-driven integration** between the Spring Boot ERP Core service (port 8081) and the Django Finance Service (port 8000).

### Architecture Pattern

```
Spring Boot (Port 8081)              Django Finance Service (Port 8000)
├── Finance Service Client  ←────────► Finance Integration ViewSet
├── Domain Events            ◄────────  Signal Handlers
└── Event Listeners                   └── SpringBootServiceClient
```

## Integration Components

### 1. Spring Boot → Django Communication

**Location**: `erp-core-spring/UserService/src/main/java/com/company/userService/finance/`

#### FinanceServiceClient
REST client that calls Django Finance APIs with:
- ✅ Journal entry creation & posting
- ✅ Customer & invoice management
- ✅ Financial reporting (trial balance, AR balance)
- ✅ Health checks
- ✅ Error handling & timeouts

**Example Usage**:
```java
@Autowired
private FinanceServiceClient financeClient;

// Create journal entry in Django
CreateJournalEntryRequest req = new CreateJournalEntryRequest();
req.setJournalId(jsId);
req.setDescription("Sales transaction");
// ... set other fields

try {
    JournalEntryDTO result = financeClient.createJournalEntry(req);
    // Handle result
} catch (FinanceServiceException e) {
    // Handle error gracefully
    log.error("Finance service error", e);
}
```

#### Configuration
**File**: `application.properties`
```properties
finance.service.baseUrl=http://localhost:8000/api
finance.service.apiKey=                          # Optional API key
finance.service.connectTimeout=5000              # Connect timeout
finance.service.readTimeout=10000                # Read timeout
finance.service.enabled=true                     # Enable/disable integration
```

### 2. Django → Spring Boot Communication

**Location**: `finance-service-django/finance/integration/`

#### SpringBootServiceClient
Python REST client for async communication with Spring Boot:
- ✅ Event publishing
- ✅ User/employee data fetching
- ✅ Health checks
- ✅ Resilient error handling

**Example Usage**:
```python
from finance.integration import SpringBootServiceClient, publish_invoice_posted

# Publish event when invoice is posted
def post_invoice(invoice):
    invoice.posted = True
    invoice.save()
    
    # This triggers signal → publishes event to Spring Boot
    publish_invoice_posted(invoice)
```

#### Configuration
**File**: `config/settings.py`
```python
SPRING_BOOT_BASE_URL = 'http://localhost:8081'
SPRING_BOOT_API_KEY = None                       # Optional API key
SPRING_BOOT_CONNECT_TIMEOUT = 5                  # seconds
SPRING_BOOT_READ_TIMEOUT = 10                    # seconds
SPRING_BOOT_ENABLED = True
```

### 3. Event-Driven Architecture

#### Publishing Events (Django → Spring Boot)

When finance records are created/modified, signals automatically publish events:

```python
# finance/signals.py
@receiver(post_save, sender=Invoice)
def invoice_posted_signal(sender, instance, created, update_fields, **kwargs):
    if update_fields and 'posted' in update_fields and instance.posted:
        publish_invoice_posted(instance)  # → Spring Boot event
```

**Supported Events**:
- `INVOICE_POSTED` - When invoice is marked posted
- `JOURNAL_ENTRY_POSTED` - When journal entry is posted
- `PAYMENT_RECEIVED` - When customer payment is created
- `BILL_POSTED` - When supplier bill is posted

#### Consuming Events (Spring Boot)

**File**: `FinanceEventListeners.java`

```java
@Component
public class FinanceEventListeners {
    
    @EventListener
    public void onInvoicePosted(InvoicePostedEvent event) {
        // React to invoice posted event
        // Update customer credit, send notifications, etc.
        log.info("Invoice posted: " + event.getInvoiceId());
    }
    
    @EventListener
    public void onJournalEntryPosted(JournalEntryPostedEvent event) {
        // React to journal entry event
        // Update GL balances, compliance checks, etc.
    }
}
```

## API Endpoints

### Django Finance Service Endpoints (Called from Spring Boot)

Base URL: `http://localhost:8000/api/spring/`

#### Create Journal Entry
```
POST /spring/create_journal_entry/
Content-Type: application/json

{
    "company_id": "123e4567-e89b-12d3-a456-426614174000",
    "date": "2024-01-15",
    "reference": "JE-001",
    "description": "Sales transaction",
    "lines": [
        {
            "account_id": "uuid",
            "debit": 1000.00,
            "credit": 0,
            "cost_center_id": "uuid",
            "description": "Sales account"
        }
    ]
}

Response: 201 Created
{
    "id": "uuid",
    "journal_entry_id": "uuid",
    "date": "2024-01-15",
    "posted": false,
    ...
}
```

#### Post Journal Entry
```
POST /spring/journal-entries/{je_id}/post/

Response: 200 OK
{
    "id": "uuid",
    "posted": true,
    ...
}
```

#### Create Customer
```
POST /spring/customers/
Content-Type: application/json

{
    "company_id": "uuid",
    "name": "ABC Corp",
    "customer_code": "CUST-001",
    "email": "contact@abc.com",
    "credit_limit": 50000.00,
    "payment_terms": "NET30"
}

Response: 201 Created
{
    "id": "uuid",
    "customer_code": "CUST-001",
    ...
}
```

#### Create Invoice
```
POST /spring/invoices/
Content-Type: application/json

{
    "company_id": "uuid",
    "customer_id": "uuid",
    "invoice_number": "INV-001",
    "issue_date": "2024-01-15",
    "due_date": "2024-02-15",
    "lines": [
        {
            "description": "Product A",
            "quantity": 10,
            "unit_price": 100.00,
            "account_id": "uuid"
        }
    ]
}

Response: 201 Created
{
    "id": "uuid",
    "invoice_number": "INV-001",
    "total_amount": "1100.00",
    ...
}
```

#### Post Invoice
```
POST /spring/invoices/{invoice_id}/post/

Response: 200 OK
{
    "id": "uuid",
    "posted": true,
    ...
}
```

#### Get Trial Balance
```
GET /spring/trial-balance/?company_id=uuid&as_of_date=2024-01-15

Response: 200 OK
{
    "company_id": "uuid",
    "date": "2024-01-15",
    "data": {
        "total_debits": "10000.00",
        "total_credits": "10000.00",
        "in_balance": true
    }
}
```

#### Get AR Balance
```
GET /spring/customers/{customer_id}/ar-balance/

Response: 200 OK
{
    "customer_id": "uuid",
    "customer_code": "CUST-001",
    "name": "ABC Corp",
    "total_invoiced": "50000.00",
    "total_paid": "30000.00",
    "ar_balance": "20000.00",
    "credit_limit": "50000.00",
    "available_credit": "30000.00"
}
```

### Spring Boot Event Publishing Endpoint (Called from Django)

Base URL: `http://localhost:8081/api`

#### Publish Event
```
POST /api/finance/events
Content-Type: application/json

{
    "event_type": "INVOICE_POSTED",
    "data": {
        "invoice_id": "123e4567-e89b-12d3-a456-426614174000",
        "invoice_number": "INV-001",
        "customer_id": "uuid",
        "company_id": "uuid",
        "total_amount": "1100.00",
        "issued_at": "2024-01-15T10:30:00"
    }
}

Response: 202 Accepted
{
    "status": "accepted",
    "message": "Event queued for processing"
}
```

## Testing & Validation

### Django Integration Tests

Use the management command to test connectivity:

```bash
# Test health check
python manage.py test_spring_integration --health

# Test get user
python manage.py test_spring_integration --user-id=user123

# Test publish event
python manage.py test_spring_integration \
    --event-type=INVOICE_POSTED \
    --event-data='{"invoice_id":"123","amount":"1000"}'
```

### Manual Testing with cURL

```bash
# Test Django health
curl -X GET http://localhost:8000/api/spring/ 

# Create journal entry
curl -X POST http://localhost:8000/api/spring/create_journal_entry/ \
  -H "Content-Type: application/json" \
  -d '{"company_id":"uuid","date":"2024-01-15",...}'

# Get trial balance
curl -X GET "http://localhost:8000/api/spring/trial-balance/?company_id=uuid"

# Publish event to Spring Boot
curl -X POST http://localhost:8081/api/finance/events \
  -H "Content-Type: application/json" \
  -d '{"event_type":"INVOICE_POSTED","data":{...}}'
```

## Error Handling

### HTTP Status Codes

| Status | Meaning | Cause |
|--------|---------|-------|
| 200 | OK | Successful synchronous operation |
| 201 | Created | New resource created |
| 202 | Accepted | Event accepted for async processing |
| 400 | Bad Request | Invalid data or missing required field |
| 404 | Not Found | Resource not found |
| 500 | Server Error | Unexpected server error |
| 503 | Service Unavailable | Dependent service not responding |

### Exception Handling

**Spring Boot**:
```java
try {
    JournalEntryDTO result = financeClient.createJournalEntry(req);
} catch (FinanceServiceException e) {
    log.error("Finance service error: " + e.getMessage());
    // Fallback: queue for batch processing later
}
```

**Django**:
```python
try:
    SpringBootServiceClient.publish_event(event_type, event_data)
except Exception as e:
    logger.error(f"Failed to publish event: {e}")
    # Fallback: store for retry queue
```

## Security Considerations

### Current State (Development)
- ⚠️ No authentication between services
- ⚠️ Both services accessible on localhost

### Production Requirements
- ✅ Implement API key authentication
- ✅ Use mutual TLS (mTLS) for service-to-service communication
- ✅ Implement rate limiting & quotas
- ✅ Add request signing (HMAC)
- ✅ Use private network/VPN for inter-service communication
- ✅ Implement audit logging

### Implementation Steps

#### Add API Key Validation

**Django** (`springboot_integration.py`):
```python
from django.conf import settings
from rest_framework.authentication import TokenAuthentication

class SpringBootIntegrationViewSet(viewsets.ViewSet):
    def initial(self, request, *args, **kwargs):
        api_key = request.headers.get('X-API-Key')
        if api_key != settings.SPRING_BOOT_API_KEY:
            raise PermissionDenied("Invalid API key")
```

**Spring Boot** (`FinanceServiceConfig.java`):
```java
@Configuration
public class FinanceServiceConfig {
    
    @Bean
    public RestTemplate restTemplate(FinanceServiceProperties props) {
        return new RestTemplateBuilder()
            .interceptors((request, body, execution) -> {
                if (props.getApiKey() != null) {
                    request.getHeaders().set("X-API-Key", props.getApiKey());
                }
                return execution.execute(request, body);
            })
            .build();
    }
}
```

## Deployment Architecture

### Local Development
```
Localhost:8081 (Spring Boot)  ← → Localhost:8000 (Django)
```

### Production Containerized
```
docker-compose.yml
├── service: spring-erp-core
│   ├── port: 8081
│   └── env: FINANCE_SERVICE_URL=http://django-finance:8000
├── service: django-finance
│   ├── port: 8000
│   └── env: SPRING_BOOT_URL=http://spring-erp:8081
└── network: erp-network
```

**docker-compose.yml sample**:
```yaml
version: '3.8'

services:
  spring-erp:
    build: ./erp-core-spring
    ports:
      - "8081:8081"
    environment:
      FINANCE_SERVICE_URL: http://django-finance:8000
    depends_on:
      - postgres
    networks:
      - erp-network

  django-finance:
    build: ./finance-service-django
    ports:
      - "8000:8000"
    environment:
      SPRING_BOOT_URL: http://spring-erp:8081
    depends_on:
      - postgres
    networks:
      - erp-network

  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: erp_db
      POSTGRES_PASSWORD: password
    volumes:
      - postgres-data:/var/lib/postgresql/data
    networks:
      - erp-network

volumes:
  postgres-data:

networks:
  erp-network:
    driver: bridge
```

## Monitoring & Logging

### Spring Boot Logging

**application.properties**:
```properties
logging.level.com.company.userService.finance=DEBUG
logging.level.com.company.userService.finance.client=DEBUG
```

### Django Logging

**settings.py**: Already configured with file and console handlers

### Health Checks

Both services expose health endpoints:
- Spring Boot: `GET /actuator/health`
- Django: `GET /api/spring/` (list endpoint)

Use these for Kubernetes/Docker health probes.

## Troubleshooting

| Error | Cause | Solution |
|-------|-------|----------|
| Connection refused | Service not running | Start Django: `python manage.py runserver` |
| 404 on endpoint | Wrong URL path | Check endpoint path in documentation |
| Timeout | Service slow/unresponsive | Check service logs, increase timeout |
| API Key rejected | Wrong/missing key | Verify key in configuration |
| Database lock | Concurrent modifications | Use transaction.on_commit callbacks |

## Future Enhancements

1. **Message Queue** - Replace REST with RabbitMQ/Kafka for async reliability
2. **API Gateway** - Add Kong/Ambassador for rate limiting & authentication
3. **Service Discovery** - Use Consul/Eureka for dynamic service registration
4. **Observability** - Add OpenTelemetry for distributed tracing
5. **Compensation Transactions** - Implement Saga pattern for distributed transactions
6. **API Versioning** - Support API v1, v2, etc. for backward compatibility

---

**Last Updated**: February 2026  
**Status**: ✅ Production-Ready Foundation  
**Maintainers**: Development Team
