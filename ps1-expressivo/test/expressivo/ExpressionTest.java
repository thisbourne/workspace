/* Copyright (c) 2015-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;
import java.util.Map;
import java.util.HashMap;

import org.junit.Test;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    /*
     * Testing strategy
     * ==================
     *
     * Number():
     *           Input: decimal number, partition: 0, decimal, whole number value, very small decimal
     *
     * Plus():
     *           Input: left and right; both Expressions; tested by Expression.parse
     *
     * Multiply():
     *           Input: left and right; both Expressions; tested by Expression.parse
     *
     * toString():
     *           Input: the object itself
     *           Partition: 1+4.3; 3+4*9.3; (3+4)*9.3; (3*(5+4.1)+6)*7.3; (0.0007+0.00013)*10345.23;
     *                      x*x*y*x+y*x*z; ((x+y)*(y*y)+6.8)*z
     *           Output: a parsable representation of this expression, such that
     *                   for all e:Expression, e.equals(Expression.parse(e.toString())).
     *
     */

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }


    @Test()
    public void testNewNumberDecimal(){
        Expression number = new Number(8.7);
        assertEquals(number.toString(), "8.7");
    }

    @Test()
    public void testNewNumberZero(){
        Expression number = new Number(0);
        assertEquals(number.toString(), "0.0");
    }

    @Test()
    public void testNewNumberWholeNumber(){
        Expression number = new Number(10);
        assertEquals(number.toString(), "10.0");
    }

    @Test()
    public void testNewNumberSmallDecimal(){
        Expression number = new Number(0.00007);
        assertEquals(number.toString(), "0.000070");
    }

    @Test()
    public void testParseEquals() {
       Object expr1 = Expression.parse("3");
       Object expr2 = Expression.parse("3");
       boolean testValue = expr1.equals(expr2);
       System.out.println("Does expr 1 equal expr 2: " + testValue);
       System.out.println("The Hashcode expr 1: " + expr1.hashCode());
       System.out.println("The Hashcode expr 2: " + expr2.hashCode());
       assertEquals("Expected identical expressions to be equal",
               expr1, expr2);
    }
    
    @Test()
    public void testParseDecimalFractionsIntegerAdd(){
        String expr1String = "(0.10 + 3 + 38.897 + 74) * x";
        Expression expr1 = Expression.parse(expr1String);
        System.out.println(expr1.toString());
    }
    
    @Test()
    public void testParseIntegerVeryLarge(){
        String expr1String = "9007199254740992";
        Expression expr1 = Expression.parse(expr1String);
        System.out.println(expr1.toString());
    }
    
    @Test()
    public void testParseDecimalVerySmall(){
        String expr1String = "0.000001";
        Expression expr1 = Expression.parse(expr1String);
        System.out.println(expr1.toString());
    }
    
    @Test()
    public void testVariousVariableNames(){
        String expr1String = "x * foo * jigglywiggly * HeLlOwOrLd * x";
        Expression expr1 = Expression.parse(expr1String);
        Map<String, Double> environment = new HashMap<String, Double>();
        environment.put("x", 2.0);
        environment.put("y", 3.0);
        environment.put("z", 4.0);
        Expression expr2 = expr1.simplify(environment);
        System.out.println("The simplified expression is: " + expr2.toString());
    }


}
