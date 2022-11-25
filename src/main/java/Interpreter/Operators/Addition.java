package Interpreter.Operators;

public class Addition {

    public String stringPlusString(String A, String B){
     return A+B;
    }

    public String stringPlusInt(String A, int B){
        if(A.matches("^-?|[1-9][0-9]*")){
            int temp = Integer.parseInt(A);
            return Integer.toString(temp + B);
        }else{
            return A + Integer.toString(B);
        }

    }

    public String intPlusString(int A, String B){
        if(B.matches("^-?|[1-9][0-9]*")){
            int temp = Integer.parseInt(B);
            return Integer.toString(temp + A);
        }else{
            return  Integer.toString(A) + B;
        }
    }

    public int intPlusInt(int A, int B){
        return A+B;
    }

    public String stringPlusBool(String A, Boolean B){
        return A + Boolean.toString(B);
    }

    public String stringPlusFloat(String A, float B){
        return A + Float.toString(B);
    }

    public boolean intPlusBool(int a, boolean B){
        Boolean temp;

        if(a % 2 == 0){
            temp = true;
        }else{
            temp = false;
        }

        return temp || B;
    }

    public float intPlusFloat(int A, float B){
        float temp = (float) A;
        return temp + B;
    }

    public boolean boolPlusBool(boolean A, boolean B){
        return A || B;
    }

    public boolean boolPlusFloat(boolean A, float B){
        Boolean temp;

        if(B % 2.0 == 0.0){
            temp = true;
        }else{
            temp = false;
        }

        return temp || A;
    }
}
