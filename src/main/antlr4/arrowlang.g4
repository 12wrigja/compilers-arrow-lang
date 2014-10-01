grammar arrowlang;

start : stmts
      ;

stmts : tStmt stmts
      | e
      ;


tStmt : stmt
      | funcDefStmt
      ;

stmt : callStmt
     | declStmt
     | assignStmt
     | ifStmt
     | whileStmt
     | forStmt
     ;

block : LBRACKET blockStmts returnStmt RBRACKET
      | LBRACKET blockStmts RBRACKET
      | LBRACKET returnStmt RBRACKET
      ;

blockStmts : blockStmt blockStmts
           | e
           ;

blockStmt : stmt
          | loopControlStmt
          ;

expr : arithExpr ;

funcDefStmt : FUNC NAME LPAREN paramDecls RPAREN typeSpec block
            | FUNC NAME LPAREN paramDecls RPAREN block
            | FUNC NAME LPAREN RPAREN typeSpec block
            | FUNC NAME LPAREN RPAREN block
            ;

paramDecls : NAME typeSpec paramDeclsRem
           ;

paramDeclsRem : COMMA NAME typeSpec paramDeclsRem
            | e
            ;

booleanExpr : andExpr booleanExprRem
            ;

booleanExprRem : OR OR andExpr booleanExprRem
             | e
             ;

andExpr : notExpr andExprRem
        ;

andExprRem : AND AND notExpr andExprRem
         | e
         ;

notExpr : EXCL booleanTerm
        | booleanTerm
        ;

booleanTerm : cmpExpr
            | booleanConstant
            | LPAREN booleanExpr RPAREN
            ;

cmpExpr : arithExpr cmpOp arithExpr ;

cmpOp : LT
      | LT EQ
      | EQ EQ
      | EXCL EQ
      | GT EQ
      | GT
      ;

booleanConstant : TRUE
                | FALSE
                ;

arithExpr : mulDiv arithExprRem
          ;

arithExprRem : PLUS mulDiv arithExprRem
           | DASH mulDiv arithExprRem
           | e
           ;

mulDiv : negate mulDivRem
       ;

mulDivRem : MULT negate mulDivRem
        | DIV negate mulDivRem
        | MOD negate mulDivRem
        | e
        ;

negate : DASH atomic
       | atomic
       ;

atomic : valueExpr
       | LPAREN arithExpr RPAREN
       ;

valueExpr : constant
          | symbolValueExpr
          ;

constant : INT_CONST
         | FLOAT_CONST
         | STRING_CONST
         ;

symbolValueExpr : call
                | NAME
                ;

callStmt : call ;

call : NAME LPAREN callParams RPAREN
     | NAME LPAREN RPAREN
     ;

callParams : expr callParamsRem
           ;

callParamsRem : COMMA expr callParamsRem
            | e
            ;

declStmt : VAR NAME typeSpec EQ expr
         | VAR NAME typeSpec
         | VAR NAME EQ expr
         ;

typeName : NAME ;

typeSpec : typeName ;

assignStmt : NAME EQ expr ;

ifStmt : IF booleanExpr block elseIfStmt
       | IF booleanExpr block
       ;

elseIfStmt : ELSE block
           | ELSE ifStmt
           ;

whileStmt : WHILE booleanExpr block
          | WHILE block
          ;

forStmt : FOR declStmt SMC booleanExpr SMC assignStmt block
        | FOR declStmt SMC booleanExpr SMC block
        | FOR declStmt SMC SMC assignStmt block
        | FOR declStmt SMC SMC block
        | FOR SMC booleanExpr SMC assignStmt block
        | FOR SMC booleanExpr SMC block
        | FOR SMC SMC assignStmt block
        ;

returnStmt : RETURN expr
           | RETURN
           ;

loopControlStmt : CONTINUE
                | BREAK
                ;
          
          
e : '';
//Lexer Tokens
VAR : 'var';
NAME : '((a..z)|(A..Z))((a..z)|(A..Z)|(0..9)|(_))*'; //'([a-z]|[A-Z])([a-z]|[A-Z]|[0-9]|_)*'; 
FUNC : 'func';
IF : 'if';
ELSE : 'else';
FOR : 'for';
WHILE : 'while';
CONTINUE : 'continue';
BREAK : 'break';
RETURN : 'return';
TRUE : 'true';
FALSE : 'false';
LBRACKET : '{';
RBRACKET : '}';
LPAREN : '(';
RPAREN : ')';
EQ : '=';
COMMA : ',';
OR : '|';
AND : '&';
EXCL : '!';
LT : '<';
GT : '>';
PLUS : '+';
DASH : '-';
MULT : '*';
DIV  : '/';
MOD : '%';
SMC : ';';
INT_CONST : '[0-9]+';
FLOAT_CONST : '[0-9]*\\.?[0-9]+((E|e)(\\+|-)?[0-9]+)?';