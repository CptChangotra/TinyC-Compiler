package tinycc.implementation.statement;

import tinycc.diagnostic.Diagnostic;
import tinycc.diagnostic.Locatable;
import tinycc.implementation.expression.Expression;
import tinycc.implementation.semantic.Scope;

public class WhileStatement extends Statement {
    private Locatable loc;
    private Expression condition;
    private Statement body;

    public WhileStatement(Locatable loc, Expression condition, Statement body){
        this.loc = loc;
        this.condition = condition;
        this.body = body;
    }

    public Locatable getLocation(){
        return this.loc;
    }

    public String toString(){
        return "While[" + this.condition.toString() + ", " + this.body.toString() + "]";
    }

    @Override
    public void checkType(Diagnostic d, Scope s) {
        condition.checkType(d, s);
        body.checkType(d, s);
    }
    
}
