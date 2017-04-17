/* Copyright (c) 2007-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import minesweeper.BoardSquare;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Specification
 */
public class Board {
    
    //rep
    private final BoardSquare[] board;
    private final int rows;
    private final int columns;
    
    // Abstraction function:
    // the board array of BoardSqures represents the minesweeper board state in which element 0 represents board square at 0,0
    // the board squares for each row occurs consecutively in the character array; 
    // rows equals the number of rows in the board
    // columns represents the number of columns of the board
    // so, for example, the boardsquare at 3, 2 where 3 is the row and 2 is the column will be at 
    // the element (3 + 1) * (columns) + 2 + 1;
    // an element of board can be either "-" for untouched, "F" for flagged, " "(space) for dug and 0 neighbors with bomb
    
    // Rep Invariant:
    // 0 < rows;
    // 0 < columns;
    // rows = columns;
    
    // TODO: Abstraction function, rep invariant, rep exposure, thread safety
    
    public Board( int rows, int columns, String boardString){
        this.rows = rows;
        this.columns = columns;
        board = initialize(boardString);
    }
    
    
    public String look(){
        String result = "";
        for( int j= 0; j < rows; j++){
            for( int i = 0; i < columns; i++){
                result = result + board[(j * rows) + i].getState()+ " ";
            }
            result = result.substring(0,(2*(j+1)*columns)-1) + "\n";
        }
        result = result.substring(0, (2*rows*columns)-1);
        //System.out.print(result);
        
        return result;
    }
    
    public boolean dig(int x, int y){
        
        //check if square is on board
        if ((x<0) || (x >= rows) || (y < 0) || (y >= columns)) return false;
        
        //get square
        BoardSquare square = board[(rows *x) + y];
        //check if square is untouched
        if (square.getState() != '-') return false;
        //process dig request
        if(square.getProximityBombs() != 0){
            square.setState(Integer.toString(square.getProximityBombs()).charAt(0));
        }
        else{
            square.setState(' ');
        }
        if (square.getBomb()){
            square.setBomb(false);
            removeBomb(x, y);
            if (square.getState() == ' ') clearArea(x, y);
            return true;   
        }
        else{
            if (square.getState() == ' ') clearArea(x, y);
            return false;
        }
    }
    
    public void flag(int x, int y){
      //check if square is on board
        if ((x<0) || (x >= rows) || (y < 0) || (y >= columns)) return;
        
        //get square
        BoardSquare square = board[(rows *x) + y];
        //check if square is untouched
        if (square.getState() == '-') square.setState('F');
        return;
    }
    
    public void deflag(int x, int y){
        //check if square is on board
          if ((x<0) || (x >= rows) || (y < 0) || (y >= columns)) return;
          
          //get square
          BoardSquare square = board[(rows *x) + y];
          //check if square is untouched
          if (square.getState() == 'F') square.setState('-');
          return;
      }
    
    public int getColumns(){
        return columns;
    }
    
    public int getRows(){
        return rows;
    }
    
    //for testing only
    
    public BoardSquare getSquare(int x, int y){
        boolean hasBomb = board[(x*columns) + y].getBomb();
        int proximityBombs = board[(x*columns) + y].getProximityBombs();
        char state = board[(x*columns) + y].getState();
        BoardSquare squareCopy = new BoardSquare(hasBomb, proximityBombs, state);
        return squareCopy;
        //return board[(x*columns) + y];
    }
    
    private void removeBomb(int x, int y){
        for (int j = x-1; j <= x+1; j++){
            for (int i = y-1; i <= y+1; i++){
                
                //check to make sure not out of bounds
                if ((j>=0) && (j < rows) && (i >= 0) && (i < columns)){
                
                    BoardSquare square = board[(j*columns) + i];
                    int bombs = square.getProximityBombs();
                    if ((j!=x) || (i!=y)) square.setProximityBombs(bombs - 1);
                    if (square.getState() != '-' && square.getState() != 'F'){
                        if(square.getProximityBombs() != 0){
                            square.setState(Integer.toString(square.getProximityBombs()).charAt(0));
                        }
                        else{
                            square.setState(' ');
                        }
                    }
                }
            }
        }
    }
    
    private void clearArea(int x, int y){
        for (int j = x-1; j <= x+1; j++){
            for (int i = y-1; i <= y+1; i++){
                
                //check to make sure not out of bounds
                if ((j>=0) && (j < rows) && (i >= 0) && (i < columns)){
                
                    BoardSquare square = board[(j*columns) + i];
                    if (square.getProximityBombs() == 0){
                        dig(j,i); //square.setState(' ');
                        //clearArea(j, i);
                    }
                    else square.setState(Integer.toString(square.getProximityBombs()).charAt(0));
                }
            }
        }
    }
    
    private BoardSquare[] initialize(String boardString){
        // get bomb values for each square
        String[] boardArray = boardString.split("[\\s\\n\\r]+");
        // initialize return array
        //System.out.println("the number of rows is: " + rows);
        //System.out.println("the number of columns is: " + columns);
        BoardSquare[] result = new BoardSquare[rows*columns];
        // set values for board with inital bomb proximity count of 0
        for (int j = 0; j < rows; j++){
            for (int i = 0; i < columns; i++){
                //System.out.println("At row: " + j + " and col: " + i);
                //System.out.println("boardArray element is: " + boardArray[(j*columns)+i]);
                boolean bomb = (boardArray[(j*columns)+i].equals("1")) ? true : false;
                //System.out.println("value of bomb is" + bomb +"\n");
                result[(j*columns) + i] = new BoardSquare(bomb, 0, '-');
            }
        }
        // count proximity bombs
        for (int j = 0; j < rows; j++){
            for (int i = 0; i < columns; i++){
                countBombs(j, i, result);
            }
        }
        
        return result;
    }
    
    private void countBombs(int x, int y, BoardSquare [] board){
        int count =0;
        for( int j = x-1; j <= x+1; j++){
            for( int i = y-1; i <= y+1; i++){
                //check to make sure not out of bounds
                if ((j>=0) && (j < rows) && (i >= 0) && (i < columns)){
                    if (board[(j*columns) + i].getBomb() == true) count++;
                }
            }
        }
        if (board[(x*columns) + y].getBomb() == true){
            board[(x*columns) + y].setProximityBombs(count -1);
        }
        else{
            board[(x*columns) + y].setProximityBombs(count);
        }
    }
     
    
    // TODO: Specify, test, and implement in problem 2
    
}