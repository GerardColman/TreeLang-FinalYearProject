package Parser;

import java.util.*;

public class Tokenizer {
    /*
    PLAN

    USE STRING TOKENIZER

    HAVE ARRAY OF KEY VALUES THAT THE STRING WILL BE SPLIT ON DURING A 2ND Pass.  These should be key symbols

     */

    public ArrayList<String> Tokenize(String line){
        ArrayList<String> output_tokens = new ArrayList<>();
        StringTokenizer stringTokenizer = new StringTokenizer(line, Arrays.toString(key_symbols));

        char[] line_char_reference = line.toCharArray();
        int head = 0;
        int lead = 0;
        for(lead=0 ; lead < line_char_reference.length ; lead++){

            if(line_char_reference[lead] == ' '){ //Handles spaces
                String token = line.substring(head, lead);
                token = token.trim();
                output_tokens.add(token);
                head = lead;
            }else if(line_char_reference[lead] == ';'){ // Handles end line symbol
                String token = line.substring(head, lead);
                token = token.trim();
                output_tokens.add(token);
                output_tokens.add(";");
                head = lead;
            }else if(containsKeySymbols(Character.toString(line_char_reference[lead]))){ //Handles Brackets
                String token = line.substring(head, lead);
                token = token.trim();
                output_tokens.add(token);
                head = lead;
                lead++;
                token = line.substring(head, lead);
                token = token.trim();
                output_tokens.add(token);
                head = lead;
            }else if(line_char_reference[lead] == '#'){ //Handles comments
                break;
            }

        }

        output_tokens.removeAll(Collections.singleton(" "));
        return output_tokens;
    }

    private boolean containsKeySymbols(String token){
        boolean contains = false;

        for(String symbol : key_symbols){
            if(token.contains(symbol)){
                contains = true;
                break;
            }
        }

        return contains;
    }

    private final String[] key_symbols = {
            "(",
            ")",
            "{",
            "}",
            "[",
            "]"
    };
}
