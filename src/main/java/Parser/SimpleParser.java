package Parser;

import org.antlr.v4.runtime.ParserRuleContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.snt.inmemantlr.GenericParser;
import org.snt.inmemantlr.listener.DefaultTreeListener;
import org.snt.inmemantlr.tree.ParseTree;
import org.snt.inmemantlr.tree.ParseTreeNode;
import org.snt.inmemantlr.utils.FileUtils;

import javax.json.JsonObject;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.List;
import java.util.Objects;

public class SimpleParser {
    public void parse(String filename){
        try{
            File f = new File("treelang.g4");
            GenericParser gp = new GenericParser(f);
            String s = FileUtils.loadFileContent(filename);

            DefaultTreeListener defaultTreeListener = new DefaultTreeListener();

            gp.setListener(defaultTreeListener);
            gp.compile();

            ParserRuleContext ctx = gp.parse(s, GenericParser.CaseSensitiveType.NONE);

            ParseTree parseTree = defaultTreeListener.getParseTree();

            write(parseTree.getRoot());

//            JSONObject json = new JSONObject(parseTree.toJson());

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    JSON Layout {
        node rule = title
        node label = value | child node
    }
     */

    private void write(ParseTreeNode root){

        JSONArray parseTreeJson = preOrderTraversal(root);

        // Writing to file
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.json"));

            writer.write(parseTreeJson.toString(1));

            writer.close();
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    private JSONArray preOrderTraversal(ParseTreeNode root){
        JSONArray parseTreeJson = new JSONArray();

        // Goes to program node
        ParseTreeNode currentNode = root;
        if(!Objects.equals(currentNode.getRule(), "program")){
            currentNode = currentNode.getFirstChild();
        }

        for(ParseTreeNode node : currentNode.getChildren()){
            JSONObject object = visit(node);
            parseTreeJson.put(object);
        }
        //System.out.println(currentNode.getChildren());
        return parseTreeJson;
    }

    private JSONObject visit(ParseTreeNode node) {
        //System.out.println(node.getRule() + " : " + node.getLabel());


        JSONObject jsonObject = new JSONObject();

        // Variable Declarations
        if (Objects.equals(node.getRule(), "declaration")) {
            jsonObject.put("NODE_TYPE", "DECLARATION");
            String label = node.getLabel().substring(3, node.getLabel().length() - 1);

            if (label.contains("=")) {
                String name = label.substring(0, label.indexOf('='));
                String value = label.substring(label.indexOf('=') + 1);
                jsonObject.put("VAR_NAME", name);
                jsonObject.put("VAR_VALUE", value);
            } else {
                jsonObject.put("VAR_NAME", label);
            }

        }

        // Assignments
        if (Objects.equals(node.getRule(), "statement")) {
            node = node.getFirstChild();
        }

        if (Objects.equals(node.getRule(), "assignstmt")) {
            jsonObject.put("NODE_TYPE", "ASSIGNMENT");

            String label = node.getLabel().substring(0, node.getLabel().length() - 1); // Gets label without semicolon        }

            String varName = label.substring(0, label.indexOf('='));
            jsonObject.put("VAR_NAME", varName);

            String leftOperand;
            String rightOperand;
            String operation = null;
            if(label.contains("*")){
                operation = "*";
            }
            if(label.contains("+")){
                operation = "+";
            }
            if(label.contains("-")){
                operation = "-";
            }
            if(label.contains("/")){
                operation = "/";
            }


            if(operation != null){
                leftOperand = label.substring(label.indexOf('=')+1, label.indexOf(operation.charAt(0)));
                rightOperand = label.substring(label.indexOf((operation.charAt(0))));
                rightOperand = rightOperand.substring(1);

                jsonObject.put("LEFT_OPERAND", leftOperand);
                jsonObject.put("OPERATION", operation);
                jsonObject.put("RIGHT_OPERAND", rightOperand);
            }else{
                leftOperand = label.substring(label.indexOf('=')+1);
                jsonObject.put("LEFT_OPERAND", leftOperand);
            }
        }

        if(Objects.equals(node.getRule(), "ifstmt")){
            jsonObject.put("NODE_TYPE", "IFSTMT");
            JSONArray ifArr = new JSONArray();
            for(ParseTreeNode ifChildNode : node.getChildren()){
                if(Objects.equals(ifChildNode.getRule(), "lterm")){
                    jsonObject.put("LEFT_TERM", ifChildNode.getLabel());
                }else if(Objects.equals(ifChildNode.getRule(), "rterm")){
                    jsonObject.put("RIGHT_TERM", ifChildNode.getLabel());
                }else if(Objects.equals(ifChildNode.getRule(), "equality")){
                    jsonObject.put("EQUALITY", ifChildNode.getLabel());
                }else{
                    ifArr.put(visit(ifChildNode));
                }

                jsonObject.put("INTERNAL_STATEMENTS", ifArr);
            }
        }

        if(Objects.equals(node.getRule(), "printstmt")){
            String label = node.getLabel().substring(5, node.getLabel().length()-1);
            jsonObject.put("NODE_TYPE", "PRINTSTMT");
            jsonObject.put("NODE_VALUE", label);
        }

        if(Objects.equals(node.getRule(), "whileloop")){
            jsonObject.put("NODE_TYPE", "WHILELOOP");
            JSONArray whileArr = new JSONArray();

            for(ParseTreeNode whileChildNode : node.getChildren()){
                if(Objects.equals(whileChildNode.getRule(), "lterm")){
                    jsonObject.put("LEFT_TERM", whileChildNode.getLabel());
                }else if(Objects.equals(whileChildNode.getRule(), "rterm")){
                    jsonObject.put("RIGHT_TERM", whileChildNode.getLabel());
                }else if(Objects.equals(whileChildNode.getRule(), "equality")){
                    jsonObject.put("EQUALITY", whileChildNode.getLabel());
                }else{
                    whileArr.put(visit(whileChildNode));
                }

                jsonObject.put("INTERNAL_STATEMENTS", whileArr);
            }
        }

        return jsonObject;
    }
}
