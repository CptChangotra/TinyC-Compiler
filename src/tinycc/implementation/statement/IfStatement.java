package tinycc.implementation.statement;

import tinycc.diagnostic.Diagnostic;
import tinycc.diagnostic.Locatable;
import tinycc.implementation.expression.Expression;
import tinycc.implementation.semantic.Scope;

public class IfStatement extends Statement {
    private Locatable loc;
    private Expression condition;
    private Statement consequence;
    private Statement alternative;

    public IfStatement(Locatable loc, Expression condition, Statement consequence, Statement alternative){
        this.loc = loc;
        this.condition = condition;
        this.consequence = consequence;
        this.alternative = alternative;
    }

    public Locatable getLocation(){
        return this.loc;
    }

    @Override
    public String toString(){
        if (this.alternative != null){
            return "If[" + this.condition.toString() + ", " + this.consequence.toString() + ", " + this.alternative.toString() + "]";
        } else {
            return "If[" + this.condition.toString() + ", " + this.consequence.toString() + "]";
        }
    }

    @Override
    public void checkType(Diagnostic d, Scope s) {
        // check the expression, cons, and alter, for errors.
        // IF and while only accept scalar types.
        this.condition.checkType(d, s);
        this.consequence.checkType(d, s);
        this.alternative.checkType(d, s);
    }
}
