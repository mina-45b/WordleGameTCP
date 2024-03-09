package net;

import model.Board;
import model.Logic;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que representa el servidor para el juego Wordle.
 */
public class ServerTcp {
    int port;
    Logic logicWordle;
    Board board;
    public ServerTcp(int port) {
        this.port = port;
        logicWordle = new Logic();
        board = new Board();
        System.out.println("Server opened by port "+port);
    }

    /**
     * Método que escucha y acepta conexiones de clientes, crea un hilo para cada cliente.
     */
    public void listen() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Connection with "+clientSocket.getInetAddress());

                board.addNumPlayers();
                ThreadTcp ChildServer = new ThreadTcp(clientSocket, logicWordle, board);
                Thread client = new Thread(ChildServer);
                client.start();
            }

        } catch (IOException e) {
            Logger.getLogger(ServerTcp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void main(String[] args) {
        ServerTcp serverTcp = new ServerTcp(5558);
        serverTcp.listen();
    }

}
