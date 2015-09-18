package xyz.jadonfowler.jvmbg;

//import static org.objectweb.asm.Opcodes.*;

public enum IdentifierType{
    BYTE("B"), CHAR("C"), STRING("S"), INT("I"), LONG("J"), FLOAT("F"), DOUBLE("D"), BOOLEAN("Z"), VOID("V");
    String descriptor;

    IdentifierType(String s) {
        this.descriptor = s;
    }

    @Override public String toString() {
        return descriptor;
    }

    public static IdentifierType getIdentifier(Object o) {
        if (o instanceof Integer) return INT;
        else if (o instanceof Character) return CHAR;
        else if (o instanceof String) return STRING;
        else if (o instanceof Byte) return BYTE;
        else if (o instanceof Long) return LONG;
        else if (o instanceof Float) return FLOAT;
        else if (o instanceof Double) return DOUBLE;
        else if (o instanceof Boolean) return BOOLEAN;
        else return VOID;
    }
}
