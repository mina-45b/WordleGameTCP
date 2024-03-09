package net;

import model.Board;
import model.Logic;
import model.Move;
import java.io.*;
import java.net.Socket;

/**
 * Clase que representa un hilo para la comunicación con un cliente en el juego Wordle.
 */
public class ThreadTcp implements Runnable{
    Socket clientSocket = null;
    InputStream in = null;
    OutputStream out = null;
    Logic logicWordle;
    Board board;
    String winnerWord;
    boolean gameOver;

    public ThreadTcp(Socket clientSocket, Logic logicWordle, Board board) throws IOException {
        this.clientSocket = clientSocket;
        this.logicWordle = logicWordle;
        this.board = board;
        gameOver = false;
        in = clientSocket.getInputStream();
        out = clientSocket.getOutputStream();
    }

    /**
     * Implementa la lógica del juego y la comunicación con un cliente específico.
     */
    public void run() {
        winnerWord = logicWordle.getWinnerWord();
        System.out.println("Winner word is: " + winnerWord);
        Move moves = null;
        try {
            while (!gameOver) {

                ObjectOutputStream oos = new ObjectOutputStream(out); //Envia el tablero al cliente
                oos.writeObject(board);
                oos.flush();

                ObjectInputStream ois = new ObjectInputStream(in);
                try {
                    moves = (Move) ois.readObject(); //Lee el movimiento
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("move: " + moves.getName() + "->" + moves.getWord());
                if(!board.players.containsKey(moves.getName())) {
                    board.players.put(moves.getName(), 1);
                } else {
                    int attempts = board.players.get(moves.getName()) + 1;
                    board.players.put(moves.getName(), attempts);
                }

                //Comprobación de la palabra
                board.response = logicWordle.check(moves.getWord(), board.players.get(moves.getName()));

               if (board.response.equals("winner")) {
                   gameOver = true;
                   System.out.println(moves.getName()+" is the winner");
                   board.finished++;
                   System.out.println("current players: "+(board.getNumPlayers()-board.finished)+" of "+board.getNumPlayers());
               } else if (board.response.equals("loser")) {
                   gameOver = true;
                   System.out.println(moves.getName()+" lost");
                   board.finished++;
                   System.out.println("current players: "+(board.getNumPlayers()-board.finished)+" of "+board.getNumPlayers());
               } else if (board.response.equals("invalid")) {
                   int attempts = board.players.get(moves.getName()) - 1;
                   board.players.put(moves.getName(), attempts);
               }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(board);
            oos.flush();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
