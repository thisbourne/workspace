package expressivo;

import java.util.Map;

public class Plus implements Expression {
    
    //Rep
    private final Expression left;
    private final Expression right;
    
    // Rep Invariant:            left and right are Immutable expressions
    // Abstration Function:      left and right represent the expressions on the left and right
    //                           side of the addition operator
    // Safety from rep exposure: All fields are private;
    //                           left and right are immutable fields
    
    public Plus(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }
    
    @Override
    public String toString(){
        return "(" + left.toString() + " + " + right.toString() + ")";
    }
    
    @Override
    public boolean equals(Object thatObject){
        if(!(thatObject instanceof Plus)) return false;
        Plus thatPlus = (Plus) thatObject;
        return this.left.equals(thatPlus.left) && this.right.equals(thatPlus.right);
    }
    
    @Override
    public int hashCode(){
        return left.hashCode() + right.hashCode();
    }
    
    @Override
    public Expression differentiate (String variable){
        return new Plus(this.left.differentiate(variable), this.right.differentiate(variable));
    }
    
    @Override
    public boolean isSymbolic(){
        return (left.isSymbolic() || right.isSymbolic()) ? true : false;
    }
    
    @Override
    public Expression simplify(Map<String,Double> environment){
        Expression leftSimplified = left.simplify(environment);
        Expression rightSimplified = right.simplify(environment);
        if((!leftSimplified.isSymbolic()) && (!rightSimplified.isSymbolic())){
            return new Number(Double.valueOf(leftSimplified.toString()) + Double.valueOf(rightSimplified.toString()));
        }
        else return new Plus(leftSimplified, rightSimplified);
    }

}
