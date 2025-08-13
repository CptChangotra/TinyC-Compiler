package tinycc.implementation.expression.primaryExpressions;

import tinycc.diagnostic.Diagnostic;
import tinycc.implementation.expression.Expression;
import tinycc.implementation.semantic.Scope;
import tinycc.implementation.type.SimpleType;
import tinycc.implementation.type.Type;
import tinycc.parser.TokenKind;

public class CharacterConstant extends Expression {
    private String c;

    public CharacterConstant(String c){
        if (c == null) throw new IllegalArgumentException("Character cannot be null!");
        this.c = c;
    }


    public String getChar(){
        return this.c;
    }

    @Override
    public String toString(){
        return "Const_'" + this.c + "'";
    }


    @Override
    public Type checkType(Diagnostic d, Scope s) {
        return new SimpleType(TokenKind.CHAR);
    }
}
