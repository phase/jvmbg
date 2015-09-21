#JVM Bytecode Generator
A JVM Bytecode Generator for compilers.

JVMBG offers an easy-to-use API that can work with lazy or normal compilers.

Here's a sample of this awesomeness (*Note: This is a major WIP and won't work as expected right now!*):
```java
//Example
JVMClass clazz = new JVMClass("xyz/jadonfowler/derp/Test"); // public class Test extends java.lang.Object
clazz.addInstructions(
    // Class fields
    () -> clazz.createField(new Variable(IdentifierType.BOOLEAN, "isField", true)), // boolean isField = true
    () -> clazz.createConstructor() // finishes the constructor bytecode, should be called AFTER fields are added
);

JVMMethod method = new JVMMethod("test", Modifiers.PUBLIC, Modifiers.STATIC); // public static void test()
method.addInstructions(
    () -> method.createLocalVariable(new Variable(IdentifierType.INT, "variable", 7)), // int variable = 7
    () -> method.changeLocalVariable("variable", 12)
    // Any lines in here get JIT compiled automatically, but you can only build one method at a time.
);

clazz.addMethod(method); // Finishes up the method bytecode
clazz.build(); // Output class files into local directory
```