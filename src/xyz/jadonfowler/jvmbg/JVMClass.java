package xyz.jadonfowler.jvmbg;

import java.io.*;
import org.objectweb.asm.*;

public class JVMClass implements Opcodes {
    private final int modifiers;
    final String name;
    final String superClass;
    protected static ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    protected static FieldVisitor fv;
    protected static MethodVisitor mv;
    protected static AnnotationVisitor av0;

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
