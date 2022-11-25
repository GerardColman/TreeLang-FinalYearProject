package Parser;

import Datatypes.Statements.*;
import Variables.VariableTypes;

import java.util.ArrayList;
import java.util.Objects;

/*
Generator.java takes in a line with a certain statement on it.
Parses that line. creates a corresponding statement object to be added to the AST
 */
public class Generator {

    public VariableDeclaration createVariableDeclaration(ArrayList<String> tokens){
        if(!Objects.equals(tokens.get(0), "var")){
            System.out.println("WRONG SYNTAX: Use var to declare variables");
            return null;
        }

        String name = tokens.get(1);
        String type = "";
        String value = tokens.get(3);

        //Determining type
        if(tokens.get(3).charAt(0) == '"' && tokens.get(3).charAt(tokens.get(3).length()-1) == '"'){ // String parsing
            type = VariableTypes.STRING.toString();
        }else if (Objects.equals(tokens.get(3), "True") || Objects.equals(tokens.get(3), "False")){ // Boolean parsing
            type = VariableTypes.BOOLEAN.toString();
        }else if(!tokens.get(3).contains("[a-zA-Z]+")){ // Int Parsing
            type = VariableTypes.INTEGER.toString();
        }

        return new VariableDeclaration(type, name, value);
    }

    public IfStatement createIfStatement(ArrayList<String> tokens){

        System.out.println(tokens.toString());
        String left = tokens.get(2);
        String right = tokens.get(4);
        String operator = null;

//        if(Objects.equals(tokens.get(3), "==")){
//            operator = "EQUALS";
//        }else if(Objects.equals(tokens.get(3), "!=")){
//            operator = "N_EQUALS";
//        }else if(Objects.equals(tokens.get(3), ">")){
//            operator = "GREATER_THAN";
//        }else if(Objects.equals(tokens.get(3), "<")){
//            operator = "LESS_THAN";
//        }else{
//            throw new IllegalArgumentException("IF CLAUSE IS NOT A BOOLEAN STATEMENT");
//        }

        switch (tokens.get(3)){
            case "==":
                operator = "EQUALS";
                break;
            case "!=":
                operator = "N_EQUALS";
                break;
            case ">":
                operator = "GREATER_THAN";
                break;
            case ">=":
                operator = "GREATER_THAN_EQUAL";
                break;
            case "<":
                operator = "LESS_THAN";
                break;
            case "<=":
                operator = "LESS_THAN_EQUAL";
                break;
            default:
                throw new IllegalArgumentException("IF CLAUSE IS NOT A BOOLEAN STATEMENT");
        }

        return new IfStatement(left, right, operator);
    }

    public IfStatement addStatementToIf(IfStatement current_if, Statement statement_to_add){

        if(statement_to_add instanceof Literal){
            throw new IllegalArgumentException();
        }else{
            current_if.addToBody(statement_to_add);
        }

        return current_if;
    }
}
