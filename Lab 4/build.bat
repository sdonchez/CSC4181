@echo off
REM Build file for C-Minus compiler
echo Compiling yacc parser specification
yacc -d -J cminus.y

echo Compiling JFlex scanner specification
java -Xmx128m -jar jflex-full-1.7.0.jar -q cminus.flex

echo Compiling main program
javac ParseMain.java Parser.java Yylex.java

echo Done