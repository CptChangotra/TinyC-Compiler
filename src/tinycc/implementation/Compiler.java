
package tinycc.implementation;

import tinycc.diagnostic.Diagnostic;
import tinycc.implementation.semantic.Scope;
import tinycc.implementation.statement.Declaration;
import tinycc.implementation.statement.Statement;
import tinycc.implementation.type.FunctionType;
import tinycc.implementation.type.Type;
import tinycc.implementation.ExternalDeclaration;
import tinycc.implementation.FunctionDefinition;
import tinycc.parser.ASTFactory;
import tinycc.parser.Lexer;
import tinycc.parser.Parser;
import tinycc.logic.Formula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tinycc.asmgen.AsmGen;

/**
 * The main compiler class.
 *
 * An instance of this class will handle a single translation unit (e.g. input
 * file). There will be multiple instances of your class during runtime of your
 * compiler. You can change this class but the given name and signature of
 * methods and the name of the class must not be modified.
 */
public class Compiler {

Diagnostic diagnostic;
private ASTFactoryImplementation astFactory;

	/**
	 * Initializes the compiler class with the given diagnostic module
	 *
	 * @param diagnostic The diagnostic module to use
	 * @see Diagnostic
	 */
	public Compiler(final Diagnostic diagnostic) {
		this.diagnostic = diagnostic;
	}

	/**
	 * Returns the current ASTFactory which is used internally.
	 *
	 * @return The current ASTFactory which is used internally.
	 * @see ASTFactory
	 */
	public ASTFactory getASTFactory() {
		if (astFactory == null) {
			astFactory = new ASTFactoryImplementation();
		}
		return astFactory;
	}

	/**
	 * Parses a single translation unit which is given by an instance of the Lexer
	 * class.
	 *
	 * @param lexer The lexer to use
	 * @see Lexer
	 * @remarks This function is invoked only once in each instance of the compiler
	 *          class.
	 */
	public void parseTranslationUnit(final Lexer lexer) {
		Parser parser = new Parser(diagnostic, lexer, this.getASTFactory());
		parser.parseTranslationUnit();
	}

