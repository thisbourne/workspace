package minesweeper;



public class BoardSquare {
    
    //rep
    //private final int row;
    //private final int column;
    private boolean bomb;
    private int proximityBombs;
    private char state;
    
    //private final Set<BoardSquare> neighbors;
    
    BoardSquare(boolean bomb, int proximityBombs, char state){
        //this.row = row;
        //this.column = column;
        this.bomb = bomb;
        this.proximityBombs = proximityBombs;
        this.state = state;
    }
    
    public boolean getBomb() {
        boolean result = bomb ? true : false;
        return result;
    }
    
    public void setBomb(boolean bomb){
        this.bomb = bomb;
    }
    
    public int getProximityBombs(){
        return proximityBombs;
    }
    
    public void setProximityBombs(int bombs){
        this.proximityBombs = bombs;
    }
    
    public char getState(){
        return state;
    }
    
    public void setState( char state){
        this.state = state;
    }
    

}
