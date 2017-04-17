package expressivo;

import java.util.Map;

public class Variable implements Expression {
    
    //Rep
    private final String variable;
    
    // Rep Invariant: variable is a String representing a variable of an expression
    //                acceptable values for variable are [a-zA-Z]
    // Abstration Function: Variable represents a variable used in an expression
    // Safety from rep exposure:
    //   All fields are private;
    //   Variable is an immutable type String
    
    public Variable(String variable){
        this.variable = variable;
    }
    
    @Override
    public String toString(){
        return variable;
    }
    
    @Override
    public boolean equals(Object thatObject){
        if(!(thatObject instanceof Variable)) return false;
        Variable thatVariable = (Variable) thatObject;
        return this.variable.equals(thatVariable.variable);
    }
    
    @Override
    public int hashCode(){
        return variable.hashCode();
    }
    
    @Override
    public Expression differentiate (String variable){
        if(this.variable.equals(variable)) return new Number (1);
        else return new Number (0);
    }
    
    @Override
    public boolean isSymbolic(){
        return true;
    }
    
    @Override
    public Expression simplify(Map<String,Double> environment){
        if(environment.containsKey(variable)) return new Number(environment.get(variable));
        else return this;
    }

}
