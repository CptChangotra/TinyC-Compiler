package tinycc.implementation.expression;

import tinycc.diagnostic.Diagnostic;
import tinycc.implementation.semantic.Scope;
import tinycc.implementation.type.Type;
import tinycc.parser.TokenKind;

/**
 * The main expression class (see project description)
 *
 * You can change this class but the given name of the class must not be
 * modified.
 */
public abstract class Expression {

	/**
	 * Creates a string representation of this expression.
	 *
	 * @remarks See project documentation.
	 * @see StringBuilder
	 */
	@Override
	public abstract String toString();

	/*public void setType(TokenKind type){
		this.type = type;
	}

	public TokenKind getType(){
		return this.type;
	}*/

	public abstract Type checkType(Diagnostic d, Scope s);
	//public abstract Type getType();

	/*public abstract int eval();

	public boolean equals(Object object){
		if (!(object instanceof Expression)){
			return false;
		}
		Expression other = (Expression) object;
		return this.eval() == other.eval();
	}*/

    public boolean isLValue() {
        return false;
    }
}
