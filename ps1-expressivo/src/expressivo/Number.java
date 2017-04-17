package expressivo;

import java.util.Map;
import java.util.regex.*;
import java.lang.Integer;

public class Number implements Expression {

    //Rep
    private final Double number;
    
    // Rep Invariant: number is a double precision decimal number
    // Abstration Function: number represents a real positive value number in an expression
    // Safety from rep exposure:
    //   All fields are private;
    //   number is an immutable type Double
    
    public Number(double number){
        this.number = new Double(number);
    }
    
    @Override
    public String toString(){
        String numberString = number.toString();
        
        //check if string uses scientific notation
        if (numberString.contains("E")){
            return scientificToString(numberString);
        }
        return number.toString();
    }
    
    @Override
    public boolean equals(Object thatObject){
        if(!(thatObject instanceof Number)) return false;
        Number thatNumber = (Number) thatObject;
        return this.number.equals(thatNumber.number);
    }
    
    @Override
    public int hashCode(){
        return number.hashCode();
    }
    
    @Override
    public Expression differentiate (String variable){
        return new Number (0);
    }
    
    @Override
    public boolean isSymbolic(){
        return false;
    }
    
    @Override
    public Expression simplify(Map<String,Double> environment){
        return this;
    }
    
    private String scientificToString(String numberString){
        String fixedNumberString = "";
        Pattern scientificNotation = Pattern.compile("([0-9]*)\\.([0-9]*)E(-?)([0-9]+)");
        Matcher numberParse = scientificNotation.matcher(numberString);
        numberParse.matches();
        /*
        System.out.println("Does it match?: " + numberParse.matches());
        if (numberParse.matches()){
            System.out.println("the number of groups is: " + numberParse.groupCount());
            for(int i=0; i <= numberParse.groupCount(); i++) System.out.println("Group: " + i + " is: " + numberParse.group(i));
            System.out.println("the length of group 1 is: " + numberParse.group(1).length());
            System.out.println("the length of group 2 is: " + numberParse.group(2).length());
            System.out.println("the length of group 3 is: " + numberParse.group(3).length());
            System.out.println("the length of group 4 is: " + numberParse.group(4).length());
        }*/
        fixedNumberString = numberParse.group(1) + numberParse.group(2);
        String power = numberParse.group(4);
        Integer numberZerosToAdd = new Integer(power) - numberParse.group(2).length();
        if(numberParse.group(3).length() == 0){
            for(int i=0; i < numberZerosToAdd; i++){
                fixedNumberString = fixedNumberString + "0";
            }
        }
        else{
            for(int i=0; i < numberZerosToAdd; i++){
                fixedNumberString = "0" + fixedNumberString;
            }
            fixedNumberString = "0." + fixedNumberString;
        }
        //System.out.println(fixedNumberString);
        return fixedNumberString;
    }
}