	/**
	 * Checks the semantics of the input program.
	 *
	 * @see ASTFactory
	 * @remarks Use the diagnostics module to report errors. This function is
	 *          invoked only once in each instance of the compiler class.
	 */
	public void checkSemantics() {
		// Use the ASTFactory that was used during parsing
		ASTFactoryImplementation factory = (ASTFactoryImplementation) getASTFactory();

		// Create the global scope
		Scope globalScope = new Scope();

		// Process all declarations and collect functions
		Map<String, List<Object>> functions = new HashMap<>();
		Map<String, Object> globalVars = new HashMap<>();
		
		for (Object decl : factory.getDeclarations()) {
			if (decl instanceof ExternalDeclaration) {
				ExternalDeclaration ext = (ExternalDeclaration) decl;
				// If it's a function prototype, collect it
				if (ext.getType() instanceof FunctionType) {
					String name = ext.getName().getText();
					// Check for conflict with global variable
					if (globalVars.containsKey(name)) {
						diagnostic.printError(ext.getName(), "Function '%s' conflicts with global variable", name);
					}
					// Get or create list for this function
					List<Object> list = functions.get(name);
					if (list == null) {
						list = new ArrayList<>();
						functions.put(name, list);
					}
					list.add(ext);
				} else {
					// Global variable - check for conflict with function
					String name = ext.getName().getText();
					if (functions.containsKey(name)) {
						diagnostic.printError(ext.getName(), "Global variable '%s' conflicts with function", name);
					}
					globalVars.put(name, ext);
					ext.checkType(diagnostic, globalScope);
				}
			} else if (decl instanceof FunctionDefinition) {
				FunctionDefinition func = (FunctionDefinition) decl;
				String name = func.getFunctionName().getText();
				// Check for conflict with global variable
				if (globalVars.containsKey(name)) {
					diagnostic.printError(func.getFunctionName(), "Function '%s' conflicts with global variable", name);
				}
				// Get or create list for this function
				List<Object> list = functions.get(name);
				if (list == null) {
					list = new ArrayList<>();
					functions.put(name, list);
				}
				list.add(func);
			}
		}
		
		// Process each function
		for (String funcName : functions.keySet()) {
			List<Object> items = functions.get(funcName);
			List<ExternalDeclaration> prototypes = new ArrayList<>();
			List<FunctionDefinition> definitions = new ArrayList<>();
			
			// Separate prototypes and definitions
			for (Object item : items) {
				if (item instanceof ExternalDeclaration) {
					prototypes.add((ExternalDeclaration) item);
				} else if (item instanceof FunctionDefinition) {
					definitions.add((FunctionDefinition) item);
				}
			}
			
			// Check for multiple definitions
			if (definitions.size() > 1) {
				for (int i = 1; i < definitions.size(); i++) {
					diagnostic.printError(definitions.get(i).getFunctionName(), 
						"Redefinition of function '%s'", funcName);
				}
			}
			
			// Check for conflicting prototypes
			for (int i = 1; i < prototypes.size(); i++) {
				if (!prototypes.get(0).getType().equals(prototypes.get(i).getType())) {
					diagnostic.printError(prototypes.get(i).getName(), 
						"Conflicting types for function '%s'", funcName);
				}
			}
			
			// Check for type mismatch between prototype and definition
			if (!definitions.isEmpty() && !prototypes.isEmpty()) {
				if (!prototypes.get(0).getType().equals(definitions.get(0).getFunctionType())) {
					diagnostic.printError(definitions.get(0).getFunctionName(), 
						"Conflicting types for function '%s'", funcName);
				}
			}
			
			// Register the function (prefer definition over prototype)
			Object toRegister = null;
			if (!definitions.isEmpty()) {
				toRegister = definitions.get(0);
			} else if (!prototypes.isEmpty()) {
				toRegister = prototypes.get(0);
			}
			
			if (toRegister != null) {
				try {
					if (toRegister instanceof FunctionDefinition) {
						FunctionDefinition def = (FunctionDefinition) toRegister;
						globalScope.add(funcName, new Declaration(
							def.getFunctionType(), def.getFunctionName(), null));
					} else {
						ExternalDeclaration ext = (ExternalDeclaration) toRegister;
						globalScope.add(funcName, new Declaration(
							ext.getType(), ext.getName(), null));
					}
				} catch (Exception e) {
					// This shouldn't happen since we're checking conflicts above
					if (toRegister instanceof FunctionDefinition) {
						diagnostic.printError(((FunctionDefinition) toRegister).getFunctionName(), 
							"Function '%s' already declared", funcName);
					} else {
						diagnostic.printError(((ExternalDeclaration) toRegister).getName(), 
							"Function '%s' already declared", funcName);
					}
				}
			}
		}

		// Check function bodies
		for (FunctionDefinition function : factory.getFunctionDefinitions()) {
			function.checkType(diagnostic, globalScope);
		}
	}

	/**
	 * Performs optimizations on the input program.
	 *
	 * @remarks Bonus exercise.
	 */
	public void performOptimizations() {
		throw new UnsupportedOperationException("TODO: implement this");
	}

	/**
	 * Generates code for the input program.
	 *
	 * @param out The target output stream.
	 * @remarks This function is invoked only once in each instance of the compiler
	 *          class. Only necessary if mentioned in the project description.
	 */
	public void generateCode(final AsmGen out) {
		throw new UnsupportedOperationException("TODO: implement this");
	}

	/**
	 * Generates verification conditions for the input program.
	 *
	 * @remarks This function is invoked only once in each instance of the compiler
	 *          class. Only necessary if mentioned in the project description.
	 */
	public Formula genVerificationConditions() {
		throw new UnsupportedOperationException("TODO: implement this");
	}
}
