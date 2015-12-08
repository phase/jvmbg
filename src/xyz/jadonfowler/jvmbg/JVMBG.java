package xyz.jadonfowler.jvmbg;

public class JVMBG {
    public static void main(String[] args) throws Exception {
        // Example
        System.out.println("JVMBG Test");
        JVMClass clazz = new JVMClass("Test"); // public class Test extends java.lang.Object
        JVMConstructor constructor = new JVMConstructor(clazz, Modifiers.PUBLIC);
        constructor.createField(new Variable(IdentifierType.INT, "field", 12));
        constructor.finishFields();
        clazz.addMethod(constructor);
        
        JVMMethod method = new JVMMethod("test", Modifiers.PUBLIC, Modifiers.STATIC);
        //int variable = 7
        method.createLocalVariable(new Variable(IdentifierType.INT, "variable", 7));
        method.changeLocalVariable("variable", 12);
        clazz.addMethod(method);
        
        clazz.build();
    }
}
