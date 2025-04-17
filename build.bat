@echo off
setlocal

:: Directory configuration
set SRC=src
set BUILD=build
set LIB=lib
set JSON=JSON
set JAR_NAME=AutoInstaller.jar

:: Change this to your actual main class (with package name if exists)
:: Example: autoinstaller.Main or just Main
set MAIN_CLASS=autoinstaller.Main

:: Clean previous build
if exist %BUILD% (
    echo [INFO] Cleaning up previous build directory...
    rmdir /s /q %BUILD%
)
mkdir %BUILD%

:: Classpath including external libraries
set CLASSPATH=%LIB%\jackson-annotations-2.15.2.jar;%LIB%\jackson-core-2.15.2.jar;%LIB%\jackson-databind-2.15.2.jar

:: Collect all Java source files
echo [INFO] Collecting source files...
del sources.txt >nul 2>&1
for /r %SRC% %%f in (*.java) do (
    echo %%f >> sources.txt
)

:: Compile
echo [INFO] Compiling sources...
javac -cp %CLASSPATH% -d %BUILD% @sources.txt

if not %ERRORLEVEL%==0 (
    echo [ERROR] Compilation failed!
    del sources.txt >nul 2>&1
    pause
    exit /b
)

del sources.txt >nul 2>&1
echo [SUCCESS] Compilation completed.

:: Create manifest file
echo Main-Class: %MAIN_CLASS% > manifest.txt

:: Copy resource files (e.g., JSON)
xcopy %JSON% %BUILD%\resources\JSON\ /E /I /Y >nul

:: Create executable JAR
echo [INFO] Creating JAR file...
jar cfm %JAR_NAME% manifest.txt -C %BUILD% .

if %ERRORLEVEL%==0 (
    echo [SUCCESS] JAR file "%JAR_NAME%" created successfully.
) else (
    echo [ERROR] Failed to create JAR file.
)

:: Cleanup
del manifest.txt >nul 2>&1
endlocal
pause
