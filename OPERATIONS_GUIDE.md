# ERP System Backend - Operations & Maintenance Guide

**For**: DevOps, System Administrators, Operations Teams  
**Version**: 3.5.5  
**Last Updated**: February 21, 2026

---

## 📊 System Overview

### Production Deployment

```
Client Browser
       ↓
  Load Balancer (nginx/HAProxy)
       ↓
┌─────────────────────────┐
│   ERPMain (Port 8081)   │  × N instances
│  Spring Boot 3.5.5      │    (for HA)
│  Java 21                │
└─────┬────────┬──────────┘
      │        │
      ├─────────┼─ PostgreSQL (Primary DB)
      │        │
      ├─────────┼─ Redis (Session Cache)
      │        │
      └─────────┼─ File Storage (Documents)
```

### Performance Specifications

| Component | Typical | Max |
|-----------|---------|-----|
| Response Time | 200-500ms | <2000ms |
| Throughput | 100 req/sec | 1000 req/sec |
| Memory Usage | 2GB | 4GB |
| Concurrent Users | 50 | 500 |
| Database Connections | 20 | 50 |

---

## 🚀 Deployment

### Docker Deployment

**Create Dockerfile**:
```dockerfile
FROM openjdk:21-slim
WORKDIR /app
COPY ERPMain/target/ERPMain-1.0.0.jar app.jar
ENV JAVA_OPTS="-Xmx2G -Xms1G"
EXPOSE 8081
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

**Build Docker Image**:
```bash
docker build -t erp-system:3.5.5 .
```

**Run Container**:
```bash
docker run -d \
  --name erp-app \
  -p 8081:8081 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/erp_db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=secure_password \
  -e SPRING_REDIS_HOST=redis-cache \
  -e SPRING_REDIS_PORT=6379 \
  -e JWT_SECRET=prod_secret_key \
  -e SERVER_SERVLET_CONTEXT_PATH=/api \
  -m 2G \
  --memory-swap 4G \
  erp-system:3.5.5
```

### Docker Compose Deployment

**File**: `docker-compose.yml`
```yaml
version: '3.8'

services:
  erp-app:
    image: erp-system:3.5.5
    container_name: erp-app
    ports:
      - "8081:8081"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/erp_db
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
      SPRING_REDIS_HOST: redis
      SPRING_REDIS_PORT: 6379
      JWT_SECRET: ${JWT_SECRET_KEY}
      TZ: UTC
    depends_on:
      - postgres
      - redis
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
        reservations:
          cpus: '1'
          memory: 1G
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8081/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s

  postgres:
    image: postgres:14
    container_name: postgres-db
    environment:
      POSTGRES_DB: erp_db
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
    ports:
      - "5432:5432"

  redis:
    image: redis:7
    container_name: redis-cache
    ports:
      - "6379:6379"
    command: redis-server --appendonly yes
    volumes:
      - redis_data:/data

volumes:
  postgres_data:
  redis_data:
```

**Start Services**:
```bash
# Create .env file
echo "DB_PASSWORD=secure_password" > .env
echo "JWT_SECRET_KEY=your_secure_jwt_key" >> .env

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f erp-app
```

---

## 🔍 Monitoring

### Health Checks

**Endpoint**: `GET /actuator/health`

```bash
curl http://localhost:8081/actuator/health | jq .
```

**Response**:
```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "redis": { "status": "UP" },
    "diskSpace": { "status": "UP" }
  }
}
```

### Key Metrics to Monitor

**1. Application Metrics** (`/actuator/metrics`):
```bash
# JVM Memory
curl http://localhost:8081/actuator/metrics/jvm.memory.used | jq .

# HTTP Requests
curl http://localhost:8081/actuator/metrics/http.server.requests | jq .

# Database Connections
curl http://localhost:8081/actuator/metrics/db.connection.active | jq .
```

**2. Database Performance**:
```sql
-- Check active connections
SELECT datname, usename, count(*) 
FROM pg_stat_activity 
GROUP BY datname, usename;

-- Check slow queries (log_min_duration_statement = 1000)
SELECT query, mean_time, calls 
FROM pg_stat_statements 
ORDER BY mean_time DESC 
LIMIT 10;

-- Check table sizes
SELECT schemaname, tablename, pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) 
FROM pg_tables 
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
```

**3. Redis Performance**:
```bash
# Check Redis info
redis-cli INFO server
redis-cli INFO memory
redis-cli INFO stats

# Monitor commands
redis-cli MONITOR
```

### Logging Configuration

**File**: `UserService/src/main/resources/application.properties`

```properties
# Log Levels
logging.level.root=INFO
logging.level.com.company=INFO
logging.level.org.springframework.security=WARN
logging.level.org.hibernate.SQL=WARN

# Production: Use JSON logging
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %logger{36} - %msg%n
logging.file.name=logs/erp.log
logging.file.max-size=10MB
logging.file.max-history=30

