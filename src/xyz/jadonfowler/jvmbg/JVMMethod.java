package xyz.jadonfowler.jvmbg;

import java.util.*;
import org.objectweb.asm.*;

public class JVMMethod implements Opcodes {
    private HashMap<Variable, Integer> variables = new HashMap<Variable, Integer>();
    private final int modifiers;
    private final String name;
    private String description;
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
        this.description = dec;
    }

    public JVMMethod createLocalVariable(Variable v) {
        int argumentCount = description.split("(")[1].split(")")[0].length();
        variables.put(v, variables.keySet().size() + argumentCount);
        return this;
    }

    // public JVMMethod changeLocalVariable(String identifier, Object value){
    public void createAsmMethod() {
        {
            superClass.mv = superClass.cw.visitMethod(modifiers, name, description, null, null);
            superClass.mv.visitCode();
            for (Variable v : variables.keySet()) {
                int order = variables.get(v);
                switch (v.getType()) {
                case INT:
                    superClass.mv.visitIntInsn(BIPUSH, (int) v.getValue());
                    superClass.mv.visitVarInsn(ISTORE, order);
                case STRING:
                    superClass.mv.visitLdcInsn(v.getValue().toString());
                    superClass.mv.visitVarInsn(ASTORE, order);
                case BOOLEAN:
                    if ((boolean) v.getValue()) {
                        superClass.mv.visitInsn(ICONST_1);
                        superClass.mv.visitVarInsn(ISTORE, order);
                    }
                    else {
                        superClass.mv.visitInsn(ICONST_0);
                        superClass.mv.visitVarInsn(ISTORE, order);
                    }
                case CHAR:
                    superClass.mv.visitIntInsn(BIPUSH, (int) ((char) v.getValue())); // Overcasting?
                    superClass.mv.visitVarInsn(ISTORE, order);
                case LONG:
                    superClass.mv.visitLdcInsn((long) v.getValue());
                    superClass.mv.visitVarInsn(LSTORE, order);
                case FLOAT:
                    superClass.mv.visitLdcInsn((float) v.getValue());
                    superClass.mv.visitVarInsn(FSTORE, order);
                default:
                    break;
                }
            }
            superClass.mv.visitInsn(RETURN);
            int argumentCount = description.split("(")[1].split(")")[0].length();
            int variableCount = variables.keySet().size();
            superClass.mv.visitMaxs(variableCount > 0 ? 1 : 0, variableCount + argumentCount);
            superClass.mv.visitEnd();
            superClass.mv = null;
        }
    }
}
