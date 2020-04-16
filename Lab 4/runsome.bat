@echo off
REM Script to compile and run some of the C-Minus test programs
REM Usage: runsome

set testdir=test

call run.bat %testdir%\ex0.c
call run.bat %testdir%\test0.c
call run.bat %testdir%\max.c
