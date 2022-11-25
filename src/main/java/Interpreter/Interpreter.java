package Interpreter;

import Interpreter.Operators.Addition;
import Interpreter.Operators.Division;
import Interpreter.Operators.Multiplication;
import Interpreter.Operators.Subtraction;
import Variables.Variable;
import Variables.VariableTypes;
import org.json.JSONArray;
import org.json.JSONObject;
import org.snt.inmemantlr.utils.FileUtils;

import java.util.HashMap;
import java.util.Objects;

public class Interpreter {
    HashMap<String, Variable> variables = new HashMap<>();
    Addition addition = new Addition();
    Subtraction subtraction = new Subtraction();
    Multiplication multiplication = new Multiplication();
    Division division = new Division();

    VariableTypes handleTermReturnType;
    int line_number = 0;


    /* Primary Function */
    public void interpret(String filename){

        /* Reading in the json parse tree */
        String s = FileUtils.loadFileContent(filename);
        JSONArray parseTree = new JSONArray(s);

        for(Object obj : parseTree){
            line_number++;
            JSONObject jsonObject = (JSONObject) obj;
            switch ((String) jsonObject.get("NODE_TYPE")){
                case "DECLARATION":
                    handleVar(jsonObject);
                    break;
                case "PRINTSTMT":
                    print(jsonObject);
                    break;
                case "ASSIGNMENT":
                    handleAssignments(jsonObject);
                    break;
                case "IFSTMT":
                    handleIfStatements(jsonObject);
                    break;
                case "WHILELOOP":
                    handleWhileLoop(jsonObject);
                    break;
                default:
                    System.out.println("ERROR (line " + line_number + ") : Unrecognised statement");
            }
        }

        //System.out.println(variables.toString());
    }


    private void handleAssignments(JSONObject jsonObject){
        if(variables.containsKey(jsonObject.get("VAR_NAME"))){

            // We know its a term if it has a right operand
            if(jsonObject.toMap().containsKey("RIGHT_OPERAND")){

                String var_name = jsonObject.getString("VAR_NAME");
                String left_operand = jsonObject.getString("LEFT_OPERAND");
                String right_operand = jsonObject.getString("RIGHT_OPERAND");
                String operation = jsonObject.getString("OPERATION");

                String term_result = handleTerm(left_operand, operation, right_operand);

                Variable updated_var = new Variable(term_result, handleTermReturnType);

                variables.remove(var_name);
                variables.put(var_name, updated_var);


            }else{
                String var_name = jsonObject.getString("VAR_NAME");
                String left_operand = jsonObject.getString("LEFT_OPERAND");

                if(variables.containsKey(left_operand)){
                    left_operand = variables.get(left_operand).getValue();
                }

                Variable updated_var = new Variable(left_operand, determineType(left_operand));

                variables.remove(var_name);
                variables.put(var_name, updated_var);
            }

        }else{
            System.out.println("ERROR (line " + line_number + ") : No variable name provided in assignment statement");
        }
    }

    private void print(JSONObject object){

        // Printing Booleans
        if(Objects.equals(object.get("NODE_VALUE"), "true") | Objects.equals(object.get("NODE_VALUE"), "false")){
            System.out.println(object.get("NODE_VALUE"));
            return;
        }

        String toPrint = object.getString("NODE_VALUE");
        if(toPrint.matches("[a-zA-Z]+")){
            if(variables.containsKey(toPrint)){
                System.out.println(variables.get(toPrint).getValue());
            }else{
                System.out.println(toPrint);
            }
        }else{
            System.out.println(toPrint);
        }


    }

    private void handleVar(JSONObject varObject){


        String varName = (String) varObject.get("VAR_NAME");


        if(varObject.has("VAR_VALUE")){
            String varValue = (String) varObject.get("VAR_VALUE");

            variables.put(varName, new Variable(varValue, determineType(varValue)));
        }else{

            variables.put(varName, new Variable("0", VariableTypes.INTEGER));
        }
    }

    private VariableTypes determineType(String value){

        //Integer
        if(value.matches("^-?|[1-9][0-9]*") || value.matches("0")){
            return VariableTypes.INTEGER;
        }

        //Float
        if(value.matches("^0.0|-?[1-9][0-9]*.[0-9]+")){
            return VariableTypes.FLOAT;
        }

        //Boolean
        if(value.equals("true") || value.equals("false")){
            return VariableTypes.BOOLEAN;
        }

        //String
        if(value.matches("[a-zA-Z]+")){
            return VariableTypes.STRING;
        }

        return null;
    }

