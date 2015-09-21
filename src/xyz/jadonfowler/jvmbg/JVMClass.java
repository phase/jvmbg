package xyz.jadonfowler.jvmbg;

import java.io.*;
import org.objectweb.asm.*;

public class JVMClass implements Opcodes {
    private final int modifiers;
    private final String name;
    private final String superClass;
    static ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    static FieldVisitor fv;
    static MethodVisitor mv;
    static AnnotationVisitor av0;
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
        mv.visitMethodInsn(INVOKESPECIAL, this.superClass, "<init>", "()V", false);
    }

    /**
     * Should be called before constructor is closed!
     * 
     * @param v
     */
    public void createField(Variable v) {
        // Create Field
        fv = cw.visitField(0, v.getIdentifier(), v.getType().toString(), null, null);
        fv.visitEnd();
        mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitVarInsn(ALOAD, fieldCount);
        switch (v.getType()) {
        case INT:
            int value = (int) v.getValue();
            if (Math.abs(value) >= 128) mv.visitIntInsn(SIPUSH, value);
            else mv.visitIntInsn(BIPUSH, value);
        case STRING:
            mv.visitLdcInsn(v.getValue().toString());
        case BOOLEAN:
            if ((boolean) v.getValue()) mv.visitInsn(ICONST_1);
            else mv.visitInsn(ICONST_0);
        case CHAR:
            mv.visitIntInsn(BIPUSH, (int) ((char) v.getValue())); // Overcasting?
        case LONG:
            mv.visitLdcInsn((long) v.getValue());
        case FLOAT:
            mv.visitLdcInsn((float) v.getValue());
        case DOUBLE:
            mv.visitLdcInsn((double) v.getValue());
        default:
            break;
        }
        mv.visitFieldInsn(PUTFIELD, name, v.getIdentifier(), v.getType().toString());
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
        m.createAsmMethod();
        return this;
    }

    public void build() {
        cw.visitEnd();
        final byte[] classBytes = cw.toByteArray();
        try (FileOutputStream stream = new FileOutputStream(name + ".class")) {
            stream.write(classBytes);
            stream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
