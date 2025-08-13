package tinycc.implementation;

import java.util.List;

import tinycc.diagnostic.Diagnostic;
import tinycc.diagnostic.Location;
import tinycc.implementation.semantic.Scope;
import tinycc.implementation.statement.Declaration;
import tinycc.implementation.statement.Statement;
import tinycc.implementation.type.FunctionType;
import tinycc.implementation.type.SimpleType;
import tinycc.implementation.type.Type;
import tinycc.parser.Token;
import tinycc.parser.TokenKind;

public class FunctionDefinition extends Statement {
    private Type type;
    private Token name;
    List<Token> parameterNames;
    Statement body;

    public FunctionDefinition(Type type, Token name, List<Token> parameterNames, Statement body){
        this.type = type;
        this.name = name;
        this.parameterNames = parameterNames;
        this.body = body;
    }

    public Type getFunctionType(){
        return this.type;
    }

    public Token getFunctionName(){
        return this.name;
    }

    public List<Token> getParamaterNames(){
        return this.parameterNames;
    }

    public Statement getFunctionBody(){
        return this.body;
    }

    public Location getFunctionLocation(){
        return new Location(name);
    }

    private String printArguments(List<Token> functionArguments){
        String arguments = "";
        int count = 0;
        for (Token argument : functionArguments){
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
        if (this.parameterNames.isEmpty()){
            return "Call" + "[" + getFunctionName() + "]";
        }
        return "Call" + "[" + getFunctionName() + ", " + printArguments(this.parameterNames) + "]";
    }

    	@Override
	public void checkType(Diagnostic d, Scope s) {
		// Function is already registered in global scope by Compiler.checkSemantics()
		// so we don't need to register it again here
		
		System.err.println("DEBUG: Processing function definition: " + name.getText());
		System.err.println("DEBUG: Function type: " + type);
		System.err.println("DEBUG: Parameter names count: " + parameterNames.size());
		
		// Create function scope with parameters
		Scope functionScope = s.newNestedScope();
		
		// Set the current function's return type in the scope
		if (type instanceof FunctionType) {
			FunctionType funcType = (FunctionType) type;
			Type returnType = funcType.getReturnType();
			functionScope.setCurrentFunctionReturnType(returnType);
			System.err.println("DEBUG: Function " + name.getText() + " return type: " + returnType);
		}
		
		// Get parameter types from function type
		if (type instanceof FunctionType) {
			FunctionType funcType = (FunctionType) type;
			List<Type> paramTypes = funcType.getFunctionArguments();
			
			System.err.println("DEBUG: Function " + name.getText() + " has " + paramTypes.size() + " parameter types");
			System.err.println("DEBUG: Function has " + parameterNames.size() + " parameter names");
			
			for (int i = 0; i < parameterNames.size(); i++) {
				Token param = parameterNames.get(i);
				Type paramType;
				
				if (i < paramTypes.size()) {
					paramType = paramTypes.get(i);
					System.err.println("DEBUG: Parameter " + (param != null ? param.getText() : "null") + " has type: " + paramType);
				} else {
					// Fallback to int if parameter type is missing
					paramType = new SimpleType(TokenKind.INT);
					System.err.println("DEBUG: Parameter " + (param != null ? param.getText() : "null") + " using fallback type: " + paramType);
				}
				
				if (param != null) {
					Declaration paramDecl = new Declaration(paramType, param, null);
					try {
						functionScope.add(param.getText(), paramDecl);
						System.err.println("DEBUG: Successfully added parameter " + param.getText() + " with type " + paramType + " to function scope");
					} catch (Exception e) {
						d.printError(param, "Parameter '%s' already declared", param.getText());
					}
				} else {
					System.err.println("DEBUG: Parameter at index " + i + " is null!");
				}
			}
		} else {
			System.err.println("DEBUG: Function type is not FunctionType: " + type.getClass().getSimpleName());
			// Fallback: assume all parameters are int
			for (Token param : parameterNames) {
				Declaration paramDecl = new Declaration(new SimpleType(TokenKind.INT), param, null);
				try {
					functionScope.add(param.getText(), paramDecl);
				} catch (Exception e) {
					d.printError(param, "Parameter '%s' already declared", param.getText());
				}
			}
		}
		
		// Check function body
		body.checkType(d, functionScope);
	}
    
}