    private String handleTerm(String leftTerm, String operation, String rightTerm){


        // Variable names take precedence over strings
        if(variables.containsKey(leftTerm)){
            leftTerm = variables.get(leftTerm).getValue();
        }

        if(variables.containsKey(rightTerm)){
            rightTerm = variables.get(rightTerm).getValue();
        }

        VariableTypes leftType = determineType(leftTerm);
        VariableTypes rightType = determineType(rightTerm);

        switch (operation){
            case "+":{
                if(leftType == VariableTypes.STRING && rightType == VariableTypes.STRING){
                    handleTermReturnType = VariableTypes.STRING;
                    return addition.stringPlusString(leftTerm, rightTerm);
                }

                if(leftType == VariableTypes.STRING && rightType == VariableTypes.INTEGER){
                    handleTermReturnType = VariableTypes.STRING;
                    return addition.stringPlusInt(leftTerm, Integer.parseInt(rightTerm));
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.STRING){
                    handleTermReturnType = VariableTypes.STRING;
                    return addition.intPlusString(Integer.parseInt(leftTerm), rightTerm);
                }

                if(leftType == VariableTypes.STRING && rightType == VariableTypes.BOOLEAN){
                    handleTermReturnType = VariableTypes.STRING;
                    return addition.stringPlusBool(leftTerm, Boolean.parseBoolean(rightTerm));
                }

                if(leftType == VariableTypes.STRING && rightType == VariableTypes.FLOAT){
                    handleTermReturnType = VariableTypes.STRING;
                    return addition.stringPlusFloat(leftTerm, Float.parseFloat(rightTerm));
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.BOOLEAN){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString(addition.intPlusBool(Integer.parseInt(leftTerm), Boolean.parseBoolean(rightTerm)));
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.FLOAT){
                    handleTermReturnType = VariableTypes.FLOAT;
                    return Float.toString(addition.intPlusFloat(Integer.parseInt(leftTerm), Float.parseFloat(rightTerm)));
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.INTEGER){
                    handleTermReturnType = VariableTypes.INTEGER;
                    return Integer.toString(addition.intPlusInt(Integer.parseInt(leftTerm), Integer.parseInt(rightTerm)));
                }

                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.BOOLEAN){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString((Boolean.parseBoolean(leftTerm)) || (Boolean.parseBoolean(rightTerm)));
                }

                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.FLOAT){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString(addition.boolPlusFloat(Boolean.parseBoolean(leftTerm), Float.parseFloat(rightTerm)));
                }
            }
            case "-":{
                if(leftType == VariableTypes.STRING && rightType == VariableTypes.STRING){
                    handleTermReturnType = VariableTypes.STRING;
                    return subtraction.stringMinusString(leftTerm, rightTerm);
                }

                if(leftType == VariableTypes.STRING && rightType == VariableTypes.INTEGER){
                    handleTermReturnType = VariableTypes.STRING;
                    return subtraction.stringMinusInt(leftTerm, Integer.parseInt(rightTerm));
                }

                if(leftType == VariableTypes.STRING && rightType == VariableTypes.BOOLEAN){
                    handleTermReturnType = VariableTypes.STRING;
                    return subtraction.stringMinusBoolean(leftTerm, Boolean.parseBoolean(rightTerm));
                }

                if(leftType == VariableTypes.STRING && rightType == VariableTypes.FLOAT){
                    handleTermReturnType = VariableTypes.STRING;
                    return subtraction.stringMinusFloat(leftTerm, Float.parseFloat(rightTerm));
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.INTEGER){
                    handleTermReturnType = VariableTypes.INTEGER;
                    return Integer.toString(subtraction.intMinusInt(Integer.parseInt(leftTerm), Integer.parseInt(rightTerm)));
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.STRING){
                    handleTermReturnType = VariableTypes.INTEGER;
                    return Integer.toString(subtraction.intMinusString(Integer.parseInt(leftTerm), rightTerm));
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.BOOLEAN){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString(subtraction.intMinusBool(Integer.parseInt(leftTerm), Boolean.parseBoolean(rightTerm)));
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.FLOAT){
                    handleTermReturnType = VariableTypes.FLOAT;
                    return Float.toString(subtraction.intMinusFloat(Integer.parseInt(leftTerm), Float.parseFloat(rightTerm)));
                }

                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.BOOLEAN){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString(subtraction.boolMinusBool(Boolean.parseBoolean(leftTerm), Boolean.parseBoolean(rightTerm)));
                }

                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.FLOAT){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString(subtraction.boolMinusFloat(Boolean.parseBoolean(leftTerm), Float.parseFloat(rightTerm)));
                }

                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.INTEGER){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString(subtraction.boolMinusInt(Boolean.parseBoolean(leftTerm), Integer.parseInt(rightTerm)));
                }

                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.STRING){
                    handleTermReturnType = VariableTypes.STRING;
                    return subtraction.boolMinusString(Boolean.parseBoolean(leftTerm), rightTerm);
                }

                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.FLOAT){
                    handleTermReturnType = VariableTypes.FLOAT;
                    return Float.toString(subtraction.floatMinusFloat(Float.parseFloat(leftTerm), Float.parseFloat(rightTerm)));
                }

                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.INTEGER){
                    handleTermReturnType = VariableTypes.FLOAT;
                    return Float.toString(subtraction.floatMinusInt(Float.parseFloat(leftTerm), Integer.parseInt(rightTerm)));
                }

                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.STRING){
                    handleTermReturnType = VariableTypes.STRING;
                    return subtraction.floatMinusString(Float.parseFloat(leftTerm), rightTerm);
                }

                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.BOOLEAN){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString(subtraction.floatMinusBool(Float.parseFloat(leftTerm), Boolean.parseBoolean(rightTerm)));
                }
            }
            case "*":{
                if(leftType == VariableTypes.STRING && rightType == VariableTypes.STRING){
                    handleTermReturnType = VariableTypes.STRING;
                    return multiplication.stringTimesString(leftTerm,rightTerm);
                }

                // str * int - DONE
                if(leftType == VariableTypes.STRING && rightType == VariableTypes.INTEGER){
                    handleTermReturnType = VariableTypes.STRING;
                    return multiplication.stringTimesInt(leftTerm, Integer.parseInt(rightTerm));
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.STRING){
                    handleTermReturnType = VariableTypes.STRING;
                    return multiplication.stringTimesInt(rightTerm, Integer.parseInt(leftTerm));
                }

                // str * bool - DONE
                if(leftType == VariableTypes.STRING && rightType == VariableTypes.BOOLEAN){
                    handleTermReturnType = VariableTypes.STRING;
                    return multiplication.stringTimesBool(leftTerm, Boolean.parseBoolean(rightTerm));
                }

                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.STRING){
                    handleTermReturnType = VariableTypes.STRING;
                    return multiplication.stringTimesBool(rightTerm, Boolean.parseBoolean(leftTerm));
                }

                // str + float - DONE
                if(leftType == VariableTypes.STRING && rightType == VariableTypes.FLOAT){
                    handleTermReturnType = VariableTypes.STRING;
                    return multiplication.stringTimesFloat(leftTerm, Float.parseFloat(rightTerm));
                }

                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.STRING){
                    handleTermReturnType = VariableTypes.STRING;
                    return multiplication.stringTimesFloat(rightTerm, Float.parseFloat(leftTerm));
                }

                // int + int
                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.INTEGER){
                    handleTermReturnType = VariableTypes.INTEGER;
                    return Integer.toString(multiplication.intTimesInt(Integer.parseInt(leftTerm), Integer.parseInt(rightTerm)));
                }

                // Bool + bool
                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.BOOLEAN){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString(multiplication.boolTimesBool(Boolean.parseBoolean(leftTerm), Boolean.parseBoolean(rightTerm)));
                }

                // Bool + float
                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.FLOAT){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString(multiplication.boolTimesFloat(Boolean.parseBoolean(leftTerm), Float.parseFloat(rightTerm)));
                }

                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.BOOLEAN){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString(multiplication.boolTimesFloat(Boolean.parseBoolean(rightTerm), Float.parseFloat(leftTerm)));
                }

                // Bool + Int
                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.INTEGER){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString(multiplication.boolTimesInt(Boolean.parseBoolean(leftTerm), Integer.parseInt(rightTerm)));
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.BOOLEAN){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString(multiplication.boolTimesInt(Boolean.parseBoolean(rightTerm), Integer.parseInt(leftTerm)));
                }

                //float * float
                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.FLOAT){
                    handleTermReturnType = VariableTypes.FLOAT;
                    return Float.toString(multiplication.floatTimesfloat(Float.parseFloat(leftTerm), Float.parseFloat(rightTerm)));
                }

                // Float * int
                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.INTEGER){
                    handleTermReturnType = VariableTypes.FLOAT;
                    return Float.toString(multiplication.floatTimesInt(Float.parseFloat(leftTerm), Integer.parseInt(rightTerm)));
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.FLOAT){
                    handleTermReturnType = VariableTypes.FLOAT;
                    return Float.toString(multiplication.floatTimesInt(Float.parseFloat(rightTerm), Integer.parseInt(leftTerm)));
                }

            }
            case "/":{

                // str / str
                if(leftType == VariableTypes.STRING && rightType == VariableTypes.STRING){
                    handleTermReturnType = VariableTypes.STRING;
                    return division.stringDivideString(leftTerm,rightTerm);
                }

                // str / int
                if(leftType == VariableTypes.STRING && rightType == VariableTypes.INTEGER){
                    handleTermReturnType = VariableTypes.STRING;
                    return division.stringDivideInt(leftTerm,Integer.parseInt(rightTerm));
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.STRING){
                    handleTermReturnType = VariableTypes.STRING;
                    return division.stringDivideInt(rightTerm,Integer.parseInt(leftTerm));
                }

                // str / bool
                if(leftType == VariableTypes.STRING && rightType == VariableTypes.BOOLEAN){
                    handleTermReturnType = VariableTypes.STRING;
                    return division.stringDivideBool(leftTerm,Boolean.parseBoolean(rightTerm));
                }

                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.STRING){
                    handleTermReturnType = VariableTypes.STRING;
                    return division.stringDivideBool(rightTerm,Boolean.parseBoolean(leftTerm));
                }

                // str / float
                if(leftType == VariableTypes.STRING && rightType == VariableTypes.FLOAT){
                    handleTermReturnType = VariableTypes.STRING;
                    return division.stringDivideFloat(leftTerm,Float.parseFloat(rightTerm));
                }

                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.STRING){
                    handleTermReturnType = VariableTypes.STRING;
                    return division.stringDivideFloat(rightTerm,Float.parseFloat(leftTerm));
                }

                // Int / int
                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.INTEGER){
                    handleTermReturnType = VariableTypes.INTEGER;
                    return Integer.toString(division.intDivideInt(Integer.parseInt(leftTerm), Integer.parseInt(rightTerm)));
                }

                // bool / bool
                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.BOOLEAN){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString(division.booleanDivideBoolean(Boolean.parseBoolean(leftTerm), Boolean.parseBoolean(rightTerm)));
                }

                // Bool / Float
                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.FLOAT){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString(division.boolDivideFloat(Boolean.parseBoolean(leftTerm), Float.parseFloat(rightTerm)));
                }

                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.BOOLEAN){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString(division.boolDivideFloat(Boolean.parseBoolean(rightTerm), Float.parseFloat(leftTerm)));
                }

                // bool / int
                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.INTEGER){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString(division.boolDivideInt(Boolean.parseBoolean(leftTerm), Integer.parseInt(rightTerm)));
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.BOOLEAN){
                    handleTermReturnType = VariableTypes.BOOLEAN;
                    return Boolean.toString(division.boolDivideInt(Boolean.parseBoolean(rightTerm), Integer.parseInt(leftTerm)));
                }

                // Float / Float
                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.FLOAT){
                    handleTermReturnType = VariableTypes.FLOAT;
                    return Float.toString(division.floatDividesFloat(Float.parseFloat(leftTerm), Float.parseFloat(rightTerm)));
                }

                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.INTEGER){
                    handleTermReturnType = VariableTypes.FLOAT;
                    return Float.toString(division.floatDividesInt(Float.parseFloat(leftTerm), Integer.parseInt(rightTerm)));
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.FLOAT){
                    handleTermReturnType = VariableTypes.FLOAT;
                    return Float.toString(division.floatDividesInt(Float.parseFloat(rightTerm), Integer.parseInt(leftTerm)));
                }
            }
            default:{
                System.out.println("ERROR (line " + line_number + ") : Operation not recognised, valid operations are (+, -, *, /)");
                return null;
            }
        }


    }

    private void handleIfStatements(JSONObject jsonObject){
        String leftTerm = jsonObject.getString("LEFT_TERM");
        String equality = jsonObject.getString("EQUALITY");
        String rightTerm = jsonObject.getString("RIGHT_TERM");
        JSONArray statements = jsonObject.getJSONArray("INTERNAL_STATEMENTS");

        if(handleBooleanTerm(leftTerm, equality, rightTerm)){
            handleInternalStatements(statements);
        }
    }

    private void handleWhileLoop(JSONObject jsonObject){
        String leftTerm = jsonObject.getString("LEFT_TERM");
        String equality = jsonObject.getString("EQUALITY");
        String rightTerm = jsonObject.getString("RIGHT_TERM");
        JSONArray statements = jsonObject.getJSONArray("INTERNAL_STATEMENTS");


        while(handleBooleanTerm(leftTerm, equality, rightTerm)){
            handleInternalStatements(statements);
        }
    }
    private boolean handleBooleanTerm(String leftTerm, String Equality, String rightTerm){

        if(variables.containsKey(leftTerm)){
            leftTerm = variables.get(leftTerm).getValue();
        }

        if(variables.containsKey(rightTerm)){
            rightTerm = variables.get(rightTerm).getValue();
        }

        VariableTypes leftType = determineType(leftTerm);
        VariableTypes rightType = determineType(rightTerm);

        switch (Equality){
            case "==":{
                if(leftType == VariableTypes.STRING && rightType == VariableTypes.STRING){
                    return Objects.equals(leftTerm, rightTerm);
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.INTEGER){
                    return Integer.parseInt(leftTerm) == Integer.parseInt(rightTerm);
                }

                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.BOOLEAN){
                    return Boolean.parseBoolean(leftTerm) == Boolean.parseBoolean(rightTerm);
                }

                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.FLOAT){
                    return Float.parseFloat(leftTerm) == Float.parseFloat(rightTerm);
                }
            }

            case "!=":{
                if(leftType == VariableTypes.STRING && rightType == VariableTypes.STRING){
                    return !Objects.equals(leftTerm, rightTerm);
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.INTEGER){
                    return Integer.parseInt(leftTerm) != Integer.parseInt(rightTerm);
                }

                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.BOOLEAN){
                    return Boolean.parseBoolean(leftTerm) != Boolean.parseBoolean(rightTerm);
                }

                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.FLOAT){
                    return Float.parseFloat(leftTerm) != Float.parseFloat(rightTerm);
                }
            }

            case ">=":{
                if(leftType == VariableTypes.STRING && rightType == VariableTypes.STRING){
                    return (leftTerm.length() >= rightTerm.length());
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.INTEGER){
                    return Integer.parseInt(leftTerm) >= Integer.parseInt(rightTerm);
                }

                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.BOOLEAN){
                    System.out.println("ERROR (line " + line_number + ") : Can't use >= with boolean values");
                }

                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.FLOAT){
                    return Float.parseFloat(leftTerm) >= Float.parseFloat(rightTerm);
                }
            }

            case "<=":{
                if(leftType == VariableTypes.STRING && rightType == VariableTypes.STRING){
                    return (leftTerm.length() <= rightTerm.length());
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.INTEGER){
                    return Integer.parseInt(leftTerm) <= Integer.parseInt(rightTerm);
                }

                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.BOOLEAN){
                    System.out.println("ERROR (line " + line_number + ") : Can't use <= with boolean values");
                }

                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.FLOAT){
                    return Float.parseFloat(leftTerm) <= Float.parseFloat(rightTerm);
                }
            }

            case ">":{
                if(leftType == VariableTypes.STRING && rightType == VariableTypes.STRING){
                    return (leftTerm.length() > rightTerm.length());
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.INTEGER){
                    return Integer.parseInt(leftTerm) > Integer.parseInt(rightTerm);
                }

                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.BOOLEAN){
                    System.out.println("ERROR (line " + line_number + ") : Can't use > with boolean values");
                }

                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.FLOAT){
                    return Float.parseFloat(leftTerm) > Float.parseFloat(rightTerm);
                }
            }

            case "<":{
                if(leftType == VariableTypes.STRING && rightType == VariableTypes.STRING){
                    return (leftTerm.length() < rightTerm.length());
                }

                if(leftType == VariableTypes.INTEGER && rightType == VariableTypes.INTEGER){
                    return Integer.parseInt(leftTerm) < Integer.parseInt(rightTerm);
                }

                if(leftType == VariableTypes.BOOLEAN && rightType == VariableTypes.BOOLEAN){
                    System.out.println("ERROR (line " + line_number + ") : Can't use < with boolean values");
                }

                if(leftType == VariableTypes.FLOAT && rightType == VariableTypes.FLOAT){
                    return Float.parseFloat(leftTerm) < Float.parseFloat(rightTerm);
                }
            }

            default:{
                return false;
            }

        }
    }

    private void handleInternalStatements(JSONArray statements){

        for(Object obj : statements){
            JSONObject jsonObject = (JSONObject) obj;
            switch ((String) jsonObject.get("NODE_TYPE")){
                case "DECLARATION":
                    handleVar(jsonObject);
                    break;
                case "PRINTSTMT":
                    print(jsonObject);
                    break;
                case "ASSIGNMENT":
                    handleAssignments(jsonObject);
                    break;
                case "IFSTMT":
                    handleIfStatements(jsonObject);
                    break;
                case "WHILELOOP":
                    handleWhileLoop(jsonObject);
                    break;
            }
        }
    }
}
