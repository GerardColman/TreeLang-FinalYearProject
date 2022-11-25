package Datatypes.Statements;

import Variables.VariableTypes;

import java.util.ArrayList;

public class IfStatement implements Statement{
    Sum clause_statement;

    public IfStatement(String left, String right, String Operator){
        clause_statement = new Sum(new Literal(left, VariableTypes.STRING.toString()), new Literal(right, VariableTypes.STRING.toString()), Operator);
    }
    ArrayList<Statement> body = new ArrayList<>();

    public void addToBody(Statement statement){
        body.add(statement);
    }

    public Sum getClause_statement() {
        return clause_statement;
    }

    public void setClause_statement(Sum clause_statement) {
        this.clause_statement = clause_statement;
    }

    public ArrayList<Statement> getBody() {
        return body;
    }

    public void setBody(ArrayList<Statement> body) {
        this.body = body;
    }


    @Override
    public String toString() {
        return "IfStatement{" +
                "clause_statement=" + clause_statement +
                ", body=" + body +
                '}';
    }
}
