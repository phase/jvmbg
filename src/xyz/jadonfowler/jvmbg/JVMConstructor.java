package xyz.jadonfowler.jvmbg;

public class JVMConstructor extends JVMMethod {
    private int fieldCount = 0;
    private final JVMClass superClass;

    public JVMConstructor(JVMClass superClass, Modifiers... ms) {
        this(superClass, "()V", ms);
    }

    public JVMConstructor(JVMClass superClass, String dec, Modifiers... ms) {
        super("<init>", dec, ms);
        this.superClass = superClass;
    }

    public void createField(Variable v) {
        // Create Field
        JVMClass.fv = JVMClass.cw.visitField(0, v.getIdentifier(), v.getType().toString(), null, null);
        JVMClass.fv.visitEnd();
        JVMClass.mv = JVMClass.cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        JVMClass.mv.visitVarInsn(ALOAD, fieldCount);
        switch (v.getType()) {
        case INT:
            int value = (int) v.getValue();
            if (Math.abs(value) >= 128) JVMClass.mv.visitIntInsn(SIPUSH, value);
            else JVMClass.mv.visitIntInsn(BIPUSH, value);
        case STRING:
            JVMClass.mv.visitLdcInsn(v.getValue().toString());
        case BOOLEAN:
            if ((boolean) v.getValue()) JVMClass.mv.visitInsn(ICONST_1);
            else JVMClass.mv.visitInsn(ICONST_0);
        case CHAR:
            JVMClass.mv.visitIntInsn(BIPUSH, (int) ((char) v.getValue())); // Overcasting?
        case LONG:
            JVMClass.mv.visitLdcInsn((long) v.getValue());
        case FLOAT:
            JVMClass.mv.visitLdcInsn((float) v.getValue());
        case DOUBLE:
            JVMClass.mv.visitLdcInsn((double) v.getValue());
        default:
            break;
        }
        JVMClass.mv.visitFieldInsn(PUTFIELD, superClass.name, v.getIdentifier(), v.getType().toString());
    }

    public void callSuperConstructor() {
        JVMClass.mv.visitMethodInsn(INVOKESPECIAL, this.superClass.superClass, "<init>", "()V", false);
    }

    @Override public void build() {
        JVMClass.mv.visitInsn(RETURN);
        int argumentCount = this.description.split("(")[1].split(")")[0].length();
        JVMClass.mv.visitMaxs(1 + fieldCount, 1 + variableCount + argumentCount);
        JVMClass.mv.visitEnd();
        JVMClass.mv = null;
    }
}
