@echo off
echo ========================================
echo Food Ordering System - Startup Script
echo ========================================
echo.

echo Building the application...
call mvn clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo Build failed! Please check the errors above.
    pause
    exit /b 1
)

echo.
echo Build successful! Starting the application...
echo.
echo The application will be available at: http://localhost:8080
echo H2 Console (if using H2): http://localhost:8080/h2-console
echo.
echo Press Ctrl+C to stop the application
echo.

java -jar target/order-feedbacks-0.0.1-SNAPSHOT.jar

pause
