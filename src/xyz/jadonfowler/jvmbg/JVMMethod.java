package xyz.jadonfowler.jvmbg;

import org.objectweb.asm.*;

public class JVMMethod implements Opcodes {
    private final int modifiers;
    protected String description;
    int variableCount = 0;

    public JVMMethod(String name, Modifiers... ms) {
        this(name, "()V", ms);
    }

    public JVMMethod(String name, String dec, Modifiers... ms) {
        int m = 0;
        for (Modifiers k : ms)
            m |= k.toACC();
        this.modifiers = m;
        this.description = dec;
        // Start code creation for JIT compilation
        JVMClass.mv = JVMClass.cw.visitMethod(modifiers, name, description, null, null);
        JVMClass.mv.visitCode();
    }

    public JVMMethod addInstructions(Runnable... is) {
        for (Runnable i : is)
            i.run();
        return this;
    }

    public void createLocalVariable(Variable v) {
        switch (v.getType()) {
        case INT:
            int value = (int) v.getValue();
            if (Math.abs(value) >= 128) JVMClass.mv.visitIntInsn(SIPUSH, value);
            else JVMClass.mv.visitIntInsn(BIPUSH, value);
            JVMClass.mv.visitVarInsn(ISTORE, variableCount);
        case STRING:
            JVMClass.mv.visitLdcInsn(v.getValue().toString());
            JVMClass.mv.visitVarInsn(ASTORE, variableCount);
        case BOOLEAN:
            if ((boolean) v.getValue()) JVMClass.mv.visitInsn(ICONST_1);
            else JVMClass.mv.visitInsn(ICONST_0);
            JVMClass.mv.visitVarInsn(ISTORE, variableCount);
        case CHAR:
            JVMClass.mv.visitIntInsn(BIPUSH, (int) ((char) v.getValue())); // Overcasting?
            JVMClass.mv.visitVarInsn(ISTORE, variableCount);
        case LONG:
            JVMClass.mv.visitLdcInsn((long) v.getValue());
            JVMClass.mv.visitVarInsn(LSTORE, variableCount);
        case FLOAT:
            JVMClass.mv.visitLdcInsn((float) v.getValue());
            JVMClass.mv.visitVarInsn(FSTORE, variableCount);
        case DOUBLE:
            JVMClass.mv.visitLdcInsn((double) v.getValue());
            JVMClass.mv.visitVarInsn(DSTORE, variableCount);
        default:
            break;
        }
        variableCount++;
    }

    // public JVMMethod changeLocalVariable(String identifier, Object value){
    
    public void createAsmMethod() {
        JVMClass.mv.visitInsn(RETURN);
        int argumentCount = description.split("(")[1].split(")")[0].length();
        JVMClass.mv.visitMaxs(variableCount > 0 ? 1 : 0, variableCount + argumentCount);
        JVMClass.mv.visitEnd();
        JVMClass.mv = null;
    }
}
