 @echo off
echo [INFO] clean  and install the project  in maven repository dir.

cd %~dp0
call mvn -e  clean  install -e -Dmaven.test.skip=true 

pause