@echo off
REM Script to parse a Cminus program
REM Usage: parse program.c

set cprogram=%1

echo ==================== %cprogram% ====================

call:parsepgm %cprogram% %jprogram%
goto:eof

:parsepgm
echo Parse C-Minus program %~1
if not exist %~1 (
	echo C- program %1 not found
	exit /b 1
)
java ParseMain %~1
goto:eof

