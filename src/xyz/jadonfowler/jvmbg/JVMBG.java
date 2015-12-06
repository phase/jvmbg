package xyz.jadonfowler.jvmbg;

public class JVMBG {
    public static void main(String[] args) throws Exception {
        // Example
        JVMClass clazz = new JVMClass("xyz/jadonfowler/Test"); // public class Test extends java.lang.Object
        
        JVMConstructor constructor = new JVMConstructor(clazz, Modifiers.PUBLIC);
        constructor.createField(new Variable(IdentifierType.INT, "field", 12));
        
        clazz.addMethod(constructor);
        
        JVMMethod method = new JVMMethod("test", Modifiers.PUBLIC, Modifiers.STATIC);
        //int variable = 7
        method.createLocalVariable(new Variable(IdentifierType.INT, "variable", 7));
        
        clazz.addMethod(method);
        clazz.build();
    }
}
