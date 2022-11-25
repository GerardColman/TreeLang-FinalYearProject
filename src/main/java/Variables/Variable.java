package Variables;

public class Variable {
    String value;
    VariableTypes type;

    public Variable(String value, VariableTypes type) {
        this.value = value;
        this.type = type;
    }

    public Variable(){

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public VariableTypes getType() {
        return type;
    }

    public void setType(VariableTypes type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "value='" + value + '\'' +
                ", type=" + type +
                '}';
    }
}
