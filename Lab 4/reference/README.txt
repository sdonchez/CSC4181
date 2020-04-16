C-Minus Reference Compilers
---------------------------

This folder contains two versions of the C-Minus compiler. One compiles 
a C-Minus program to produce a symbol table as output. The other compiles
a C-Minus program to produce JVM (jasmin) assembly code.


Getting Ready to Use
--------------------

Copy one or more C-Minus programs into this folder, copy an entire folder
of C-Minus programs here, or copy SymbolTable.jar or CodeGen.jar into the
main project folder where your example C-Minus programs are.


How to Generate a Symbol Table
------------------------------

  java -jar SymbolTable.jar < input-file

Example:

  java -jar SymbolTable.jar < max.c


How to Generate and Run a Java class
------------------------------------

  java -jar CodeGen.jar < input-file > output-file
  java -jar jasmin.jar output-file
  java Main

Example:

  java -jar CodeGen.jar < max.c > max.j
  java -jar jasmin.jar max.j
  java Main

NOTE: The user is not prompted if there is an input command in the C-Minus
program you are running. The cursor will simply wait for you to type a
number and hit Enter.


How to Generate Jasmin Code from a Java class
---------------------------------------------

Doing this will enable you to see what the Java JVM (jasmin) assembly
code should be for any given Java class file you compile. For example,
create a short Java program that declares and initializes an integer 
variable, use the javac compiler to compile it, and then use Jaspar
to disassemble it to its jasmin assembly language code.

  java -jar Jaspar.jar input-file

Example:

  java -jar Jaspar.jar Main.class
