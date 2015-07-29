package xyz.jadonfowler.jvmbg;

import org.objectweb.asm.Opcodes;

public class JVMMethod implements Opcodes {
    private final int modifiers;
    private final String name;
    private String dec;
    JVMClass superClass;

    public JVMMethod(String name, Modifiers... ms) {
        this(name, "()V", ms);
    }

    public JVMMethod(String name, String dec, Modifiers... ms) {
        int m = 0;
        for (Modifiers k : ms)
            m += k.toACC();
        this.modifiers = m;
        this.name = name;
        this.dec = dec;
    }

    public void createAsmMethod() {
        {
            superClass.mv = superClass.cw.visitMethod(modifiers, name, dec, null, null);
            superClass.mv.visitCode();
            superClass.mv.visitInsn(RETURN);
            superClass.mv.visitMaxs(0, 0);
            superClass.mv.visitEnd();
        }
    }
}
