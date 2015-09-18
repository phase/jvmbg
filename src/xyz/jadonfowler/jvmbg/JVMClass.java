package xyz.jadonfowler.jvmbg;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class JVMClass implements Opcodes {
    private final int modifiers;
    private final String name;
    private final String superClass;
    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    FieldVisitor fv;
    MethodVisitor mv;
    AnnotationVisitor av0;
    int fieldCount = 0;

    public JVMClass(String dec) {
        this(dec, Modifiers.PUBLIC);
    }

    public JVMClass(String name, Modifiers... ms) {
        this(name, "java/lang/Object", ms);
    }

    public JVMClass(String name, String superClass, Modifiers... ms) {
        int m = 0;
        for (Modifiers k : ms)
            m += k.toACC();
        this.modifiers = m;
        this.name = name.replace(".", "/");
        this.superClass = superClass.replace(".", "/");
        cw.visit(52, modifiers, name, null, superClass, null);
        // Constructor is Public, called '<init>', and returns void ('V')
        mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, superClass, "<init>", "()V", false);
    }

    public void addField(LocalVariable v){
        
    }
    
    
    /**
     * Should only be called once all fields have been added
     */
    public void createConstructor() {
        
        mv.visitInsn(RETURN);
        mv.visitMaxs(1 + fieldCount, 1);
        mv.visitEnd();
    }

    public JVMClass addMethod(JVMMethod m) {
        m.superClass = this;
        m.createAsmMethod();
        return this;
    }

    public void build(
            String output /* Directory? File? Just use JVMClass#name? */) {
        // TODO Output to file
    }
}
