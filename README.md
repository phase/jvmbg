#JVMBG
A JVM Bytecode Generator for JIT compilers.

JVMBG offers an easy-to-use API that can work with JIT, lazy JIT, or normal parsers.

Here's a sample of this awesomeness (*Note: This is a major WIP and won't work as expected right now!*):
```java
//Example
JVMClass clazz = new JVMClass("xyz/jadonfowler/derp/Test"); // public class Test extends java.lang.Object
clazz.addInstructions(
    // Class fields
    (c) -> c.createField(new Variable(IdentifierType.BOOLEAN, "isField", true)), // boolean isField = true
    (c) -> c.createConstructor() // finishes the constructor bytecode, should be called AFTER fields are added
);

JVMMethod method = new JVMMethod(clazz, "test", Modifiers.PUBLIC, Modifiers.STATIC); // public static void test()
method.addInstructions(
    (m) -> m.createLocalVariable(new Variable(IdentifierType.INT, "variable", 7)), // int variable = 7
    (m) -> m.changeLocalVariable("variable", 12)
    // Any lines in here get JIT compiled automatically, but you can only build one method at a time.
);

clazz.addMethod(method); // Finishes up the method bytecode
```
