package Interpreter.Operators;

public class Division {
    public String stringDivideString(String A, String B){
        return returnCommonLetters(A,B);
    }

    public String stringDivideInt(String A, int B){
        if(A.matches("^-?|[1-9][0-9]*")){
            int A_int = Integer.parseInt(A);
            return Integer.toString(A_int / B);
        }else{
            String B_string = Integer.toString(B);
            return returnCommonLetters(A, B_string);
        }
    }

    public String stringDivideBool(String A, boolean B){
        return returnCommonLetters(A, Boolean.toString(B));
    }

    public String stringDivideFloat(String A, Float B){
        if(A.matches("^0.0|-?[1-9][0-9]*.[0-9]+")){
            float A_float = Float.parseFloat(A);
            return Float.toString(A_float / B);
        }else{
            String B_String = Float.toString(B);

            return returnCommonLetters(A, B_String);
        }
    }

    public int intDivideInt(int A, int B){
         return A / B;
    }

    public boolean booleanDivideBoolean(boolean A, boolean B){
        return !(A && B);
    }

    public boolean boolDivideFloat(boolean A, float B){
        Boolean temp;

        if(B % 2.0 == 0.0){
            temp = true;
        }else{
            temp = false;
        }

        return !(temp && A);
    }

    public boolean boolDivideInt(boolean A, int B){
        Boolean temp;

        if(B % 2 == 0){
            temp = true;
        }else{
            temp = false;
        }

        return !(temp && A);
    }

    public float floatDividesFloat(float A, float B){
        return A / B;
    }

    public float floatDividesInt(float A, int B){
        return A / (float) B;
    }

    private String returnCommonLetters(String A, String B){
        StringBuilder output = new StringBuilder();

        for(char A_char : A.toCharArray()){
            for(char B_char : B.toCharArray()){
                if(A_char == B_char){
                    output.append(A_char);
                }
            }
        }

        return output.toString();
    }
}
