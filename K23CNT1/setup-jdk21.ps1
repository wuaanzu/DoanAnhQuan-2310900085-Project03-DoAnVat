# Script to configure JDK 21 environment variables
# Run this script as Administrator

$jdkPath = "C:\Program Files\Java\jdk-21"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Configuring JDK 21 Environment" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if JDK exists
if (Test-Path $jdkPath) {
    Write-Host "Found JDK 21 at: $jdkPath" -ForegroundColor Green
} else {
    Write-Host "JDK 21 not found at: $jdkPath" -ForegroundColor Red
    Write-Host "Please check the installation path and update the script." -ForegroundColor Yellow
    exit 1
}

# Set JAVA_HOME
try {
    [System.Environment]::SetEnvironmentVariable('JAVA_HOME', $jdkPath, 'Machine')
    Write-Host "JAVA_HOME set to: $jdkPath" -ForegroundColor Green
} catch {
    Write-Host "Failed to set JAVA_HOME: $_" -ForegroundColor Red
    exit 1
}

# Update PATH
try {
    $currentPath = [System.Environment]::GetEnvironmentVariable('Path', 'Machine')
    
    # Remove old Java paths
    $pathArray = $currentPath -split ';'
    $cleanedPath = $pathArray | Where-Object { $_ -notlike "*Java\jre*" -and $_ -notlike "*Java\jdk*" }
    
    # Add new JDK path at the beginning
    $newPath = "$jdkPath\bin;" + ($cleanedPath -join ';')
    
    [System.Environment]::SetEnvironmentVariable('Path', $newPath, 'Machine')
    Write-Host "PATH updated successfully" -ForegroundColor Green
    Write-Host "JDK 21 bin added to PATH" -ForegroundColor Green
} catch {
    Write-Host "Failed to update PATH: $_" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Configuration Complete!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "IMPORTANT: Please close this terminal and open a NEW terminal" -ForegroundColor Yellow
Write-Host "Then run these commands to verify:" -ForegroundColor Yellow
Write-Host ""
Write-Host "  java -version" -ForegroundColor White
Write-Host "  javac -version" -ForegroundColor White
Write-Host "  echo" '$env:JAVA_HOME' -ForegroundColor White
Write-Host ""
