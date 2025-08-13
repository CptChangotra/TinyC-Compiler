package tinycc.implementation.type;

import java.util.List;

public class FunctionType extends Type{
    //private Token token;
    private Type returnType;
    private List<Type> functionArguments;

    public FunctionType(/*Token token, */ Type returnType, List<Type> functionArguments){
        //this.token = token;
        this.returnType = returnType;
        this.functionArguments = functionArguments;
    }

    /*public Token getLocation(){
        return this.token;
    } */

    public Type getReturnType(){
        return this.returnType;
    }

    public List<Type> getFunctionArguments() {
        return this.functionArguments;
    }

    private String printArguments(List<Type> functionArguments){
        String arguments = "";
        int count = 0;
        for (Type argument : functionArguments){
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
        if (functionArguments.isEmpty()){
             return "FunctionType[" + getReturnType() + printArguments(functionArguments) + "]";
        } else {
                    return "FunctionType[" + getReturnType() + ", " + printArguments(functionArguments) + "]";
        }

    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FunctionType)) return false;
        FunctionType other = (FunctionType) o;

        // Check return type equality
        if (!this.returnType.equals(other.returnType)) return false;

        // Check argument list size
        if (this.functionArguments.size() != other.functionArguments.size()) return false;

        // Check each argument type for equality (order matters)
        for (int i = 0; i < this.functionArguments.size(); ++i) {
            if (!this.functionArguments.get(i).equals(other.functionArguments.get(i)))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = returnType.hashCode();
        for (Type arg : functionArguments) {
            result = 31 * result + arg.hashCode();
        }
        return result;
    }

}
