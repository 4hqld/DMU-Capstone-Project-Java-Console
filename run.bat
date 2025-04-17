@echo off
setlocal

:: Set library and JAR paths
set LIB=lib
set JAR=AutoInstaller.jar

:: Run JAR with external libraries included in classpath
java -cp "%JAR%;%LIB%\*" autoinstaller.Main

endlocal
pause
