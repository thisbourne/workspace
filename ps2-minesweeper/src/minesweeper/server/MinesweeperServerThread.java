package minesweeper.server;

import java.net.*;
import java.io.*;

public class MinesweeperServerThread implements Runnable {
    
    private Socket socket = null;
    private MinesweeperServer server = null;
    
    public MinesweeperServerThread(Socket socket, MinesweeperServer server){
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
        try {
            //server.handleConnection(socket);
            System.out.println("I'm here");
            socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace(); // but don't terminate serve()
        } 

    }

}
