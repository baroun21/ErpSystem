# Run Spring Boot ERP Application
# Script to start the application after it's built

Write-Host "=== Starting Spring Boot ERP Application ===" -ForegroundColor Green
Write-Host ""

$appPath = "C:\Users\Devoe\ErpSystem\erp-core-spring\ERPMain\target\ERPMain-1.0.0.jar"

if (-not (Test-Path $appPath)) {
    Write-Host "ERROR: Application JAR not found!" -ForegroundColor Red
    Write-Host "Expected path: $appPath" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Please build the project first by running:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "  cd C:\Users\Devoe\ErpSystem\erp-core-spring" -ForegroundColor Cyan
    Write-Host "  .\mvnw clean package -DskipTests" -ForegroundColor Cyan
    Write-Host ""
    exit 1
}

Write-Host "Starting application..." -ForegroundColor Yellow
Write-Host ""
Write-Host "Application Details:" -ForegroundColor Cyan
Write-Host "  JAR File: ERPMain-1.0.0.jar" -ForegroundColor White
Write-Host "  Port: 8081" -ForegroundColor White
Write-Host "  API Base URL: http://localhost:8081/api" -ForegroundColor White
Write-Host "  Database: Oracle" -ForegroundColor White
Write-Host ""
Write-Host "To access the application:" -ForegroundColor Yellow
Write-Host "  - Open browser: http://localhost:8081/api" -ForegroundColor Cyan
Write-Host "  - Login endpoint: http://localhost:8081/api/auth/login" -ForegroundColor Cyan
Write-Host ""
Write-Host "Startup may take 10-20 seconds. Once running, you'll see:" -ForegroundColor Gray
Write-Host "  'Started ErpApplication in X.XXX seconds'" -ForegroundColor Gray
Write-Host ""
Write-Host "Press Ctrl+C to stop the application" -ForegroundColor Yellow
Write-Host ""

java -jar $appPath

