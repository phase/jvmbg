package xyz.jadonfowler.jvmbg;

public class JVMConstructor extends JVMMethod {
    private int fieldCount = 0;
    private final JVMClass superClass;
    private boolean finishedFields = false;

    public JVMConstructor(JVMClass superClass, Modifiers... ms) {
        this(superClass, "()V", ms);
    }

    public JVMConstructor(JVMClass superClass, String dec, Modifiers... ms) {
        super("<init>", dec, ms);
        JVMClass.mv.visitVarInsn(ALOAD, 0);
        this.superClass = superClass;
    }

    public void createField(Variable v) {
        assert!finishedFields : "Fields can not be added after they have been finished!";
        // Create Field
        JVMClass.fv = JVMClass.cw.visitField(0, v.getIdentifier(), v.getType().toString(), null, null);
        JVMClass.fv.visitEnd();
        JVMClass.mv.visitVarInsn(ALOAD, 0);
        // Push value
        switch (v.getType()) {
        case INT:
            int value = (int) v.getValue();
            if (Math.abs(value) >= 128) JVMClass.mv.visitIntInsn(SIPUSH, value);
            else JVMClass.mv.visitIntInsn(BIPUSH, value);
            break;
        case STRING:
            JVMClass.mv.visitLdcInsn(v.getValue().toString());
            break;
        case BOOLEAN:
            if ((boolean) v.getValue()) JVMClass.mv.visitInsn(ICONST_1);
            else JVMClass.mv.visitInsn(ICONST_0);
            break;
        case CHAR:
            JVMClass.mv.visitIntInsn(BIPUSH, (int) ((char) v.getValue())); // Overcasting?
            break;
        case LONG:
            JVMClass.mv.visitLdcInsn((long) v.getValue());
            break;
        case FLOAT:
            JVMClass.mv.visitLdcInsn((float) v.getValue());
            break;
        case DOUBLE:
            JVMClass.mv.visitLdcInsn((double) v.getValue());
            break;
        default:
            break;
        }
        // Pop value and store into field
         System.out.println("Field in `" + superClass.name + "` with name of `" +
         v.getIdentifier() + "` of type `" + v.getType().toString() + "` with value `" + v.getValue() + "`");
        JVMClass.mv.visitFieldInsn(PUTFIELD, superClass.name, v.getIdentifier(), v.getType().toString());
        fieldCount++;
    }

    public void finishFields() {
        this.finishedFields = true;
    }

    public void callSuperConstructor() {
        JVMClass.mv.visitVarInsn(ALOAD, 0);
        JVMClass.mv.visitMethodInsn(INVOKESPECIAL, this.superClass.superClass, "<init>", "()V", false);
    }

    @Override public void build() {
        JVMClass.mv.visitInsn(RETURN);
        int argumentCount = this.description.split("\\(")[1].split("\\)")[0].length();
        System.out.println("Constructor Maxes: " + (1 + fieldCount) + "," + (1 + this.variables.size() + argumentCount));
        JVMClass.mv.visitMaxs(1 + fieldCount, 1 + this.variables.size() + argumentCount);
        JVMClass.mv.visitEnd();
        JVMClass.mv = null;
    }
}
