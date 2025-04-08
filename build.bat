@echo off
REM 현재 스크립트 위치로 이동 (항상 Project 루트 보장)
cd /d "%~dp0"

REM 경로 변수 설정
set SRC_DIR=src
set BIN_DIR=build
set LIB_DIR=lib
set MAIN_CLASS=autoinstaller.Main
set JAR_NAME=AutoInstaller.jar
set MANIFEST_FILE=manifest.txt

REM 빌드 시작
echo [정보] 빌드 디렉터리 초기화 중...
if exist %BIN_DIR% rmdir /s /q %BIN_DIR%
mkdir %BIN_DIR%

REM lib 디렉터리 내 모든 JAR을 classpath로 설정
setlocal EnableDelayedExpansion
set CP=
for %%f in (%LIB_DIR%\*.jar) do (
    set CP=!CP!;%%f
)
REM 첫 문자 세미콜론 제거
set CP=%CP:~1%
endlocal & set CLASSPATH=%CP%

REM 컴파일 수행
echo [정보] 소스 코드 컴파일 중...
javac -encoding UTF-8 -cp "%CLASSPATH%" -d %BIN_DIR% %SRC_DIR%\autoinstaller\*.java

if errorlevel 1 (
    echo [오류] 컴파일 실패
    exit /b 1
)

REM Manifest 파일 생성
echo [정보] Manifest 생성 중...
echo Main-Class: %MAIN_CLASS% > %MANIFEST_FILE%
echo Class-Path: %CLASSPATH% >> %MANIFEST_FILE%

REM JAR 생성
echo [정보] Fat JAR(%JAR_NAME%) 생성 중...
jar cfm %JAR_NAME% %MANIFEST_FILE% -C %BIN_DIR% .

REM 정리
del %MAN

pause