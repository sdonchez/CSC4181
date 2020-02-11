@echo off
REM Cleaning up generated files
echo Cleaning up generates files
if EXIST *.class del *.class
if EXIST Yylex.java del Yylex.java
if EXIST Yylex.java~ del Yylex.java~
echo Done