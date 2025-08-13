package tinycc.implementation.type;

import tinycc.parser.TokenKind;

public class SimpleType extends Type {
    private TokenKind kind;

    public SimpleType(TokenKind kind){
        System.err.println("DEBUG: Creating SimpleType with kind: " + kind);
        this.kind = kind;
    }

	public TokenKind getKind(){
		return this.kind;
	}

    @Override
    public String toString() {
		switch (this.kind) {
			case INT:
				return "Type_int";
			case CHAR:
				return "Type_char";
			case VOID:
				return "Type_void";
			default:
				throw new IllegalArgumentException("Invalid type.");
		}
    }

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SimpleType)) return false;
		SimpleType other = (SimpleType) o;
		return this.kind == other.kind;
	}

	@Override
	public int hashCode() {
		return kind.hashCode();
	}

	public boolean isInt(){
		return this.kind == TokenKind.INT;
	}

	public boolean isPointer(Type t){
		return t instanceof PointerType;
	}

	public static Type getInt(){
		return new SimpleType(TokenKind.INT);
	}

	public static Type getChar(){
		return new SimpleType(TokenKind.CHAR);
	}

    
}
