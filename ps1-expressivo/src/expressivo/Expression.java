package expressivo;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import lib6005.parser.*;

/**
 * An immutable data type representing a polynomial expression of:
 *   + and *
 *   nonnegative integers and floating-point numbers
 *   variables (case-sensitive nonempty strings of letters)
 * 
 * <p>PS1 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {
    
    // Datatype definition
    //   Expression = Number(number:int) + 
    //                Variable(variable:char) +
    //                Plus(left:Expression, right:Expression) +
    //                Multiply(left:Expression, right:Expression)
    
    enum ExpressionGrammar {ROOT, EXPRESSION, SUM, MULTIPLY, PRIMITIVE, VARIABLE, NUMBER, WHITESPACE};
    
    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS1 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parse(String input) throws IllegalArgumentException{
        Parser<ExpressionGrammar> parser = null;
        ParseTree<ExpressionGrammar> tree = null;
        try {
            parser = GrammarCompiler.compile(new File("/Users/Norm/Documents/workspace/ps1-expressivo/src/expressivo/Expression.g"),
                    ExpressionGrammar.ROOT);
        }
        catch(IOException ioe){
            System.out.println("Norm's Error opening file");
            //throw ioe;
        }
        catch(UnableToParseException utpe){
            System.out.println("Error: check grammar for syntax errors");
            //throw utpe;
        }
        
        try {
            tree = parser.parse(input);
        }
        catch(UnableToParseException utpe){
            System.out.println("Error: input string might be a problem");
            throw new IllegalArgumentException();
        }
        
        Expression expression = buildAST(tree);
        System.out.println(tree.toString());
        return expression;
        

    }
    
    public static Expression buildAST(ParseTree<ExpressionGrammar> p){
        switch(p.getName()){
        
        case NUMBER:
            return new Number(Double.parseDouble(p.getContents()));
            
        case VARIABLE:
            return new Variable(p.getContents());
            
        case PRIMITIVE:
            if(!(p.childrenByName(ExpressionGrammar.NUMBER).isEmpty())){
                return buildAST(p.childrenByName(ExpressionGrammar.NUMBER).get(0));
            }
            else if(!(p.childrenByName(ExpressionGrammar.VARIABLE).isEmpty())){
                return buildAST(p.childrenByName(ExpressionGrammar.VARIABLE).get(0));
            }
            else{
                return buildAST(p.childrenByName(ExpressionGrammar.EXPRESSION).get(0));
            }
            
        case SUM:
            boolean first = true;
            Expression result = null;
            for(ParseTree<ExpressionGrammar> child: p.childrenByName(ExpressionGrammar.MULTIPLY)){
                if(first){
                    result = buildAST(child);
                    first = false;
                }
                else{
                    result = new Plus(result, buildAST(child));
                }
            }
            if (first) {
                throw new RuntimeException("sum must have a non whitespace child:" + p);
            }
            return result;
            
        case MULTIPLY:
            boolean firstMultiply = true;
            Expression resultMultiply = null;
            for(ParseTree<ExpressionGrammar> child: p.childrenByName(ExpressionGrammar.PRIMITIVE)){
                if(firstMultiply){
                    resultMultiply = buildAST(child);
                    firstMultiply = false;
                }
                else{
                    resultMultiply = new Multiply(resultMultiply, buildAST(child));
                }
            }
            if (firstMultiply) {
                throw new RuntimeException("sum must have a non whitespace child:" + p);
            }
            return resultMultiply;
            
        case EXPRESSION:
            if(!(p.childrenByName(ExpressionGrammar.SUM).isEmpty())){
                return buildAST(p.childrenByName(ExpressionGrammar.SUM).get(0));
            }
            else{
                return buildAST(p.childrenByName(ExpressionGrammar.MULTIPLY).get(0));
            }
            
        case ROOT:
            return buildAST(p.childrenByName(ExpressionGrammar.EXPRESSION).get(0));
            
        case WHITESPACE:
            throw new RuntimeException("You should never reach here:" + p);
        }
        
        throw new RuntimeException("You should never reach here:" + p);
    }
    
    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS1 handout.
     */
    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
    
    // TODO more instance methods
    
    /**
    * @param expression is any expression
    * @param variable is any variable
    * @return an Expression expression that is the mathematical differential of the input expression
    *         w.r.t. Variable variable
    */
    public Expression differentiate( String variable);
    
    /**
     * @return boolean - true if it is Symbolic (i.e. contains variables)
     */
    
    public boolean isSymbolic();
    
    /**
     * @param Map<String,Double> environment - a map of in which the keys represent variable names
     *        and the values represent the variable values
     * @return an Expression that has it's variables, which are in environment, replaced with
     *         their numeric values.
     */
    
    public Expression simplify(Map<String, Double> environment);
    
    /* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires permission of course staff.
     */
}
