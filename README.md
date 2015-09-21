#JVM Bytecode Generator
A JVM Bytecode Generator for compilers.

JVMBG offers an easy-to-use API that can work with lazy or normal compilers.

Here's a sample of this awesomeness (*Note: This is a major WIP and won't work as expected right now!*):
```java
//Example
JVMClass clazz = new JVMClass("xyz/jadonfowler/derp/Test"); // public class Test extends java.lang.Object

JVMConstructor constructor = new JVMConstructor(clazz, Modifiers.PUBLIC); // public Test()
constructor.addInstructions(
        // Creates a field called 'field' and sets it to 12
        () -> constructor.createField(new Variable(IdentifierType.INT, "field", 12))
        );
clazz.addMethod(constructor); // Finsishes up the constructor bytecode

JVMMethod method = new JVMMethod("test", Modifiers.PUBLIC, Modifiers.STATIC); // public static void test()
method.addInstructions(
    () -> method.createLocalVariable(new Variable(IdentifierType.INT, "variable", 7)), // int variable = 7
    () -> method.changeLocalVariable("variable", 12)
);

clazz.addMethod(method); // Finishes up the method bytecode
clazz.build(); // Output class files into local directory
```