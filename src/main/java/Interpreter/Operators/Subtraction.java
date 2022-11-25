package Interpreter.Operators;

public class Subtraction {

    public String stringMinusString(String A, String B){
        return subtractCommonLetters(A, B);
    }

    public String stringMinusInt(String A, int B){
        if(A.matches("^-?|[1-9][0-9]*")){
            int A_int = Integer.parseInt(A);
            return Integer.toString(A_int - B);
        }else{
            String B_string = Integer.toString(B);
            return subtractCommonLetters(A, B_string);
        }
    }

    public String stringMinusBoolean(String A, boolean B){
        String B_string = Boolean.toString(B);

        return subtractCommonLetters(A, B_string);
    }

    public String stringMinusFloat(String A, float B){
        if(A.matches("^0.0|-?[1-9][0-9]*.[0-9]+")){
            float A_float = Float.parseFloat(A);
            return Float.toString(A_float - B);
        }else{
            String B_String = Float.toString(B);

            return subtractCommonLetters(A, B_String);
        }
    }

    public int intMinusInt(int A, int B){
        return A - B;
    }

    public int intMinusString(int A, String B){
        if(B.matches("^-?|[1-9][0-9]*")){
            int B_int = Integer.parseInt(B);

            return A - B_int;
        }else{
            String A_String = Integer.toString(A);

            String output = subtractCommonLetters(A_String, B);

            return Integer.parseInt(output);
        }
    }

    public boolean intMinusBool(int a, boolean B){
        Boolean temp;

        if(a % 2 == 0){
            temp = true;
        }else{
            temp = false;
        }

        return !(temp || B);
    }

    public float intMinusFloat(int A, float B){
        float A_float = (float) A;
        return A - B;
    }

    public boolean boolMinusBool(boolean A, boolean B){
        return !(A || B);
    }

    public boolean boolMinusFloat(boolean A, float B){
        Boolean temp;

        if(B % 2.0 == 0.0){
            temp = true;
        }else{
            temp = false;
        }

        return !(temp || A);
    }

    public boolean boolMinusInt(boolean A, int B){
        Boolean temp;

        if(B % 2 == 0){
            temp = true;
        }else{
            temp = false;
        }

        return !(temp || A);
    }

    public String boolMinusString(boolean A, String B){
        String A_string = Boolean.toString(A);

        return subtractCommonLetters(A_string, B);
    }

    public float floatMinusFloat(float A, float B){
        return A-B;
    }

    public float floatMinusInt(float A, int B){
        float B_float = (float) B;

        return A - B_float;
    }

    public String floatMinusString(float A, String B){
        if(B.matches("^0.0|-?[1-9][0-9]*.[0-9]+")){
            float B_float = Float.parseFloat(B);
            return Float.toString(A - B_float);
        }else{
            String A_String = Float.toString(A);

            return subtractCommonLetters(A_String, B);
        }
    }

    public boolean floatMinusBool(float A, boolean B){
        Boolean temp;

        if(A % 2.0 == 0.0){
            temp = true;
        }else{
            temp = false;
        }

        return !(temp || B);
    }

    private String subtractCommonLetters(String primary_string, String subtraction_string){
        for(char select : subtraction_string.toCharArray()){
            primary_string = primary_string.replace(Character.toString(select), "");
        }

        return primary_string;
    }
}
