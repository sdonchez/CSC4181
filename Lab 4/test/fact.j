.source stdin
.class synchronized Main
.super java/lang/Object

; >> FUNCTION fact <<
.method static fact(I)I
.limit stack 32
.limit locals 32
iload 0
ldc 1
if_icmpgt Label1
iconst_0
goto Label0
Label1:
iconst_1
Label0:
ifeq Label2
iload 0
iload 0
ldc 1
isub
invokestatic Main/fact(I)I
imul
istore 1
goto Label3
Label2:
ldc 1
istore 1
Label3:
iload 1
ireturn
return
.end method

; >> FUNCTION main <<
.method public static main([Ljava/lang/String;)V
.throws java/io/IOException
.limit stack 32
.limit locals 32
invokestatic Main/myRead()I
istore 1
iload 1
invokestatic Main/fact(I)I
istore 2
getstatic java/lang/System/out Ljava/io/PrintStream;
iload 2
invokevirtual java/io/PrintStream/println(I)V
return
.end method

; >> READ METHOD <<
.method public static myRead()I
.throws java/io/IOException

.limit stack 5
.limit locals 2

new java/io/BufferedReader
dup
new java/io/InputStreamReader
dup
getstatic java/lang/System/in Ljava/io/InputStream;
invokenonvirtual java/io/InputStreamReader/<init>(Ljava/io/InputStream;)V
invokenonvirtual java/io/BufferedReader/<init>(Ljava/io/Reader;)V
astore_0
aload_0
invokevirtual java/io/BufferedReader/readLine()Ljava/lang/String;
astore_1
aload_1
invokestatic java/lang/Integer/parseInt(Ljava/lang/String;)I
ireturn
.end method


; >> CONSTRUCTOR <<
.method <init>()V
.limit stack 1
.limit locals 1
aload_0
invokenonvirtual java/lang/Object/<init>()V
return
.end method

; >> CLASS INIT FOR GLOBAL ARRAYS <<
.method static <clinit>()V
.limit stack 1
.limit locals 0
return
.end method

