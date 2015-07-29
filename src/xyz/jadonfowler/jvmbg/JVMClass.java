package xyz.jadonfowler.jvmbg;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class JVMClass implements Opcodes {
    private final int modifiers;
    private final String dec;
    private final String superClass;
    private JVMMethod[] methods = new JVMMethod[64]; // Need more?
    ClassWriter cw = new ClassWriter(0);
    FieldVisitor fv;
    MethodVisitor mv;
    AnnotationVisitor av0;

    public JVMClass(String dec) {
        this(dec, Modifiers.PUBLIC);
    }

    public JVMClass(String dec, Modifiers... ms) {
        this(dec, "java/lang/Object", ms);
    }

    public JVMClass(String dec, String superClass, Modifiers... ms) {
        int m = 0;
        for (Modifiers k : ms)
            m += k.toACC();
        this.modifiers = m;
        this.dec = dec.replace(".", "/");
        this.superClass = superClass.replace(".", "/");
        createAsmClass();
    }

    private void createAsmClass() {
        cw.visit(52, modifiers, dec, null, superClass, null);
        {
            // Constructor is Public, called '<init>', and returns void ('V')
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, superClass, "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        // cw.visitEnd();
        // return cw.toByteArray();
    }

    public void addMethod(JVMMethod m) {
        m.superClass = this;
        m.createAsmMethod();
    }
}
