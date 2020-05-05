.source stdin
.class synchronized Main
.super java/lang/Object

.field static x [I
; >> FUNCTION minloc <<
.method static minloc([III)I
.limit stack 32
.limit locals 32
iload 1
istore 5
aload 0
iload 1
iaload
istore 4
iload 1
ldc 1
iadd
istore 3
Label0:
iload 3
iload 2
if_icmplt Label2
iconst_0
goto Label1
Label2:
iconst_1
Label1:
ifeq Label3
aload 0
iload 3
iaload
iload 4
if_icmplt Label5
iconst_0
goto Label4
Label5:
iconst_1
Label4:
ifeq Label6
aload 0
iload 3
iaload
istore 4
iload 3
istore 5
goto Label7
Label6:
Label7:
iload 3
ldc 1
iadd
istore 3
goto Label0
Label3:
iload 5
ireturn
return
.end method

; >> FUNCTION sort <<
.method static sort([III)V
.limit stack 32
.limit locals 32
iload 1
istore 3
Label8:
iload 3
iload 2
ldc 1
isub
if_icmplt Label10
iconst_0
goto Label9
Label10:
iconst_1
Label9:
ifeq Label11
aload 0
iload 3
iload 2
invokestatic Main/minloc([III)I
istore 4
aload 0
iload 4
iaload
istore 5
aload 0
iload 4
aload 0
iload 3
iaload
iastore
aload 0
iload 3
iload 5
iastore
iload 3
ldc 1
iadd
istore 3
goto Label8
Label11:
return
.end method

; >> FUNCTION main <<
.method public static main([Ljava/lang/String;)V
.throws java/io/IOException
.limit stack 32
.limit locals 32
ldc 0
istore 1
Label12:
iload 1
ldc 10
if_icmplt Label14
iconst_0
goto Label13
Label14:
iconst_1
Label13:
ifeq Label15
invokestatic Main/myRead()I
istore 2
getstatic Main/x [I
iload 1
iload 2
iastore
iload 1
ldc 1
iadd
istore 1
goto Label12
Label15:
getstatic Main/x [I
ldc 0
ldc 10
invokestatic Main/sort([III)V
ldc 0
istore 1
Label16:
iload 1
ldc 10
if_icmplt Label18
iconst_0
goto Label17
Label18:
iconst_1
Label17:
ifeq Label19
getstatic java/lang/System/out Ljava/io/PrintStream;
getstatic Main/x [I
iload 1
iaload
invokevirtual java/io/PrintStream/println(I)V
iload 1
ldc 1
iadd
istore 1
goto Label16
Label19:
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
ldc 10
newarray int
putstatic Main/x [I
return
.end method

