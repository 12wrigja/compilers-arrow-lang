grammar arrowlang;

Start : Stmts
      ;

Stmts : TStmt Stmts
      | e
      ;


TStmt : Stmt
      | FuncDefStmt
      ;

Stmt : CallStmt
     | DeclStmt
     | AssignStmt
     | IfStmt
     | WhileStmt
     | ForStmt
     ;

Block : "{" BlockStmts ReturnStmt "}"
      | "{" BlockStmts "}"
      | "{" ReturnStmt "}"
      ;

BlockStmts : BlockStmt BlockStmts
           | e
           ;

BlockStmt : Stmt
          | LoopControlStmt
          ;

Expr : ArithExpr ;

FuncDefStmt : FUNC NAME "(" ParamDecls ")" TypeSpec Block
            | FUNC NAME "(" ParamDecls ")" Block
            | FUNC NAME "(" ")" TypeSpec Block
            | FUNC NAME "(" ")" Block
            ;

ParamDecls : NAME TypeSpec ParamDeclsRem
           ;

ParamDeclsRem : "," NAME TypeSpec ParamDeclsRem
            | e
            ;

BooleanExpr : AndExpr BooleanExprRem
            ;

BooleanExprRem : "|" "|" AndExpr BooleanExprRem
             | e
             ;

AndExpr : NotExpr AndExprRem
        ;

AndExprRem : "&" "&" NotExpr AndExprRem
         | e
         ;

NotExpr : "!" BooleanTerm
        | BooleanTerm
        ;

BooleanTerm : CmpExpr
            | BooleanConstant
            | "(" BooleanExpr ")"
            ;

CmpExpr : ArithExpr CmpOp ArithExpr ;

CmpOp : "<"
      | "<" "="
      | "=" "="
      | "!" "="
      | ">" "="
      | ">"
      ;

BooleanConstant : true
                | false
                ;

ArithExpr : MulDiv ArithExprRem
          ;

ArithExprRem : "+" MulDiv ArithExprRem
           | "-" MulDiv ArithExprRem
           | e
           ;

MulDiv : Negate MulDivRem
       ;

MulDivRem : "*" Negate MulDivRem
        | "/" Negate MulDivRem
        | "%" Negate MulDivRem
        | e
        ;

Negate : "-" Atomic
       | Atomic
       ;

Atomic : ValueExpr
       | "(" ArithExpr ")"
       ;

ValueExpr : Constant
          | SymbolValueExpr
          ;

Constant : INT_CONST
         | FLOAT_CONST
         | STRING_CONST
         ;

SymbolValueExpr : Call
                | NAME
                ;

CallStmt : Call ;

Call : NAME "(" CallParams ")"
     | NAME "(" ")"
     ;

CallParams : Expr CallParamsRem
           ;

CallParamsRem : "," Expr CallParamsRem
            | e
            ;

DeclStmt : VAR NAME TypeSpec "=" Expr
         | VAR NAME TypeSpec
         | VAR NAME "=" Expr
         ;

TypeName : NAME ;

TypeSpec : TypeName ;

AssignStmt : NAME "=" Expr ;

IfStmt : IF BooleanExpr Block ElseIfStmt
       | IF BooleanExpr Block
       ;

ElseIfStmt : else Block
           | else IfStmt
           ;

WhileStmt : while BooleanExpr Block
          | while Block
          ;

ForStmt : for DeclStmt ";" BooleanExpr ";" AssignStmt Block
        | for DeclStmt ";" BooleanExpr ";" Block
        | for DeclStmt ";" ";" AssignStmt Block
        | for DeclStmt ";" ";" Block
        | for ";" BooleanExpr ";" AssignStmt Block
        | for ";" BooleanExpr ";" Block
        | for ";" ";" AssignStmt Block
        ;

ReturnStmt : return Expr
           | return
           ;

LoopControlStmt : continue
                | break
                ;
                
//Lexer Tokens
VAR : 'var';
NAME : ([a-z]|[A-Z])([a-z]|[A-Z]|[0-9]|_)*;
FUNC : 'func';
IF : 'if';
ELSE : 'else';
FOR : 'for';
WHILE : 'while';
CONTINUE : 'continue'
BREAK : 'break';
TRUE : 'true';
FALSE : 'false';