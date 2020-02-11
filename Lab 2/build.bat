@echo off
REM Build file for math expression scanner
echo Compiling ParserTokens interface
javac ParserTokens.java

echo Compiling JFlex scanner specification
call jflex -q mathexpr.flex

echo Compiling main program
javac MathExprScanner.java Yylex.java

echo Done