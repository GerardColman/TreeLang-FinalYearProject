import Interpreter.Interpreter;
import Parser.SimpleParser;



public class TreeLang {

    public static void main(String[] args) {
        SimpleParser Parser = new SimpleParser();
        Interpreter interpreter = new Interpreter();

        Parser.parse("test_files/printtest.tree");
        interpreter.interpret("output.json");
    }
}
