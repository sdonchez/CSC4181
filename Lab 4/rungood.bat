@echo off
REM Script to compile and run good C-Minus test programs
REM Usage: rungood

set testdir=test

call run.bat %testdir%\max.c
call run.bat %testdir%\most.c
call run.bat %testdir%\gcd.c
call run.bat %testdir%\fact.c
call run.bat %testdir%\sort.c
