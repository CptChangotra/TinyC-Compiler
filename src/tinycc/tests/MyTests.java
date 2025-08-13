package tinycc.tests;

import org.junit.Test;
import prog2.tests.CompilerTests;
import tinycc.implementation.expression.Expression;
import tinycc.implementation.type.Type;
import tinycc.implementation.semantic.Scope;
import tinycc.parser.TokenKind;
import static org.junit.Assert.assertEquals;

/**
 * Within this package you can implement your own tests that will
 * be run with the reference implementation.
 *
 * Note that no classes or interfaces will be available, except those initially
 * provided.
 * 
 * Do not write your own tests in this class. Use another class in this package.
 */
public class MyTests extends CompilerTests {
	@Test
	public void testCharSimple() {
		ASTMaker m = new ASTMaker("a");
		assertEqualsNormalized("Const_'a'", (m.createCharacter("a")).toString());
	}

	@Test
	public void testAddition() {
		ASTMaker m = new ASTMaker("1 + 2");
		// Create AST for "1 + 2"
		Expression left = m.createNumber("1");
		Expression right = m.createNumber("2");
		Expression addition = m.createBinary(TokenKind.PLUS, left, right);
		
		// Test the structure
		assertEqualsNormalized("Binary_+[Const_1, Const_2]", addition.toString());
	}

	@Test
	public void testMultiplicationType() {
		ASTMaker m = new ASTMaker("3 * 4");
		// Create AST for "3 * 4"
		Expression left = m.createNumber("3");
		Expression right = m.createNumber("4");
		Expression multiplication = m.createBinary(TokenKind.ASTERISK, left, right);
		
		// Create a scope for type checking
		Scope scope = new Scope();
		
		// Test that multiplication returns INT type
		Type resultType = multiplication.checkType(diagnostic, scope);
		assertEquals("Type_int", resultType.toString());
	}

	@Test
	public void testDivisionType() {
		ASTMaker m = new ASTMaker("10 / 2");
		// Create AST for "10 / 2"
		Expression left = m.createNumber("10");
		Expression right = m.createNumber("2");
		Expression division = m.createBinary(TokenKind.SLASH, left, right);
		
		// Create a scope for type checking
		Scope scope = new Scope();
		
		// Test that division returns INT type
		Type resultType = division.checkType(diagnostic, scope);
		assertEquals("Type_int", resultType.toString());
	}

	@Test
	public void testInvalidMultiplication() {
	    // y is a pointer, z is int, so y * z should be invalid
	    String code = "int z; int *y; void f() { y * z; }";
	    // The error should be on line 1, column (where y * z starts)
	    checkCodeNegative(code, 1, 28); // Adjust line/column as needed
	}

	@Test
	public void testInvalidDivision() {
	    // y is a pointer, z is int, so y / z should be invalid
	    String code = "int z; int *y; void f() { y / z; }";
	    checkCodeNegative(code, 1, 28); // Adjust line/column as needed
	}

	@Test
	public void testPlusPositive(){
		String code = "void f() { int a = 1; int b = 2; a + b; }";
		checkCode(code); // Adjust column as needed
	}

}
