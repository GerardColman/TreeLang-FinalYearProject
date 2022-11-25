package Datatypes.Statements;

public class Sum implements Statement{
    String operator;

    /*
    These can only be a Sum or a Literal
     */

    Statement Left;
    Statement Right;

    public Sum(Literal left, Literal right, String operator){
        this.Left = left;
        this.Right = right;
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "Sum{" +
                "operator='" + operator + '\'' +
                ", Left=" + Left +
                ", Right=" + Right +
                '}';
    }
}
