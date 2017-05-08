package minesweeper.server;

public class BoardRequest {

    private final String request;
    private final int x; 
    private final int y;
    
    public BoardRequest(String request, int x, int y){
        this.x = x;
        this.y = y;
        this.request = request;
    }
    
    public BoardRequest(String request){
        this.request = request;
        this.x = -1;
        this.y = -1;
    }
    
    public String getRequest(){
        return request;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
}