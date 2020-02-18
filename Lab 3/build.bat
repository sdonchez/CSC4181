@echo off
REM Build file for math expression parser
echo Compiling yacc parser specification
yacc -d -J mathexpr.y

echo Compiling JFlex scanner specification
call jflex -q mathexpr.flex

echo Compiling main program
javac MathExprParser.java Parser.java Yylex.java

echo Done