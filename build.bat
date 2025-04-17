@echo off
setlocal

:: ???? ??
set SRC=src
set BUILD=build
set LIB=lib
set JSON=JSON
set JAR_NAME=AutoInstaller.jar

:: ?? ???? ???
if exist %BUILD% (
    echo [Info] Initializing...
    rmdir /s /q %BUILD%
)
mkdir %BUILD%

:: ????? ?? (????? ??)
set CLASSPATH=%LIB%\jackson-annotations-2.15.2.jar;%LIB%\jackson-core-2.15.2.jar;%LIB%\jackson-databind-2.15.2.jar

:: ?? Java ?? ??? ??
echo Finding Sources...
del sources.txt >nul 2>&1
for /r %SRC% %%f in (*.java) do (
    echo %%f >> sources.txt
)

:: ???
echo Loading...
javac -cp %CLASSPATH% -d %BUILD% @sources.txt

if not %ERRORLEVEL%==0 (
    echo Failed!
    del sources.txt >nul 2>&1
    pause
    exit /b
)

:: ??? ?? ? ?? ?? ??? ??
del sources.txt >nul 2>&1
echo Success!

:: Manifest ?? ??
echo Main-Class: Main> manifest.txt

:: ??? ???(JSON ?) ??
xcopy %JSON% %BUILD%\JSON\ /E /I /Y >nul

:: JAR ??
echo Generating jar file...
jar cfm %JAR_NAME% manifest.txt -C %BUILD% .

if %ERRORLEVEL%==0 (
    echo [Success] %JAR_NAME% Generated!
) else (
    echo [Error] JAR Failed!
)

:: ??
del manifest.txt >nul 2>&1
endlocal
pause
