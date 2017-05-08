package minesweeper.server;

public class BoardReply {

    private final String message;
    private final boolean bomb;
    
    public BoardReply(String message, boolean bomb){
        this.message = message;
        this.bomb = bomb;
    }
    
    public String getMessage(){
        return message;
    }
    
    public boolean getBomb(){
        return bomb;
    }
}
