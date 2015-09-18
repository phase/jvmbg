package xyz.jadonfowler.jvmbg;


public class JVMBG {
    public static void main(String[] args) throws Exception {
        //Example
        JVMClass clazz = new JVMClass("Test"); // public class Test extends java.lang.Object
        JVMMethod method = new JVMMethod("test", Modifiers.PUBLIC, Modifiers.STATIC);
        method.addInstructions(
                (x) -> x.createLocalVariable(new LocalVariable(IdentifierType.INT, "variable", 7))
                );
        clazz.addMethod(method);
        clazz.build("./");
    }
}
