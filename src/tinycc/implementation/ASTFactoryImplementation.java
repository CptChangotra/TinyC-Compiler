package tinycc.implementation;

import java.util.ArrayList;
import java.util.List;

import tinycc.diagnostic.Locatable;
import tinycc.implementation.expression.Expression;
import tinycc.implementation.expression.binaryExpressions.BinaryExpression;
import tinycc.implementation.expression.functionCalls.FunctionCall;
import tinycc.implementation.expression.primaryExpressions.CharacterConstant;
import tinycc.implementation.expression.primaryExpressions.Identifier;
import tinycc.implementation.expression.primaryExpressions.IntegerConstant;
import tinycc.implementation.expression.primaryExpressions.StringLiteral;
import tinycc.implementation.expression.unaryExpression.UnaryExpression;
import tinycc.implementation.statement.BlockStatement;
import tinycc.implementation.statement.Declaration;
import tinycc.implementation.statement.ExpressionStatement;
import tinycc.implementation.statement.IfStatement;
import tinycc.implementation.statement.Return;
import tinycc.implementation.statement.Statement;
import tinycc.implementation.statement.WhileStatement;
import tinycc.implementation.type.FunctionType;
import tinycc.implementation.type.PointerType;
import tinycc.implementation.type.Type;
import tinycc.implementation.type.SimpleType;
import tinycc.parser.ASTFactory;
import tinycc.parser.Token;
import tinycc.parser.TokenKind;
import tinycc.implementation.FunctionDefinition;

public class ASTFactoryImplementation implements ASTFactory {
    private List<Object> declarations; // Unified list for both prototypes and definitions

    public ASTFactoryImplementation(){
        this.declarations = new ArrayList<>();
    }

    public List<Object> getDeclarations() {
        return this.declarations;
    }

    // Helper methods to get specific types
    public List<ExternalDeclaration> getExternalDeclarations() {
        List<ExternalDeclaration> externals = new ArrayList<>();
        for (Object decl : declarations) {
            if (decl instanceof ExternalDeclaration) {
                externals.add((ExternalDeclaration) decl);
            }
        }
        return externals;
    }

    public List<FunctionDefinition> getFunctionDefinitions() {
        List<FunctionDefinition> functions = new ArrayList<>();
        for (Object decl : declarations) {
            if (decl instanceof FunctionDefinition) {
                functions.add((FunctionDefinition) decl);
            }
        }
        return functions;
    }


    @Override
    public Statement createBlockStatement(Locatable loc, List<Statement> statements) {
        return new BlockStatement(loc, statements);
    }

    @Override
    public Statement createDeclarationStatement(Type type, Token name, Expression init) {
        return new Declaration(type, name, init);
    }

    @Override
    public Statement createExpressionStatement(Locatable loc, Expression expression) {
        return new ExpressionStatement(loc, expression);
    }

    @Override
    public Statement createIfStatement(Locatable loc, Expression condition, Statement consequence,
            Statement alternative) {
        return new IfStatement(loc, condition, consequence, alternative);
    }

    @Override
    public Statement createReturnStatement(Locatable loc, Expression expression) {
        return new Return(loc, expression);
    }

    @Override
    public Statement createWhileStatement(Locatable loc, Expression condition, Statement body) {
        return new WhileStatement(loc, condition, body);
    }

    @Override
    public Type createFunctionType(Type returnType, List<Type> parameters) {
        return new FunctionType(returnType, parameters);
        }

    @Override
    public Type createPointerType(Type pointsTo) {
        return new PointerType(pointsTo);
    }

    @Override
    public Type createBaseType(TokenKind kind) {
        try {
            return new SimpleType(kind);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid kind.");
        }
    }

    @Override
    public Expression createBinaryExpression(Token operator, Expression left, Expression right) {

        return new BinaryExpression(operator, left, right);
    }

    @Override
    public Expression createCallExpression(Token token, Expression callee, List<Expression> arguments) {
        return new FunctionCall(token, callee, arguments);
    }

    	@Override
	public Expression createConditionalExpression(Token token, Expression condition, Expression consequence,
			Expression alternative) {
		// For now, return a simple binary expression as placeholder
		// In a full implementation, you would create a ConditionalExpression class
		return new BinaryExpression(token, condition, consequence);
	}

    @Override
    public Expression createUnaryExpression(Token operator, boolean postfix, Expression operand) {
        return new UnaryExpression(operator, postfix, operand);
    }

    	@Override
	public Expression createPrimaryExpression(Token token) {
		switch(token.getKind()){
			case NUMBER:
				return new IntegerConstant(Integer.parseInt(token.getText()));
			case IDENTIFIER:
				return new Identifier(token.getText(), token);
			case CHARACTER:
				return new CharacterConstant(token.getText());
			case STRING:
				return new StringLiteral(token.getText());
			default:
				throw new UnsupportedOperationException("Not a primary expression: " + token.getKind());
		}
	}

    @Override
    public void createExternalDeclaration(Type type, Token name) {
        // Global scopes.
        ExternalDeclaration declare = new ExternalDeclaration(type, name);
        declarations.add(declare);
        
    }

    @Override
    public void createFunctionDefinition(Type type, Token name, List<Token> parameterNames, Statement body) {
        // Local scopes.
        FunctionDefinition function = new FunctionDefinition(type, name, parameterNames, body);
        declarations.add(function);
    }
    
}
