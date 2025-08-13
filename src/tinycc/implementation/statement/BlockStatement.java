package tinycc.implementation.statement;


import java.util.List;

import tinycc.diagnostic.Diagnostic;
import tinycc.diagnostic.Locatable;
import tinycc.implementation.semantic.Scope;

public class BlockStatement extends Statement {
    private Locatable loc;
    private List<Statement> statements;

    public BlockStatement(Locatable loc, List<Statement> statements){
        this.loc = loc;
        this.statements = statements;
    }

    public Locatable getLocation(){
        return this.loc;
    }

    public String getInputName() {
        return loc.getInputName();
    }

    public int getLine() {
        return loc.getLine();
    }

    public int getColumn() {
        return loc.getColumn();
    }


    public String printStatements(List<Statement> statements){
        String output = "";
        int count = 0;
        for (Statement statement : statements){
            if (count == statements.size() - 1){
                output += statement.toString();
                break;
            }
            output += statement.toString() + ", ";
            count++;
        }
        return output;
    }

    @Override
    public String toString(){
        if (this.statements.isEmpty()){
            return "Block[]";
        } else {
            return "Block[" + printStatements(statements) + "]";
        }
    }

    @Override
    public void checkType(Diagnostic d, Scope parent){
        Scope scope = parent.newNestedScope();
        for (Statement s : statements){
            s.checkType(d, scope);
        }
    }
}
