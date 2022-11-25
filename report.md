# Tree Lang Report
## Gerard Colman
In this report Ill just go over the state of the project so far

### Initial
The project is split up into 2 main parts
- The Parser
- The Interpreter/Runner

The Parser takes in a .tree file outputs a ParseTree in json format.<br>
The Interpreter takes in the ParseTree in json format and runs it line by line executing the code.

### Parser
A parser is made up of 2 parts
- Lexer
- Generator

The lexer takes in the file line by line and splits it up into tokens, a token is simply a chunk of text. <br>
The Generator then takes these tokens and produces a Parse tree <br><br>

Initially I tried to write the parser from scratch.  I wrote a lexer and started writing the Generator when I realized how much time <br>
I was sinking a lot of time into something which there are a lot of libraries for!<br><br>
So I found a library which is a simplified version of ANTLR. (https://github.com/julianthome/inmemantlr) <br>
This library generates a parseTree for me and I only have to write an algorithm to write it to a json file and a grammar file. <br>
<br>

### Interpreter/Runner
This part of the project takes in the parse tree outputted from the Parser and actually runs the program line by line<br>
A key aspect of my languages is the ability to implicity combine variables and classes and always getting a result.  Never an error.<br>
To accomplish this I will make use of javas overloading of functions ability.  So for example, for the plus operation<br>
I will have functions for combining a variable type (int, string, float) with every other variable type.<br>
I will repeat this for each other operation.
<br>
<br>
The Stages of the interpreter will be as follows: PUT THIS IN PROCESS SECTION
1. Read in from parse tree
2. Check for errors (non syntax error, like printing an undeclared variable)
3. Interpreting and running the line

#### Structure of the interpreter
As said above the interpreter is going to run through the parse tree line by line and run each expression.<br>
but along the way I will need to handle stuff at run time such as variables.  To do this I will use a hashmap<br>
where the key is the variable name and the value is what's stored in the variable.  If I get around to implementing functions and classes<br>
my plan is to use something similar to handle class references and function calls

## Variable Combinations
- Addition = appending
- Multiplication = interweaving
- Subtract = Removing
- Division = returns common characters