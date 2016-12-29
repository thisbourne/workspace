package warmup;

import java.util.Set;
import java.util.*;

public class Quadratic {

    /**
     * Find the integer roots of a quadratic equation, ax^2 + bx + c = 0.
     * @param a coefficient of x^2
     * @param b coefficient of x
     * @param c constant term.  Requires that a, b, and c are not ALL zero.
     * @return all integers x such that ax^2 + bx + c = 0.
     */
    public static Set<Integer> roots(int a, int b, int c) {
        if (a == 0 && b ==0 && c == 0){
            throw new IllegalArgumentException("a, b, and c must all be non-zero");
            }
        else{
            Set<Integer> roots = new HashSet<Integer>();
            long sqrt_term = square_root_term(a, b, c);
            long result1 = 0, result2 =0;
            if (sqrt_term >=0) {
                if (a !=0){
                    if ((-b + (long)Math.sqrt(sqrt_term))%(2*a) == 0) {
                         result1 = (-b + (long)Math.sqrt(sqrt_term))/(2*a);
                         roots.add((int)result1);
                    }
                    if ((-b - (long)Math.sqrt(sqrt_term))%(2*a) == 0){
                         result2 = (-b - (long)Math.sqrt(sqrt_term))/(2*a);
                         roots.add((int)result2);
                    } 
                }
                else{
                    if ((-c % b == 0) && (b !=0)) roots.add(-c/b);
                }
             }
                
            else{
                //throw new RuntimeException("root is complex number");
                return roots;
            }
 
                return roots;
        }
    }

 

    private static long square_root_term(int a, int b, int c) {
        return (((long)b*(long)b) - (4*(long)a*(long)c));
    }

    
    /**
     * Main function of program.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("For the equation x^2 - 4x + 3 = 0, the possible solutions are:");
        Set<Integer> result = roots(1, -4, 3);
        System.out.println(result);
    }

    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
