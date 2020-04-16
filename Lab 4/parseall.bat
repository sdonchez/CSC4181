@echo off
REM Script to parse all C-minus test programs
REM Usage: parseall

set testdir=test

for %%a in (%testdir%/*.c) do call parse.bat %testdir%/%%a

