package xyz.jadonfowler.jvmbg;

import org.objectweb.asm.Opcodes;

public enum Modifiers implements Opcodes {
    PUBLIC(ACC_PUBLIC), PRIVATE(ACC_PRIVATE), STATIC(ACC_STATIC);
    int m;

    private Modifiers(int m) {
        this.m = m;
    }

    public int toACC() {
        return m;
    }
}
