package tinycc.implementation;

import tinycc.diagnostic.Diagnostic;
import tinycc.diagnostic.Location;
import tinycc.implementation.semantic.Scope;
import tinycc.implementation.statement.Declaration;
import tinycc.implementation.type.Type;
import tinycc.parser.Token;

public class ExternalDeclaration {
    private Type type;
    private Token name;

    public ExternalDeclaration(Type type, Token name) {
        this.type = type;
        this.name = name;
    }

    public Type getType() {
        return this.type;
    }

    public Token getName() {
        return this.name;
    }

    public Location getLocation() {
        return new Location(name);
    }

    	public void checkType(Diagnostic d, Scope s) {
		// Check for void variable declarations
		if (this.type instanceof tinycc.implementation.type.SimpleType) {
			tinycc.implementation.type.SimpleType st = (tinycc.implementation.type.SimpleType) this.type;
			if (st.getKind() == tinycc.parser.TokenKind.VOID) {
				d.printError(name, "Cannot declare variable '%s' of type void", name.getText());
				return;
			}
		}
		
		// Add external declaration to global scope
		try {
			s.add(name.getText(), new Declaration(type, name, null));
		} catch (Exception e) {
			d.printError(name, "Global variable '%s' already declared", name.getText());
		}
	}

    @Override
    public String toString() {
        return "ExternalDeclaration[" + name + ", " + type + "]";
    }
}
