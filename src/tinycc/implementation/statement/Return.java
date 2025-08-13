package tinycc.implementation.statement;

import tinycc.diagnostic.Diagnostic;
import tinycc.diagnostic.Locatable;
import tinycc.implementation.expression.Expression;
import tinycc.implementation.semantic.Scope;
import tinycc.implementation.type.SimpleType;
import tinycc.implementation.type.Type;
import tinycc.parser.TokenKind;

public class Return extends Statement {
    private Locatable loc;
    private Expression expression;

    public Return(Locatable loc, Expression expression){
        this.loc = loc;
        this.expression = expression;
    }

    public Locatable getLocation(){
        return this.loc;
    }

    @Override
    public String toString(){
        if (this.expression != null){
            return "Return[" + this.expression.toString() + "]";
        } else {
            return "Return[]";
        }
    }

    @Override
    public void checkType(Diagnostic d, Scope s) {
        Type expectedReturnType = s.getCurrentFunctionReturnType();
        
        System.err.println("DEBUG: Return statement check - expected return type: " + expectedReturnType);
        
        if (this.expression != null) {
            Type returnType = this.expression.checkType(d, s);
            System.err.println("DEBUG: Return expression type: " + returnType);
            
            if (returnType == null) {
                return; // Error already reported by expression
            }
            
            if (expectedReturnType == null) {
                // No function context, can't validate
                System.err.println("DEBUG: No function context for return validation");
                return;
            }
            
            if (!canImplicitlyConvert(returnType, expectedReturnType)) {
                System.err.println("DEBUG: Return type mismatch - got " + returnType + ", expected " + expectedReturnType);
                d.printError(loc, "Cannot return expression of type %s from function returning %s", 
                           returnType, expectedReturnType);
            } else {
                System.err.println("DEBUG: Return type check passed");
            }
        } else {
            // Return without expression - check if function returns void
            if (expectedReturnType != null && !(expectedReturnType instanceof SimpleType) || 
                (expectedReturnType instanceof SimpleType && ((SimpleType) expectedReturnType).getKind() != TokenKind.VOID)) {
                d.printError(loc, "Cannot return void from non-void function");
            }
        }
    }
    
    private boolean canImplicitlyConvert(Type from, Type to) {
        // Same type
        if (from.equals(to)) return true;
        
        // int <-> char conversion
        if (from instanceof SimpleType && to instanceof SimpleType) {
            SimpleType fromST = (SimpleType) from;
            SimpleType toST = (SimpleType) to;
            return (fromST.getKind() == TokenKind.INT && toST.getKind() == TokenKind.CHAR) ||
                   (fromST.getKind() == TokenKind.CHAR && toST.getKind() == TokenKind.INT);
        }
        
        return false;
    }
}
