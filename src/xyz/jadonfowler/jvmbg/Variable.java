package xyz.jadonfowler.jvmbg;

public class Variable {
    private String identifier;
    private Object value;
    private IdentifierType type;

    public Variable(IdentifierType type, String identifier, Object value) {
        this.identifier = identifier;
        this.type = type;
        this.value = value;
    }

    public String getIdentifer() {
        return identifier;
    }

    public IdentifierType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
