package tinycc.implementation.expression.primaryExpressions;

import tinycc.diagnostic.Diagnostic;
import tinycc.implementation.expression.Expression;
import tinycc.implementation.semantic.Scope;
import tinycc.implementation.type.PointerType;
import tinycc.implementation.type.SimpleType;
import tinycc.implementation.type.Type;
import tinycc.parser.TokenKind;

public class StringLiteral extends Expression{
    private String string;

    public StringLiteral(String string){
        this.string = string;
    }

    public String getString(){
        return this.string;
    }

    @Override
    public String toString() {
        return "Const_\"%s\"".formatted(this.string);
    }

    @Override
    public Type checkType(Diagnostic d, Scope s) {
        return new PointerType(new SimpleType(TokenKind.CHAR));
    }

    /*@Override
    public Type getType() {
        return new PointerType(new SimpleType(TokenKind.CHAR));
    }*/

}
