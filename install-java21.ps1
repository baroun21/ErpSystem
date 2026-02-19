# Java 21 Installation Script for Windows
# Run as Administrator in PowerShell

Write-Host "=== Java 21 JDK Installation Helper ===" -ForegroundColor Green
Write-Host ""

# Check if running as admin
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole] "Administrator")
if (-not $isAdmin) {
    Write-Host "ERROR: This script must be run as Administrator!" -ForegroundColor Red
    Write-Host "Please right-click PowerShell and select 'Run as Administrator'" -ForegroundColor Yellow
    exit 1
}

Write-Host "Step 1: Download Java 21 JDK" -ForegroundColor Cyan
Write-Host ""
Write-Host "Please download Java 21 JDK from one of these sources:" -ForegroundColor Yellow
Write-Host ""
Write-Host "Option A - Oracle Official (Free registration required):" -ForegroundColor White
Write-Host "  https://www.oracle.com/java/technologies/downloads/#java21" -ForegroundColor Blue
Write-Host ""
Write-Host "Option B - Eclipse Temurin (No registration, recommended):" -ForegroundColor White
Write-Host "  https://adoptium.net/temurin/releases/?version=21" -ForegroundColor Blue
Write-Host "  Choose: Windows x64 MSI Installer"
Write-Host ""
Write-Host "Option C - Amazon Corretto:" -ForegroundColor White
Write-Host "  https://aws.amazon.com/corretto/" -ForegroundColor Blue
Write-Host ""

Write-Host "After downloading, run the installer (*.msi file)" -ForegroundColor Yellow
Write-Host "Installation Recommendation: Install to C:\Program Files\Java\jdk-21" -ForegroundColor Green
Write-Host ""

Read-Host "Press Enter once you have completed the Java 21 installation..."

Write-Host ""
Write-Host "Step 2: Verifying Java 21 Installation" -ForegroundColor Cyan
Write-Host ""

# Check if java is in PATH
$javaPath = (Get-Command java -ErrorAction SilentlyContinue).Source
if ($javaPath) {
    Write-Host "✓ Java found at: $javaPath" -ForegroundColor Green
    java -version 2>&1
} else {
    Write-Host "✗ Java not found in PATH" -ForegroundColor Red
    Write-Host "Checking common installation paths..." -ForegroundColor Yellow
    
    $commonPaths = @(
        "C:\Program Files\Java\jdk-21\bin\java.exe",
        "C:\Program Files\Eclipse Temurin\jdk-21.0.1+12\bin\java.exe",
        "C:\Program Files\Amazon Corretto\jdk21.0.1_12\bin\java.exe"
    )
    
    $found = $false
    foreach ($path in $commonPaths) {
        if (Test-Path $path) {
            Write-Host "Found Java at: $path" -ForegroundColor Green
            $found = $true
            $javaPath = $path
            break
        }
    }
    
    if (-not $found) {
        Write-Host "Java 21 not found in common paths!" -ForegroundColor Red
        Write-Host "Please ensure Java 21 was installed correctly." -ForegroundColor Yellow
        exit 1
    }
}

Write-Host ""
Write-Host "Step 3: Setting JAVA_HOME Environment Variable" -ForegroundColor Cyan
Write-Host ""

# Determine Java installation path
if ($javaPath) {
    $javaHome = Split-Path -Parent (Split-Path -Parent $javaPath)
} else {
    Write-Host "Could not determine Java home. Please enter Java installation path:" -ForegroundColor Yellow
    $javaHome = Read-Host "Enter full path to Java 21 (e.g., C:\Program Files\Java\jdk-21)"
}

Write-Host "Setting JAVA_HOME to: $javaHome" -ForegroundColor Yellow

# Set JAVA_HOME
[Environment]::SetEnvironmentVariable("JAVA_HOME", $javaHome, "Machine")
Write-Host "✓ JAVA_HOME set successfully" -ForegroundColor Green

# Add Java bin to PATH if not already there
$currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
$javaBin = "$javaHome\bin"

if ($currentPath -notlike "*$javaBin*") {
    Write-Host ""
    Write-Host "Adding Java bin directory to PATH..." -ForegroundColor Yellow
    $newPath = "$javaBin;$currentPath"
    [Environment]::SetEnvironmentVariable("Path", $newPath, "Machine")
    Write-Host "✓ Java bin added to PATH" -ForegroundColor Green
}

Write-Host ""
Write-Host "Step 4: Verifying Installation" -ForegroundColor Cyan
Write-Host ""

# Refresh environment variables
$env:JAVA_HOME = $javaHome
$env:Path = "$javaBin;$env:Path"

Write-Host "Java version:"
java -version 2>&1

Write-Host ""
Write-Host "Javac version:"
javac -version 2>&1

Write-Host ""
Write-Host "Step 5: Building Spring Boot Project" -ForegroundColor Cyan
Write-Host ""

$buildPath = "C:\Users\Devoe\ErpSystem\erp-core-spring"
if (Test-Path $buildPath) {
    Write-Host "Navigating to: $buildPath" -ForegroundColor Yellow
    Push-Location $buildPath
    
    Write-Host ""
    Write-Host "Building project with Maven..." -ForegroundColor Yellow
    Write-Host "(This may take 2-3 minutes on first build)" -ForegroundColor Gray
    Write-Host ""
    
    # Run Maven build
    .\mvnw clean package -DskipTests
    
    $buildResult = $?
    
    if ($buildResult) {
        Write-Host ""
        Write-Host "✓ Build completed successfully!" -ForegroundColor Green
        Write-Host ""
        Write-Host "Step 6: Running Spring Boot Application" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "To start the application, run:" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "  java -jar ERPMain/target/ERPMain-1.0.0.jar" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "Or use this command from current directory:" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "  .\run-app.ps1" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "Application will be available at:" -ForegroundColor Yellow
        Write-Host "  http://localhost:8081/api" -ForegroundColor Cyan
        Write-Host ""
        Write-Host "Database: Oracle" -ForegroundColor Yellow
        Write-Host "Port: 8081" -ForegroundColor Yellow
        Write-Host ""
    } else {
        Write-Host ""
        Write-Host "✗ Build failed! See errors above." -ForegroundColor Red
        exit 1
    }
    
    Pop-Location
} else {
    Write-Host "Project directory not found: $buildPath" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Setup complete! Java 21 is ready to use." -ForegroundColor Green
Write-Host ""

