@echo off
rem Double-click to start Selenoid and Selenoid UI, then open the UI in the browser.
setlocal
title Start Selenoid

rem 1. Docker must be running
docker info >nul 2>&1
if not errorlevel 1 goto docker_ready

echo Docker is not running. Starting Docker Desktop...
start "" "C:\Program Files\Docker\Docker\Docker Desktop.exe"
set /a tries=0
:wait_docker
ping -n 6 127.0.0.1 >nul
docker info >nul 2>&1
if not errorlevel 1 goto docker_ready
set /a tries+=1
if %tries% lss 36 goto wait_docker
echo ERROR: Docker did not start within 3 minutes. Start Docker Desktop and run this file again.
goto end

:docker_ready
echo Docker is running.

rem 2. Start the containers; create them with cm if they do not exist yet
docker start selenoid >nul 2>&1
if errorlevel 1 cm selenoid start --vnc
docker start selenoid-ui >nul 2>&1
if errorlevel 1 cm selenoid-ui start

rem 3. Wait until Selenoid answers
set /a tries=0
:wait_selenoid
curl -s -f -o NUL http://localhost:4444/status
if not errorlevel 1 goto selenoid_ready
ping -n 3 127.0.0.1 >nul
set /a tries+=1
if %tries% lss 30 goto wait_selenoid
echo ERROR: Selenoid is not answering on http://localhost:4444. Check "docker ps" and "docker logs selenoid".
goto end

:selenoid_ready
echo.
echo Selenoid is up:    http://localhost:4444/wd/hub
echo Selenoid UI:       http://localhost:8080
echo.
echo Run the tests with:
echo   mvn test "-Dsurefire.suiteXmlFiles=testng-selenoid.xml"
start "" http://localhost:8080

:end
echo.
pause
