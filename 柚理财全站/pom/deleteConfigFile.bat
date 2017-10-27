@echo off

cd %~dp0

ECHO [WARN]  1) delete the eclipse project files: .classpath,.project  
ECHO [WARN]  2) delete directories: .settings,bin,target 
ECHO [WARN]  3) nothing to do

 set /p  delFlag=Please input [1/2/3] :

if %delFlag% == 1  goto first

if %delFlag% == 2  goto second

if %delFlag% == 3  goto eof
  
:first

ECHO deleting the  .classpath,.project files.

 FOR /r %~dp0 %%i IN ( .project,.classpath) DO (
    if exist %%i  DEL /F /A /Q %%i
    )
pause
exit

:second

 ECHO deleting  .settings,bin,target directories

 FOR /r %~dp0 %%i IN ( .settings,bin,target) DO (if exist %%i  RMDIR /S /Q %%i) 

:eof
pause