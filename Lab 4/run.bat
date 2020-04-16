@echo off
REM Script to compile and run a C-Minus program
REM Usage: run program.c

set cprogram=%1
set jprogram=%cprogram:.c=.j%
set classfile=Main

echo ==================== %cprogram% ====================

echo Deleting %jprogram% if it exists
if exist %jprogram% ( del %jprogram% )
echo Deleting %classfile%.class if it exists
if exist %classfile%.class ( del %classfile%.class )

call:c2jasmin %cprogram% %jprogram%
call:jasmin2class %jprogram%
call:runclass %classfile%
goto:eof

:c2jasmin
echo Compile C-Minus program %~1 to Jasmine code %~2
if not exist %~1 (
	echo C- program %1 not found
	exit /b 1
)
java ParseMain %~1 > %~2
goto:eof

:jasmin2class
echo Compile Jasmin program %~1 to Java bytecode
if not exist %~1 (
	echo Jasmin program %1 not found
	exit /b 1
)
if exist Main.class del Main.class
java -jar jasmin.jar %~1
goto:eof

:runclass
echo Running Java class for %cprogram%
if not exist %~1.class (
	echo Main program %1.class not found
	exit /b 1
)
echo OUTPUT
echo ---------------------------------
java %~1
echo ---------------------------------
goto:eof