# Log to file with rotation
logging.logback.rollingpolicy.total-size-cap=1GB
```

### Set Up Monitoring with Prometheus + Grafana

**1. Enable Micrometer**:
```xml
<!-- In pom.xml -->
<dependency>
  <groupId>io.micrometer</groupId>
  <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

**2. Expose Prometheus endpoint**:
```properties
# application.properties
management.endpoints.web.exposure.include=health,metrics,prometheus
management.metrics.export.prometheus.enabled=true
```

**3. Prometheus scrape config**:
```yaml
# prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'erp-system'
    static_configs:
      - targets: ['localhost:8081']
    metrics_path: '/actuator/prometheus'
```

**4. Access Prometheus**:
```
http://localhost:9090
```

---

## 🛠️ Database Maintenance

### Regular Backups

**Automated PostgreSQL Backup**:
```bash
#!/bin/bash
# backup.sh
BACKUP_DIR="/backups/erp-db"
DATE=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="$BACKUP_DIR/erp_db_$DATE.sql.gz"

mkdir -p $BACKUP_DIR

pg_dump -h localhost -U postgres -d erp_db | gzip > $BACKUP_FILE

# Keep only last 30 days
find $BACKUP_DIR -name "*.sql.gz" -mtime +30 -delete

echo "Backup completed: $BACKUP_FILE"
```

**Schedule daily backup** (crontab):
```cron
# Daily backup at 2:00 AM
0 2 * * * /home/ubuntu/backup.sh >> /var/log/backup.log 2>&1
```

### Restore from Backup

```bash
# Restore from backup
gunzip < erp_db_20260221_020000.sql.gz | \
  psql -h localhost -U postgres -d erp_db

# Verify restore
psql -h localhost -U postgres -d erp_db -c "SELECT COUNT(*) FROM invoices;"
```

### Database Maintenance

```sql
-- Analyze table statistics
ANALYZE;

-- Rebuild indexes
REINDEX DATABASE erp_db;

-- Cleanup bloat
VACUUM FULL;

-- Check database size
SELECT pg_database.datname, 
       pg_size_pretty(pg_database_size(pg_database.datname))
FROM pg_database;

-- Find missing indexes
SELECT schemaname, tablename, attname, n_distinct
FROM pg_stats
WHERE schemaname NOT IN ('pg_catalog', 'information_schema')
ORDER BY n_distinct DESC;
```

---

## 🔐 Security Hardening

### SSL/TLS Configuration

**Enable HTTPS**:
```properties
# application.properties
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.jks
server.ssl.key-store-password=changeit
server.ssl.key-store-type=JKS
server.ssl.key-alias=tomcat
```

**Generate Self-Signed Certificate** (dev only):
```bash
keytool -genkey -alias tomcat \
  -keyalg RSA -keysize 2048 \
  -keystore keystore.jks \
  -validity 365 \
  -keypass changeit \
  -storepass changeit \
  -dname "CN=localhost,OU=Dev,O=Company,L=City,ST=State,C=US"
```

### Database Security

```sql
-- Create non-admin user for application
CREATE USER erp_user WITH PASSWORD 'strong_password_here';
GRANT CONNECT ON DATABASE erp_db TO erp_user;
GRANT USAGE ON SCHEMA public TO erp_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO erp_user;

-- Revoke unnecessary permissions
REVOKE ALL ON DATABASE erp_db FROM PUBLIC;
```

### Network Security

**Firewall Rules** (iptables example):
```bash
# Allow only needed ports
iptables -A INPUT -p tcp --dport 8081 -j ACCEPT  # Application
iptables -A INPUT -p tcp --dport 5432 -j ACCEPT  # PostgreSQL (local only)
iptables -A INPUT -p tcp --dport 6379 -j ACCEPT  # Redis (local only)
iptables -A INPUT -j DROP  # Deny all else
```

**Nginx Proxy Example**:
```nginx
upstream erp_backend {
    server 127.0.0.1:8081;
    server 127.0.0.1:8082;  # Second instance for HA
}

server {
    listen 443 ssl http2;
    server_name erp.example.com;
    
    ssl_certificate /etc/letsencrypt/live/erp.example.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/erp.example.com/privkey.pem;
    
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    
    location / {
        proxy_pass http://erp_backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # Rate limiting
        limit_req zone=api_limit burst=10 nodelay;
    }
}

limit_req_zone $binary_remote_addr zone=api_limit:10m rate=10r/s;
```

---

## 🔄 High Availability Setup

### Load Balancing

**Run Multiple Instances**:
```bash
# Terminal 1
java -Dserver.port=8081 -jar ERPMain-1.0.0.jar

# Terminal 2
java -Dserver.port=8082 -jar ERPMain-1.0.0.jar

# Terminal 3
java -Dserver.port=8083 -jar ERPMain-1.0.0.jar
```

