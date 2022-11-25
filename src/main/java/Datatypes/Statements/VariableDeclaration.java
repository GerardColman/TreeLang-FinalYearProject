package Datatypes.Statements;

public class VariableDeclaration implements Statement {
    String Type;
    String Identifier;
    String Value;

    public VariableDeclaration(String Type, String Identifier, String Value){
        this.Type = Type;
        this.Identifier = Identifier;
        this.Value = Value;
    }

    public VariableDeclaration(){

    }

    @Override
    public String toString() {
        return "VariableDeclaration{" +
                "Type='" + Type + '\'' +
                ", Identifier='" + Identifier + '\'' +
                ", Value='" + Value + '\'' +
                '}';
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getIdentifier() {
        return Identifier;
    }

    public void setIdentifier(String identifier) {
        Identifier = identifier;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
