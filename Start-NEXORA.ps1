#!/usr/bin/env powershell
# NEXORA ERP Quick Start - Frontend & Backend
# Run both services locally with proper configuration

$ErrorActionPreference = "Stop"

Write-Host "╔════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
Write-Host "║         NEXORA ERP - Frontend & Backend Quick Start        ║" -ForegroundColor Cyan
Write-Host "╚════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
Write-Host ""

# Check prerequisites
Write-Host "📋 Checking prerequisites..." -ForegroundColor Yellow

# Check Java
try {
    $javaVersion = java -version 2>&1 | Select-String "version"
    if ($javaVersion) {
        Write-Host "✅ Java installed: $javaVersion" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ Java not found. Please install Java 21 from https://www.oracle.com/java/technologies/downloads/" -ForegroundColor Red
    exit 1
}

# Check Node.js
try {
    $nodeVersion = node -v
    Write-Host "✅ Node.js installed: $nodeVersion" -ForegroundColor Green
} catch {
    Write-Host "❌ Node.js not found. Please install from https://nodejs.org/" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "📦 Setting up services..." -ForegroundColor Yellow
Write-Host ""

# Setup Backend (JAR already built)
$backendJar = "c:\Users\Devoe\ErpSystem\erp-core-spring\ERPMain\target\ERPMain-1.0.0.jar"
if (-not (Test-Path $backendJar)) {
    Write-Host "⚠️  Backend JAR not found. Building..." -ForegroundColor Yellow
    Push-Location "c:\Users\Devoe\ErpSystem\erp-core-spring"
    .\mvnw clean package -DskipTests -q
    Pop-Location
}

# Clean up old processes
$port8081 = Get-NetTCPConnection -LocalPort 8081 -ErrorAction SilentlyContinue | Select-Object -First 1 -ExpandProperty OwningProcess
if ($port8081) {
    Write-Host "🧹 Stopping existing process on port 8081..." -ForegroundColor Yellow
    Stop-Process -Id $port8081 -Force -ErrorAction SilentlyContinue
    Start-Sleep -Seconds 2
}

$port5173 = Get-NetTCPConnection -LocalPort 5173 -ErrorAction SilentlyContinue | Select-Object -First 1 -ExpandProperty OwningProcess
if ($port5173) {
    Write-Host "🧹 Stopping existing process on port 5173..." -ForegroundColor Yellow
    Stop-Process -Id $port5173 -Force -ErrorAction SilentlyContinue
    Start-Sleep -Seconds 2
}

Write-Host ""
Write-Host "🚀 Starting Backend (Spring Boot on port 8081)..." -ForegroundColor Green
Write-Host "   Command: java -jar $backendJar" -ForegroundColor Gray
Write-Host ""

# Start backend in background
$backendProcess = Start-Process -FilePath "java" -ArgumentList "-jar", $backendJar `
    -WorkingDirectory "c:\Users\Devoe\ErpSystem\erp-core-spring" `
    -NoNewWindow -PassThru

$backendPid = $backendProcess.Id

# Wait for backend to start
Write-Host "⏳ Waiting for backend to start..." -ForegroundColor Yellow
$maxAttempts = 30
$attempt = 0
$backendReady = $false

while ($attempt -lt $maxAttempts) {
    try {
        $health = Invoke-WebRequest -Uri "http://localhost:8081/actuator/health" -Method Get -TimeoutSec 2 -ErrorAction SilentlyContinue
        if ($health.StatusCode -eq 200) {
            $backendReady = $true
            break
        }
    } catch {
        # Still starting
    }
    
    if (-not (Get-Process -Id $backendPid -ErrorAction SilentlyContinue)) {
        Write-Host "❌ Backend process crashed. Check logs." -ForegroundColor Red
        exit 1
    }
    
    Start-Sleep -Seconds 1
    $attempt++
    Write-Host "." -NoNewline -ForegroundColor Yellow
}

if ($backendReady) {
    Write-Host ""
    Write-Host "✅ Backend is ready! (http://localhost:8081)" -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "❌ Backend startup timeout. Check if port 8081 is in use." -ForegroundColor Red
    Stop-Process -Id $backendPid -ErrorAction SilentlyContinue
    exit 1
}

Write-Host ""
Write-Host "🚀 Starting Frontend (Vite on port 5173)..." -ForegroundColor Green
Write-Host ""

# Start frontend
Push-Location "c:\Users\Devoe\ErpSystem\erp-frontend"

# Install dependencies if needed
if (-not (Test-Path "node_modules")) {
    Write-Host "📦 Installing frontend dependencies..." -ForegroundColor Yellow
    npm install -q
}

Write-Host "   Command: npm run dev" -ForegroundColor Gray
Write-Host ""

$frontendProcess = Start-Process -FilePath "npm" -ArgumentList "run", "dev" `
    -NoNewWindow -PassThru

Pop-Location

# Wait for frontend
Write-Host "⏳ Waiting for frontend to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

Write-Host ""
Write-Host "╔════════════════════════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "║             ✅ NEXORA ERP is now running! ✅              ║" -ForegroundColor Green
Write-Host "╚════════════════════════════════════════════════════════════╝" -ForegroundColor Green
Write-Host ""
Write-Host "📍 Frontend URL: http://localhost:5173" -ForegroundColor Cyan
Write-Host "📍 Backend URL:  http://localhost:8081" -ForegroundColor Cyan
Write-Host "📍 Backend API:  http://localhost:8081/api" -ForegroundColor Cyan
Write-Host ""
Write-Host "🔐 Default Credentials:" -ForegroundColor Yellow
Write-Host "   Username: admin" -ForegroundColor Gray
Write-Host "   Password: (check database or application.properties)" -ForegroundColor Gray
Write-Host ""
Write-Host "📚 Available Modules:" -ForegroundColor Yellow
Write-Host "   • HR Module      → Click 'HR' in topbar" -ForegroundColor Gray
Write-Host "   • Finance Module → Click 'Finance' in topbar" -ForegroundColor Gray
Write-Host ""
Write-Host "🛑 To stop services:" -ForegroundColor Yellow
Write-Host "   Press Ctrl+C in each terminal window" -ForegroundColor Gray
Write-Host ""
Write-Host "📖 Documentation:" -ForegroundColor Yellow
Write-Host "   • FRONTEND_BACKEND_INTEGRATION.md - Integration details" -ForegroundColor Gray
Write-Host "   • BACKEND_CONTROLLER_STATUS.md - Controller checklist" -ForegroundColor Gray
Write-Host ""

# Keep script running until frontend is closed
Write-Host "Services are running. Press Ctrl+C to stop." -ForegroundColor Gray
Wait-Process -Id $frontendProcess.Id -ErrorAction SilentlyContinue

# Cleanup
Write-Host ""
Write-Host "Stopping backend..." -ForegroundColor Yellow
Stop-Process -Id $backendPid -ErrorAction SilentlyContinue
Write-Host "✅ Services stopped." -ForegroundColor Green
