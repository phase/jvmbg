package xyz.jadonfowler.jvmbg;

import java.util.*;
import org.objectweb.asm.*;

public class JVMMethod implements Opcodes {
    private ArrayList<MethodInstruction> instructions = new ArrayList<MethodInstruction>();
    private final int modifiers;
    private String description;
    JVMClass superClass;
    int variableCount = 0;

    public JVMMethod(String name, Modifiers... ms) {
        this(name, "()V", ms);
    }

    public JVMMethod(String name, String dec, Modifiers... ms) {
        int m = 0;
        for (Modifiers k : ms)
            m += k.toACC();
        this.modifiers = m;
        this.description = dec;
        //Start code creation for JIT compilation
        superClass.mv = superClass.cw.visitMethod(modifiers, name, description, null, null);
        superClass.mv.visitCode();
    }

    public JVMMethod addInstructions(MethodInstruction... is) {
        for (MethodInstruction i : is)
            instructions.add(i);
        return this;
    }

    public void createLocalVariable(LocalVariable v) {
        int argumentCount = description.split("(")[1].split(")")[0].length();
        switch (v.getType()) {
        case INT:
            superClass.mv.visitIntInsn(BIPUSH, (int) v.getValue());
            superClass.mv.visitVarInsn(ISTORE, variableCount);
        case STRING:
            superClass.mv.visitLdcInsn(v.getValue().toString());
            superClass.mv.visitVarInsn(ASTORE, variableCount);
        case BOOLEAN:
            if ((boolean) v.getValue()) superClass.mv.visitInsn(ICONST_1);
            else superClass.mv.visitInsn(ICONST_0);
            superClass.mv.visitVarInsn(ISTORE, variableCount);
        case CHAR:
            superClass.mv.visitIntInsn(BIPUSH, (int) ((char) v.getValue())); // Overcasting?
            superClass.mv.visitVarInsn(ISTORE, variableCount);
        case LONG:
            superClass.mv.visitLdcInsn((long) v.getValue());
            superClass.mv.visitVarInsn(LSTORE, variableCount);
        case FLOAT:
            superClass.mv.visitLdcInsn((float) v.getValue());
            superClass.mv.visitVarInsn(FSTORE, variableCount);
        case DOUBLE:
            superClass.mv.visitLdcInsn((double) v.getValue());
            superClass.mv.visitVarInsn(DSTORE, variableCount);
        default:
            break;
        }
        variableCount++;
    }

    // public JVMMethod changeLocalVariable(String identifier, Object value){
    public void createAsmMethod() {
        for (MethodInstruction i : instructions)
            i.run(this);
        superClass.mv.visitInsn(RETURN);
        int argumentCount = description.split("(")[1].split(")")[0].length();
        superClass.mv.visitMaxs(variableCount > 0 ? 1 : 0, variableCount + argumentCount);
        superClass.mv.visitEnd();
        superClass.mv = null;
    }
}
