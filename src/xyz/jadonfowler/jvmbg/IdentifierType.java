package xyz.jadonfowler.jvmbg;

import org.objectweb.asm.*;

public enum IdentifierType implements Opcodes {
    BYTE("B"), CHAR("C"), STRING("S"), INT("I"), LONG("J"), FLOAT("F"), DOUBLE("D"), BOOLEAN("Z"), VOID("V");
    String descriptor;

    IdentifierType(String s) {
        this.descriptor = s;
    }

    @Override public String toString() {
        return descriptor;
    }
}
