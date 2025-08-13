package tinycc.implementation.type;

public class PointerType extends Type {
    private Type pointsTo;

    public PointerType(Type pointsTo){
        this.pointsTo = pointsTo;
    }

    public Type getPointsTo(){
        return this.pointsTo;
    }

    public String toString(){
        return "Pointer[" + pointsTo.toString() + "]";
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PointerType)) return false;
        PointerType other = (PointerType) o;
        return this.pointsTo.equals(other.pointsTo);
    }

    @Override
    public int hashCode() {
        return 31 * pointsTo.hashCode();
    }

}
