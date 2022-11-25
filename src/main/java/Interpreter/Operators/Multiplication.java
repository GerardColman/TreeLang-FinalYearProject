package Interpreter.Operators;

import java.util.ArrayList;

public class Multiplication {

    public String stringTimesString(String A, String B){
        return interweave(A,B);
    }

    public String stringTimesInt(String A, int B){
        return interweave(A, Integer.toString(B));
    }

    public String stringTimesBool(String A, boolean B){
        return interweave(A, Boolean.toString(B));
    }

    public String stringTimesFloat(String A, float B){
        return interweave(A, Float.toString(B));
    }

    public int intTimesInt(int A, int B){
        return A * B;
    }

    public boolean boolTimesBool(boolean A, boolean B){
        return A && B;
    }

    public boolean boolTimesFloat(boolean A, float B){
        Boolean temp;

        if(B % 2.0 == 0.0){
            temp = true;
        }else{
            temp = false;
        }

        return temp && A;
    }

    public boolean boolTimesInt(boolean A, int B){
        Boolean temp;

        if(B % 2 == 0){
            temp = true;
        }else{
            temp = false;
        }

        return temp && A;
    }

    public float floatTimesfloat(float A, float B){
        return A * B;
    }

    public float floatTimesInt(float A, int B){
        return A * (float) B;
    }

    /*
    Sourced from:
    https://www.geeksforgeeks.org/alternatively-merge-two-strings-in-java/
     */
    private String interweave(String s1, String s2){
        // To store the final string
        StringBuilder result = new StringBuilder();

        // For every index in the strings
        for (int i = 0; i < s1.length() || i < s2.length(); i++) {

            // First choose the ith character of the
            // first string if it exists
            if (i < s1.length())
                result.append(s1.charAt(i));

            // Then choose the ith character of the
            // second string if it exists
            if (i < s2.length())
                result.append(s2.charAt(i));
        }

        return result.toString();
    }
}
