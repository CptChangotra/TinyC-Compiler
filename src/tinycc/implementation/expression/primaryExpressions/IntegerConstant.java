package tinycc.implementation.expression.primaryExpressions;

import tinycc.diagnostic.Diagnostic;
import tinycc.implementation.expression.Expression;
import tinycc.implementation.semantic.Scope;
import tinycc.implementation.type.SimpleType;
import tinycc.implementation.type.Type;
import tinycc.parser.TokenKind;

public class IntegerConstant extends Expression {
    private int value;

    public IntegerConstant(int value){
        this.value = value;
    }

    public int getInt(){
        return this.value;
    }

    public int eval(){
        return this.value;
    }

    @Override
    public String toString(){
        return "Const_" + this.value;
    }

    @Override
    public Type checkType(Diagnostic d, Scope s) {
        return new SimpleType(TokenKind.INT);
    }

}
