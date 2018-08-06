@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  TestLoad startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and TEST_LOAD_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\TestLoad.jar;%APP_HOME%\lib\ignite-geospatial-2.5.2.jar;%APP_HOME%\lib\ignite-indexing-2.6.0.jar;%APP_HOME%\lib\ignite-core-2.6.0.jar;%APP_HOME%\lib\cache-api-1.0.0.jar;%APP_HOME%\lib\annotations-13.0.jar;%APP_HOME%\lib\ignite-shmem-1.0.0.jar;%APP_HOME%\lib\jts-1.13.jar;%APP_HOME%\lib\commons-codec-1.11.jar;%APP_HOME%\lib\lucene-analyzers-common-5.5.2.jar;%APP_HOME%\lib\lucene-queryparser-5.5.2.jar;%APP_HOME%\lib\lucene-core-5.5.2.jar;%APP_HOME%\lib\h2-1.4.195.jar;%APP_HOME%\lib\lucene-queries-5.5.2.jar;%APP_HOME%\lib\lucene-sandbox-5.5.2.jar

@rem Execute TestLoad
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %TEST_LOAD_OPTS%  -classpath "%CLASSPATH%" Main %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable TEST_LOAD_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%TEST_LOAD_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
