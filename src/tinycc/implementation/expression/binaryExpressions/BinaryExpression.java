package tinycc.implementation.expression.binaryExpressions;

import tinycc.diagnostic.Diagnostic;
import tinycc.implementation.semantic.Scope;
import tinycc.implementation.type.FunctionType;
import tinycc.implementation.type.PointerType;
import tinycc.implementation.type.SimpleType;
import tinycc.implementation.type.Type;
import tinycc.parser.Token;
import tinycc.parser.TokenKind;
import tinycc.implementation.expression.Expression;

public class BinaryExpression extends Expression {
    protected Type type;
    protected Expression lhs, rhs;
    protected Token operator;

    public BinaryExpression(Token operator, Expression lhs, Expression rhs){
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }


    public Expression getLhs(){
        return this.lhs;
    }

    public Expression getRhs(){
        return this.rhs;
    }

    public Token getBinaryOperator(){
        return this.operator;
    }

    public SimpleType getInt(){
        return new SimpleType(TokenKind.INT);
    }

    public PointerType getPointer(){
        return new PointerType(getInt());
    }

    // Checks if a type is an integer type
    private boolean isInt(Type t) {
        return (t instanceof SimpleType) &&
               (((SimpleType) t).getKind() == TokenKind.INT || ((SimpleType) t).getKind() == TokenKind.CHAR);
    }

    // Checks if a type is a pointer type
    private boolean isPointer(Type t) {
        return t instanceof PointerType;
    }

    // Checks if a type is a scalar type (integer or pointer, but not function)
    private boolean isScalar(Type t) {
        return isInt(t) || isPointer(t);
    }

    // Checks if a type is void*
    private boolean isVoidPointer(Type t) {
        return (t instanceof PointerType) &&
               (((PointerType) t).getPointsTo() instanceof SimpleType) &&
               ((SimpleType) ((PointerType) t).getPointsTo()).getKind() == TokenKind.VOID;
    }

    private boolean canImplicitlyConvert(Type from, Type to) {
        // int <-> int, char <-> int, etc.
        if (isInt(from) && isInt(to)) return true;

        // t* <-> t*
        if (isPointer(from) && isPointer(to)) {
            PointerType pf = (PointerType) from;
            PointerType pt = (PointerType) to;
            // Same pointer type
            if (pf.getPointsTo().equals(pt.getPointsTo())) return true;
            // Only allow T* -> void*, but not void* -> T*
            if (!isVoidPointer(from) && isVoidPointer(to)) return true;
            // Reject all other pointer conversions
            return false;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Binary_" + this.operator.toString() + "[" + this.lhs.toString() + ", " + this.rhs.toString() + "]";
    }

    @Override
    public Type checkType(Diagnostic d, Scope s) {
        Type leftType = lhs.checkType(d, s);
        Type rightType = rhs.checkType(d, s);
        
        // Debug: print types for assignment
        if (operator.getKind() == TokenKind.EQUAL) {
            System.err.println("DEBUG: Left type: " + leftType + ", Right type: " + rightType);
            System.err.println("DEBUG: isScalar(leftType): " + isScalar(leftType) + ", isScalar(rightType): " + isScalar(rightType));
        }

        switch (operator.getKind()) {
            case PLUS:
                if (isInt(leftType) && isInt(rightType)){
                    return SimpleType.getInt();
                }
                else if (isInt(leftType) && isPointer(rightType)){
                    // int + pointer -> pointer
                    System.err.println("DEBUG: int + pointer -> " + rightType);
                    return rightType;
                } else if (isPointer(leftType) && isInt(rightType)){
                    // pointer + int -> pointer
                    System.err.println("DEBUG: pointer + int -> " + leftType);
                    return leftType;
                } else {
                    d.printError(operator, "Invalid Addition Operation", lhs, rhs);
                    return null;
                }
            case MINUS:
                if (isInt(leftType) && isInt(rightType) || isPointer(leftType) && isPointer(rightType)){
                    return getInt();
                } else if (isPointer(leftType) && isInt(rightType)){
                    return getPointer();
                } else {
                    d.printError(operator, "Invalid Addition Operation", lhs, rhs);
                    return null;
                }
            case ASTERISK:
            case SLASH:
                if (isInt(leftType) && isInt(rightType)){
                    return getInt();
                } else {
                    d.printError(operator, "Multiplication and Division require both operands to be type INT", lhs, rhs);
                    return null;
                }
            case EQUAL_EQUAL:
            case BANG_EQUAL:
                if (isInt(leftType) && isInt(rightType)){
                    return getInt();
                } else if (isPointer(leftType) && isPointer(rightType)){
                    return getInt();
                } else {
                    d.printError(operator, "== and != require both operands to be either type INT or type Pointer", lhs, rhs);
                    return null;
                }
            case LESS:
            case GREATER:
            case LESS_EQUAL:
            case GREATER_EQUAL:
                if (isInt(leftType) && isInt(rightType)){
                    return getInt();
                } else if (isPointer(leftType) && isPointer(rightType)){
                    return getInt();
                } else {
                    d.printError(operator, "Comparisons require both operands to be either type INT or type Pointer", lhs, rhs);
                    return null;
                }
            case EQUAL:
                // 1. Check L-value
                if (!lhs.isLValue()) {
                    d.printError(operator, "Left side of assignment must be an assignable L-value");
                    return null;
                }
                // 2. Check that right side is not a function type
                if (rightType instanceof FunctionType) {
                    d.printError(operator, "Cannot assign function to variable");
                    return null;
                }
                // 3. Check scalar types
                if (!isScalar(leftType)) {
                    d.printError(operator, "Left side of assignment must be a scalar type");
                    return null;
                }
                if (!isScalar(rightType)) {
                    d.printError(operator, "Right side of assignment must be a scalar type");
                    return null;
                }
                // 4. Check implicit conversion
                if (!canImplicitlyConvert(rightType, leftType)) {
                    d.printError(operator, "Cannot assign %s to %s", rightType, leftType);
                    return null;
                }
                // 5. Return the type of the left operand
                return leftType;
            default:
                d.printError(operator, "Invalid Operator", operator);
                return null;
        }
    }

}
