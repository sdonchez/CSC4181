@echo off
REM Script to create jar file

set jarname=SymbolTable.jar
set manifest=MainManifest.txt

jar cfmv %jarname% %manifest% *.class