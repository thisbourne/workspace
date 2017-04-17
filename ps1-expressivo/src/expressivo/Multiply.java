package expressivo;

import java.util.Map;

public class Multiply implements Expression {
    
    //Rep
    private final Expression left;
    private final Expression right;
    
    // Rep Invariant:            left and right are Immutable expressions
    // Abstration Function:      left and right represent the expressions on the left and right
    //                           side of the multiplication operator
    // Safety from rep exposure: All fields are private;
    //                           left and right are immutable fields
    
    public Multiply(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }
    
    @Override
    public String toString(){
        return left.toString() + " * " + right.toString();
    }
    
    @Override
    public boolean equals(Object thatObject){
        if(!(thatObject instanceof Multiply)) return false;
        Multiply thatMultiply = (Multiply) thatObject;
        return this.left.equals(thatMultiply.left) && this.right.equals(thatMultiply.right);
    }
    
    @Override
    public int hashCode(){
        return left.hashCode() + right.hashCode();
    }
    
    @Override
    public Expression differentiate (String variable){
        Expression leftDiff = new Multiply(left, this.right.differentiate(variable));
        Expression rightDiff = new Multiply(right, this.left.differentiate(variable));
        return new Plus(leftDiff, rightDiff);
    }
    
    @Override
    public boolean isSymbolic(){
        return (left.isSymbolic() || right.isSymbolic()) ? true : false;
    }
    
    @Override
    public Expression simplify(Map<String,Double> environment){
        Expression leftSimplified = left.simplify(environment);
        Expression rightSimplified = right.simplify(environment);
        System.out.println("left expr: " + leftSimplified.toString() + " is Symbolic: " + leftSimplified.isSymbolic());
        System.out.println("right expr: " + rightSimplified.toString() + " is Symbolic: " + rightSimplified.isSymbolic());
        if((!leftSimplified.isSymbolic()) && (!rightSimplified.isSymbolic())){
            return new Number(Double.valueOf(leftSimplified.toString()) * Double.valueOf(rightSimplified.toString()));
        }
        else return new Multiply(leftSimplified, rightSimplified);
    }

}