**Nginx as Load Balancer**:
```nginx
upstream erp_instances {
    least_conn;  # Load balancing algorithm
    server 127.0.0.1:8081 weight=1 max_fails=3 fail_timeout=30s;
    server 127.0.0.1:8082 weight=1 max_fails=3 fail_timeout=30s;
    server 127.0.0.1:8083 weight=1 max_fails=3 fail_timeout=30s;
}

server {
    listen 80;
    location / {
        proxy_pass http://erp_instances;
        proxy_http_version 1.1;
        proxy_set_header Connection "";
        proxy_set_header Host $host;
        
        # Health check
        access_log /var/log/nginx/erp_access.log;
    }
}
```

### Database High Availability

**PostgreSQL Replication** (Primary-Replica setup):

1. **Primary Server** (192.168.1.100):
```bash
# Enable replication in postgresql.conf
wal_level = replica
max_wal_senders = 3
max_replication_slots = 3
hot_standby = on
```

2. **Replica Server** (192.168.1.101):
```bash
# Take base backup
pg_basebackup -h 192.168.1.100 -D /var/lib/postgresql/14/main -U replicator -v -P

# Create recovery.conf
standby_mode = 'on'
primary_conninfo = 'host=192.168.1.100 port=5432 user=replicator'
```

### Redis Sentinel (High Availability)

**sentinel.conf**:
```
port 26379
daemonize no
logfile ""
dir /var/lib/redis

sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 30000
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 180000
```

---

## 📈 Scaling Guide

### Vertical Scaling (Bigger Server)

```bash
# Increase Java heap size
java -Xmx4G -Xms2G -jar ERPMain-1.0.0.jar
```

### Horizontal Scaling (More Instances)

**Configuration for distributed caching**:
```properties
# application.properties
spring.data.redis.cluster.nodes=redis1:6379,redis2:6379,redis3:6379
spring.session.store-type=redis
```

**Database Connection Pooling**:
```properties
spring.datasource.hikari.maximum-pool-size=30
spring.datasource.hikari.minimum-idle=10
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

---

## 🚨 Troubleshooting

### Issue: Application Won't Start

```bash
# Check logs
tail -f logs/erp.log
java -jar ERPMain-1.0.0.jar 2>&1 | head -100

# Check port in use
netstat -tunlp | grep 8081
lsof -i :8081

# Verify environment
echo $SPRING_DATASOURCE_URL
echo $SPRING_REDIS_HOST
```

### Issue: Database Connection Failed

```bash
# Test PostgreSQL connection
psql -h localhost -U postgres -d erp_db -c "SELECT 1;"

# Check connection pool
curl http://localhost:8081/actuator/metrics/db.connection.active | jq .

# Verify credentials
psql -h localhost -U postgres -W  # Enter password
```

### Issue: OutOfMemoryException

```bash
# Check JVM memory
curl http://localhost:8081/actuator/metrics/jvm.memory.used | jq .

# Increase heap size
java -Xmx4G -Xms2G -jar ERPMain-1.0.0.jar

# Analyze heap dump
jmap -dump:live,format=b,file=heap.hprof <PID>
jhat -J-Xmx4G heap.hprof
```

### Issue: Slow Database Queries

```sql
-- Enable query logging
ALTER SYSTEM SET log_min_duration_statement = 1000;  -- Log queries > 1s
SELECT pg_reload_conf();

-- Find slow queries
SELECT query, mean_time, max_time, calls
FROM pg_stat_statements
ORDER BY mean_time DESC
LIMIT 10;

-- Analyze query plan
EXPLAIN ANALYZE SELECT * FROM invoices WHERE status = 'OVERDUE';
```

---

## 📋 Maintenance Checklist

### Daily
- [ ] Check application health: `/actuator/health`
- [ ] Review error logs
- [ ] Monitor disk space
- [ ] Verify backups completed

### Weekly
- [ ] Analyze database statistics: `ANALYZE;`
- [ ] Check slow query logs
- [ ] Review security logs
- [ ] Test failover (HA setups)

### Monthly
- [ ] Full database backup and restore test
- [ ] Update dependencies: `./mvnw versions:display-updates`
- [ ] Review performance metrics
- [ ] Optimize indexes if needed
- [ ] Update SSL certificates (before expiry)

### Quarterly
- [ ] Security audit
- [ ] Load testing
- [ ] Disaster recovery drill
- [ ] Update Java/Spring versions (if needed)

---

## 📞 Support & Escalation

### Critical Issues

If application is down:
1. Check `/actuator/health`
2. Verify PostgreSQL and Redis are running
3. Review recent logs
4. Restart application
5. If issue persists, check system resources

### Contact

- **Dev Team**: dev-team@company.com
- **DevOps**: devops@company.com
- **Database Team**: dba@company.com

---

**Last Updated**: February 21, 2026  
**Status**: ✅ Production Ready
