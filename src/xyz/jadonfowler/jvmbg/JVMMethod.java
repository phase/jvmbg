package xyz.jadonfowler.jvmbg;

import java.util.*;
import org.objectweb.asm.*;

public class JVMMethod implements Opcodes {
    private final int modifiers;
    protected String description;
    protected String name;
    protected final ArrayList<Variable> variables = new ArrayList<Variable>();

    public JVMMethod(String name, Modifiers... ms) {
        this(name, "()V", ms);
    }

    public JVMMethod(String name, String dec, Modifiers... ms) {
        int m = 0;
        for (Modifiers k : ms)
            m |= k.toACC();
        this.modifiers = m;
        this.description = dec;
        this.name = name;
        // Start code creation for JIT compilation
        JVMClass.mv = JVMClass.cw.visitMethod(modifiers, name, description, null, null);
        JVMClass.mv.visitCode();
    }

    public void createLocalVariable(Variable v) {
        for (Variable vx : variables)
            if (vx.getIdentifier().equals(v.getIdentifier())) throw new IllegalArgumentException(
                    "There is already a variable in " + name + "() called " + vx.getIdentifier() + "!");
        variables.add(v);
        changeLocalVariable(v.getIdentifier(), v.getValue());
    }

    public void changeLocalVariable(String identifier, Object value) {
        Variable v = null;
        for (Variable vx : variables) {
            if (vx.getIdentifier().equals(identifier)) {
                v = vx;
                break;
            }
        }
        assert v == null : "Variable " + identifier + " not found in " + name + "()";
        int variableIndex = variables.indexOf(v);
        switch (v.getType()) {
        case INT:
            int vi = (int) value;
            if (Math.abs(vi) >= 128) JVMClass.mv.visitIntInsn(SIPUSH, vi);
            else JVMClass.mv.visitIntInsn(BIPUSH, vi);
            JVMClass.mv.visitVarInsn(ISTORE, variableIndex);
            break;
        case STRING:
            JVMClass.mv.visitLdcInsn(value.toString());
            JVMClass.mv.visitVarInsn(ASTORE, variableIndex);
            break;
        case BOOLEAN:
            if ((boolean) value) JVMClass.mv.visitInsn(ICONST_1);
            else JVMClass.mv.visitInsn(ICONST_0);
            JVMClass.mv.visitVarInsn(ISTORE, variableIndex);
            break;
        case CHAR:
            JVMClass.mv.visitIntInsn(BIPUSH, (int) ((char) value)); // Overcasting?
            JVMClass.mv.visitVarInsn(ISTORE, variableIndex);
            break;
        case LONG:
            JVMClass.mv.visitLdcInsn((long) value);
            JVMClass.mv.visitVarInsn(LSTORE, variableIndex);
            break;
        case FLOAT:
            JVMClass.mv.visitLdcInsn((float) value);
            JVMClass.mv.visitVarInsn(FSTORE, variableIndex);
            break;
        case DOUBLE:
            JVMClass.mv.visitLdcInsn((double) value);
            JVMClass.mv.visitVarInsn(DSTORE, variableIndex);
            break;
        default:
            break;
        }
    }

    public void createIf(int somehow_get_boolean, Runnable block) {
        // TODO
    }

    protected void build() {
        final int variableCount = variables.size();
        JVMClass.mv.visitInsn(RETURN);
        int argumentCount = description.split("\\(")[1].split("\\)")[0].length();
        JVMClass.mv.visitMaxs(variableCount > 0 ? 1 : 0, variableCount + argumentCount);
        JVMClass.mv.visitEnd();
        JVMClass.mv = null;
    }
}