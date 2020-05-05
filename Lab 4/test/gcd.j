.source stdin
.class synchronized Main
.super java/lang/Object

; >> FUNCTION gcd <<
.method static gcd(II)I
.limit stack 32
.limit locals 32
iload 1
ldc 0
if_icmpeq Label1
iconst_0
goto Label0
Label1:
iconst_1
Label0:
ifeq Label2
iload 0
ireturn
goto Label3
Label2:
iload 1
iload 0
iload 0
iload 1
idiv
iload 1
imul
isub
invokestatic Main/gcd(II)I
ireturn
Label3:
return
.end method

; >> FUNCTION main <<
.method public static main([Ljava/lang/String;)V
.throws java/io/IOException
.limit stack 32
.limit locals 32
invokestatic Main/myRead()I
istore 1
invokestatic Main/myRead()I
istore 2
getstatic java/lang/System/out Ljava/io/PrintStream;
iload 1
iload 2
invokestatic Main/gcd(II)I
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

