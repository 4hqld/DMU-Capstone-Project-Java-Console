@echo off
echo [정보] 빌드 시작...

:: 1. build 폴더 생성
if not exist build (
    mkdir build
    echo [정보] build 디렉토리 생성 완료
)

:: 2. 컴파일
echo [정보] Java 파일 컴파일 중...
javac -encoding utf-8 -d build src\*.java
if errorlevel 1 (
    echo [오류] 컴파일 실패
    pause
    exit /b
)

:: 3. manifest.txt 생성
echo Main-Class: Main> manifest.txt
echo.>> manifest.txt

:: 4. JAR 파일 생성
echo [정보] JAR 파일 생성 중...
jar cfm AutoInstaller.jar manifest.txt -C build .

if exist AutoInstaller.jar (
    echo [성공] AutoInstaller.jar 생성 완료
) else (
    echo [오류] JAR 파일 생성 실패
)

:: 5. 정리
del manifest.txt

pause
