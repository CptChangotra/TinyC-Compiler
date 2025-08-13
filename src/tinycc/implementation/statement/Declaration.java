package tinycc.implementation.statement;

import tinycc.diagnostic.Diagnostic;
import tinycc.implementation.expression.Expression;
import tinycc.implementation.semantic.Scope;
import tinycc.implementation.type.SimpleType;
import tinycc.implementation.type.Type;
import tinycc.parser.Token;
import tinycc.parser.TokenKind;
import tinycc.implementation.type.PointerType;

public class Declaration extends Statement{
    private Type type;                      // Must be the final types.
    private Token name;                     // Could only be char or char* (string)
    private Expression init;                // Must always match type

    public Declaration(Type type, Token name, Expression init){
        this.type = type;
        this.name = name;
        this.init = init;
    }

    public Token getName(){
        return this.name;
    }

    public Type getType(){
        return this.type;
    }

    @Override
    public String toString(){
        if (this.init != null){
            return "Declaration_" + this.name.getText() + "[" + this.type.toString() + ", " + this.init.toString() + "]";
        } else {
            return "Declaration_" + this.name.getText() + "[" + this.type.toString() + "]";
        }
        
    }

    @Override
    public void checkType(Diagnostic d, Scope s) {
        // Check for void variable declarations
        if (this.type instanceof SimpleType) {
            SimpleType st = (SimpleType) this.type;
            if (st.getKind() == TokenKind.VOID) {
                d.printError(name, "Cannot declare variable '%s' of type void", name.getText());
                return;
            }
        }
        
        try {
            s.add(name.getText(), this);
        } catch (Exception e) {
            d.printError(name, "Double declaration of '%s'", name.getText());
        }
        if (init != null) {
            Type initType = init.checkType(d, s);
            if (initType != null && !canImplicitlyConvert(initType, this.type)) {
                d.printError(name, "Cannot initialize '%s' of type %s with expression of type %s", 
                           name.getText(), this.type, initType);
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
        
        // String literal (char*) to char* conversion
        if (from instanceof SimpleType && to instanceof PointerType) {
            SimpleType fromST = (SimpleType) from;
            PointerType toPT = (PointerType) to;
            if (fromST.getKind() == TokenKind.STRING && toPT.getPointsTo() instanceof SimpleType) {
                SimpleType pointsTo = (SimpleType) toPT.getPointsTo();
                return pointsTo.getKind() == TokenKind.CHAR;
            }
        }
        
        return false;
    }
}