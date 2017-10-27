@echo off
echo [INFO] clean  and packed the project  in the target directory.

cd %~dp0
call mvn -e   clean package -Dmaven.test.skip=true

pause