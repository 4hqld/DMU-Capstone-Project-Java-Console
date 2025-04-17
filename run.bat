@echo off
setlocal

rem === Path Settings ===
set BUILD_DIR=build
set LIB_DIR=lib

rem === Set classpath for runtime ===
set CLASSPATH=%BUILD_DIR%;%LIB_DIR%\*;

rem === Run Main class ===
echo [INFO] Running program...
java -cp %CLASSPATH% autoinstaller.Main

if %ERRORLEVEL% neq 0 (
    echo [ERROR] Program terminated with errors.
    exit /b 1
)

echo [SUCCESS] Program exited successfully.
exit /b 0
