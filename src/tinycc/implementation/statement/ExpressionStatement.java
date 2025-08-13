package tinycc.implementation.statement;

import tinycc.diagnostic.Diagnostic;
import tinycc.diagnostic.Locatable;
import tinycc.implementation.expression.Expression;
import tinycc.implementation.semantic.Scope;

public class ExpressionStatement extends Statement {
    private Locatable loc;
    private Expression expression;

    public ExpressionStatement(Locatable loc, Expression expression){
        this.loc = loc;
        this.expression = expression;
    }

    public Locatable getLocation(){
        return this.loc;
    }

    public void checkType(Diagnostic d, Scope s) {
        this.expression.checkType(d, s); // type-check the expression
    }

    @Override
    public String toString(){
        return this.expression.toString();
    }
    
}
