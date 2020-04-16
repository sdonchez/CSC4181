@echo off
REM Script to compile and run all C-Minus test programs
REM Usage: runall

set testdir=test

for %%a in (%testdir%/ex*.c) do call run.bat %testdir%/%%a
for %%a in (%testdir%/test*.c) do call run.bat %testdir%/%%a

