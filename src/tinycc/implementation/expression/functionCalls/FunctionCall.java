package tinycc.implementation.expression.functionCalls;

import java.util.List;

import tinycc.diagnostic.Diagnostic;
import tinycc.implementation.expression.Expression;
import tinycc.implementation.semantic.Scope;
import tinycc.implementation.type.Type;
import tinycc.parser.Token;
import tinycc.implementation.statement.Declaration;
import tinycc.implementation.type.FunctionType;
import tinycc.implementation.type.SimpleType;
import tinycc.parser.TokenKind;

public class FunctionCall extends Expression {
    private Token token;
    private Expression functionName;
    private List<Expression> functionArguments;

    public FunctionCall(Token token, Expression functionName, List<Expression> functionArguments){
        this.token = token;
        this.functionName = functionName;
        this.functionArguments = functionArguments;
    }

    public Expression getFunctionName(){
        return this.functionName;
    }

    public List<Expression> getFunctionArguments(){
        return this.functionArguments;
    }

    public Token getLocation(){
        return this.token;
    }

    private String printArguments(List<Expression> functionArguments){
        String arguments = "";
        int count = 0;
        for (Expression argument : functionArguments){
            if (count == functionArguments.size() - 1){
                arguments += argument.toString();
                break;
            }
            arguments += argument.toString() + ", ";
            count++;
        }
        return arguments;
    }

    @Override
    public String toString(){
        if (this.functionArguments.isEmpty()){
            return "Call" + "[" + getFunctionName() + "]";
        }
        return "Call" + "[" + getFunctionName() + ", " + printArguments(this.functionArguments) + "]";
    }

    @Override
    public Type checkType(Diagnostic d, Scope s) {
        // Check that function name resolves to a function type
        Type functionType = functionName.checkType(d, s);
        if (functionType == null) {
            return null;
        }
        if (!(functionType instanceof FunctionType)) {
            d.printError(token, "Cannot call non-function type");
            return null;
        }
        
        FunctionType ft = (FunctionType) functionType;
        List<Type> expectedParams = ft.getFunctionArguments();
        
        // Check number of arguments
        if (functionArguments.size() != expectedParams.size()) {
            d.printError(token, "Function expects " + expectedParams.size() + " arguments, but " + functionArguments.size() + " were provided");
            return null;
        }
        
        // Check each argument type
        for (int i = 0; i < functionArguments.size(); i++) {
            Type argType = functionArguments.get(i).checkType(d, s);
            if (argType == null) {
                return null;
            }
            Type expectedType = expectedParams.get(i);
            
            if (!canImplicitlyConvert(argType, expectedType)) {
                d.printError(token, "Argument " + (i+1) + " has incompatible type");
                return null;
            }
        }
        
        return ft.getReturnType();
    }
    
    private boolean canImplicitlyConvert(Type from, Type to) {
        // Simple conversion rules for now
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
