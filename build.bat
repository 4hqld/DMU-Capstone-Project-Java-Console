@echo off
setlocal

:: [정보] 경로 설정
set SRC_DIR=src
set BUILD_DIR=build
set LIB_DIR=lib
set MAIN_CLASS=autoinstaller.Main
set JAR_NAME=AutoInstaller-FAT.jar

:: [정보] build 폴더 초기화
echo [정보] build 디렉터리 초기화 중...
if exist %BUILD_DIR% rmdir /s /q %BUILD_DIR%
mkdir %BUILD_DIR%

:: [정보] 클래스 컴파일
echo [정보] 소스 컴파일 중...
javac -d %BUILD_DIR% -cp "%LIB_DIR%\*" %SRC_DIR%\autoinstaller\*.java

if %ERRORLEVEL% NEQ 0 (
    echo [오류] 컴파일 실패.
    exit /b 1
)

:: [정보] fat jar용 임시 폴더 생성
set TMP_DIR=tmp_jar
if exist %TMP_DIR% rmdir /s /q %TMP_DIR%
mkdir %TMP_DIR%
mkdir %TMP_DIR%\autoinstaller

:: [정보] 클래스 복사
xcopy /Y /E %BUILD_DIR%\*.class %TMP_DIR%\

:: [정보] 라이브러리 JAR 압축 해제
echo [정보] 라이브러리 압축 해제 중...
for %%f in (%LIB_DIR%\*.jar) do (
    echo    %%~nxf 포함 중...
    pushd %TMP_DIR%
    jar xf ..\%%f
    popd
)

:: [정보] fat jar 생성
echo Main-Class: %MAIN_CLASS% > manifest.txt
jar cfm %JAR_NAME% manifest.txt -C %TMP_DIR% .

:: [정리]
rmdir /s /q %TMP_DIR%

echo [성공] fat JAR 생성 완료: %JAR_NAME%

:: [정보] 실행 예시
echo.
echo [정보] 실행 명령어:
echo java -jar %JAR_NAME%
echo.

endlocal
pause
