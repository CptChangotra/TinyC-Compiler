package tinycc.implementation.expression.unaryExpression;


import tinycc.diagnostic.Diagnostic;
import tinycc.implementation.expression.Expression;
import tinycc.implementation.semantic.Scope;
import tinycc.implementation.type.FunctionType;
import tinycc.implementation.type.PointerType;
import tinycc.implementation.type.SimpleType;
import tinycc.implementation.type.Type;
import tinycc.parser.Token;
import tinycc.parser.TokenKind;

public class UnaryExpression extends Expression{
    private Token operator;
    private boolean postfix;
    private Expression operand;

    public UnaryExpression(Token operator, boolean postfix, Expression operand){
        this.operand = operand;
        this.postfix = postfix;
        this.operator = operator;
    }

    public Expression getOperand(){
        return this.operand;
    }

    public Token getOperator(){
        return this.operator;
    }

    public String toString(){
        return "Unary_" + this.operator.toString() + "[" + this.operand.toString() + "]";
    }

    @Override
    public boolean isLValue() {
        switch (operator.getKind()) {
            case ASTERISK: // dereference operator
                // Dereferenced expressions are L-values
                return true;
            case AND: // address-of operator
                // Address-of expressions are not L-values
                return false;
            case PLUS_PLUS:
            case MINUS_MINUS:
                // Increment/decrement expressions are not L-values
                return false;
            default:
                // Other unary expressions are not L-values
                return false;
        }
    }

    @Override
    public Type checkType(Diagnostic d, Scope s) {
        Type operandType = operand.checkType(d, s);
        
        // If operand type checking failed, return null
        if (operandType == null) {
            return null;
        }
        
        // Debug output for dereference operator
        if (operator.getKind() == TokenKind.ASTERISK) {
            System.err.println("DEBUG: Dereferencing operand type: " + operandType);
            System.err.println("DEBUG: Operand is pointer: " + (operandType instanceof PointerType));
        }
        
        switch (operator.getKind()) {
            case AND: // address-of operator
                if (!operand.isLValue()) {
                    d.printError(operator, "Address-of operator requires an l-value");
                    return null;
                }
                return new PointerType(operandType);
                
            case ASTERISK: // dereference operator
                if (!(operandType instanceof PointerType)) {
                    d.printError(operator, "Dereference operator requires a pointer type");
                    return null;
                }
                // Check if it's a void* pointer
                PointerType ptrType = (PointerType) operandType;
                if (ptrType.getPointsTo() instanceof SimpleType && 
                    ((SimpleType) ptrType.getPointsTo()).getKind() == TokenKind.VOID) {
                    d.printError(operator, "Cannot dereference void* pointer");
                    return null;
                }
                return ptrType.getPointsTo();
                
            case SIZEOF:
                // Reject sizeof on function types and void
                if (operandType instanceof FunctionType) {
                    d.printError(operator, "Cannot apply sizeof to function type");
                    return null;
                }
                if (operandType instanceof SimpleType && ((SimpleType) operandType).getKind() == TokenKind.VOID) {
                    d.printError(operator, "Cannot apply sizeof to void type");
                    return null;
                }
                // sizeof can be applied to other types
                return new SimpleType(TokenKind.INT);
                
            case PLUS_PLUS:
            case MINUS_MINUS:
                if (!operand.isLValue()) {
                    d.printError(operator, "Increment/decrement requires an l-value");
                    return null;
                }
                if (!isScalar(operandType)) {
                    d.printError(operator, "Increment/decrement requires a scalar type");
                    return null;
                }
                return operandType;
            
            default:
                // For other unary operators, just return the operand type
                return operandType;
        }
    }
    
    private boolean isScalar(Type t) {
        return (t instanceof SimpleType) || (t instanceof PointerType);
    }

}
