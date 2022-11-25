grammar treelang;

program       :
              (declaration | statement)* EOF
              ;

// Parse rule for variable declarations

declaration   :
              VAR NAME SEMICOLON
              | VAR NAME '=' term SEMICOLON
              ;

// Parse rule for statements

statement      :
               ifstmt
             | printstmt
             | assignstmt
             | whileloop
               ;

// Parse rule for while loops
whileloop   :
            WHILE LPAREN lterm equality rterm RPAREN LCURLY
            (declaration | statement)*
            RCURLY
            ;

// Parse rule for if statements
ifstmt      :
            IF LPAREN lterm equality rterm RPAREN LCURLY
            (declaration | statement)*
            RCURLY
            ;


// Parse rule for print statements

printstmt      :
               PRINT term SEMICOLON
               ;

// Parse rule for assignment statements

assignstmt      :
                NAME ASSIGN expression SEMICOLON
                ;

// Parse rule for expressions

expression      :
                term
                | term OPERATION term
                ;

// Parse rule for terms

term          :
              identifier
              | floating
              | integer
              ;

lterm          :
              identifier
              | integer
              ;

rterm          :
              identifier
              | integer
              ;
equality: EQUALITY ;

// Parse rule for identifiers

identifier   : NAME  ;

// Parse rule for numbers

integer      : INTEGER  ;

floating         : FLOATING ;

// Reserved Keywords
////////////////////////////////

WHILE: 'while';
IF: 'if';
ENDIF: 'endif';
PRINT: 'print';
VAR: 'var';

// Operators
OPERATION: '+' | '-' | '*' | '/';
EQUALITY: '==' | '<' | '>' | '<=' | '>=' | '!=' ;
ASSIGN: '=';

// Semicolon and parentheses
SEMICOLON: ';';
LPAREN: '(';
RPAREN: ')';
LCURLY: '{';
RCURLY: '}';

// Integers
INTEGER: '0' | '-'?[1-9][0-9]*;

// FLOATS
FLOATING: '0.0' | '-'?[1-9][0-9]*.[0-9]+ ;

// Variable names
NAME: [a-zA-Z]+;

// Ignore all white spaces
WS: [ \t\r\n]+ -> skip ;

// Comments
COMMENT : '//' ~[\t\r\n]* -> skip;