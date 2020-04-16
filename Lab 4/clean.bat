@echo off
REM Cleaning up generated files
echo Cleaning up generates files
if EXIST *.class del *.class
if EXIST Yylex.java del Yylex.java
if EXIST Yylex.java~ del Yylex.java~
if EXIST Parser.java del Parser.java
if EXIST ParserTokens.java del ParserTokens.java
if EXIST ParserVal.java del ParserVal.java
if EXIST test del test\*.j
echo Done