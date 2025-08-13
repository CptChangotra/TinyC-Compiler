package tinycc.implementation.semantic;

import java.util.HashMap;
import java.util.Map;

import tinycc.implementation.statement.Declaration;
import tinycc.implementation.type.Type;

public class Scope {
    private final Map<String, Declaration> table;
    private final Scope parent;
    private Type currentFunctionReturnType; // Track current function's return type

    public Scope(){
        this(null);
    }

    private Scope(Scope parent){
        this.parent = parent;
        this.table = new HashMap<String, Declaration>();
        this.currentFunctionReturnType = null;
    }

    public Scope newNestedScope(){
        Scope child = new Scope(this);
        child.currentFunctionReturnType = this.currentFunctionReturnType;
        return child;
    }

    public void add(String id, Declaration d) throws Exception{
        if (table.containsKey(id)){
            throw new IllegalArgumentException("Id already exists.");
        }
        table.put(id, d);
    }

    public Declaration lookup(String id) throws Exception{
        // Check current scope
        if (this.table.containsKey(id)) {
            return this.table.get(id);
        }
        // If not found and we have a parent scope, check parent
        if (this.parent != null) {
            return this.parent.lookup(id);
        }
        // Not found in any scope
        throw new Exception("Variable not declared!");
    }
    
    // Methods to track function return type
    public void setCurrentFunctionReturnType(Type returnType) {
        this.currentFunctionReturnType = returnType;
    }
    
    public Type getCurrentFunctionReturnType() {
        return this.currentFunctionReturnType;
    }
}
