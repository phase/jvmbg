package xyz.jadonfowler.jvmbg;

import static org.objectweb.asm.Opcodes.*;

public enum Modifiers {
    PUBLIC(ACC_PUBLIC), PRIVATE(ACC_PRIVATE), STATIC(ACC_STATIC);
    int m;

    private Modifiers(int m) {
        this.m = m;
    }

    public int toACC() {
        return m;
    }

    public static Modifiers from(String s) {
        if (s.equalsIgnoreCase("static")) return STATIC;
        else if (s.equalsIgnoreCase("public")) return PUBLIC;
        else if (s.equalsIgnoreCase("private")) return PRIVATE;
        else return null;
    }
}
