package tinycc.implementation.expression.primaryExpressions;

import tinycc.diagnostic.Diagnostic;
import tinycc.implementation.expression.Expression;
import tinycc.implementation.semantic.Scope;
import tinycc.implementation.statement.Declaration;
import tinycc.implementation.type.Type;
import tinycc.parser.Token;

public class Identifier extends Expression {
    private String identifier;
    private Token token;
    private Declaration declaration = null;

    public Identifier(String identifier, Token token){
        this.identifier = identifier;
        this.token = token;
    }

    public Identifier(String identifier){
        this(identifier, null);
    }

    public String getIdentifier(){
        return identifier;
    }

    @Override
    public boolean isLValue() {
        return true;
    }

    @Override
    public String toString(){
        return "Var_" + identifier;
    }

    	@Override
	public Type checkType(Diagnostic d, Scope s) {
		try {
			this.declaration = s.lookup(identifier);
			Type resultType = this.declaration.getType();
			System.err.println("DEBUG: Identifier '" + identifier + "' resolves to type: " + resultType);
			return resultType;
		} catch (Exception e) {
			d.printError(token, "Variable '" + identifier + "' not declared.");
			return null;
		}
	}

}
