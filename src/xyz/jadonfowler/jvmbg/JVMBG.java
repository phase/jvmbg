package xyz.jadonfowler.jvmbg;


public class JVMBG {
    public static void main(String[] args) throws Exception {
        //Example
        JVMClass clazz = new JVMClass("Test"); // public class Test extends java.lang.Object
        JVMMethod method = new JVMMethod("test", Modifiers.PUBLIC, Modifiers.STATIC);
        method.addInstructions(
                (m) -> m.createLocalVariable(new LocalVariable(IdentifierType.INT, "variable", 7)), //int variable = 7
                (m) -> {}
                );
        clazz.addMethod(method);
        clazz.build("./");
    }
}
