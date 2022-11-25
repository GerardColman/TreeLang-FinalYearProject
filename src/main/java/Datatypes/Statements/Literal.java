package Datatypes.Statements;

public class Literal implements Statement{
    String value;
    String type;

    public Literal(){

    }
    public Literal(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Literal{" +
                "value='" + value + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
