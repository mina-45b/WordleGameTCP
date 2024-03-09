package net;

import model.Board;
import model.Move;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTcp  extends Thread{
    String name;
    Socket socket;
    InputStream in;
    OutputStream out;
    Board board;
    Move move;
    Scanner scanner;
    boolean continueConnected;

    public ClientTcp(String hostname, int port) {
        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (UnknownHostException e) {
            System.out.println("Host not found: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        continueConnected = true;
        scanner = new Scanner(System.in);
        move = new Move();
    }

    @Override
    public void run() {

        while (continueConnected) {

            board = getRequest();

            System.out.println(board);
            switch (board.response) {
                case "loser":
                    System.out.println("You lose! You haven't guessed the word :(");
                    continueConnected = false;
                    break;
                case "winner":
                    System.out.println("You won! :)");
                    continueConnected = false;
                    break;
                default:
                    if (board.response.isEmpty()) {
                        System.out.println("Let's get started, "+name+"!"+" Current players: " +board.getNumPlayers());
                    }
                    if (board.response.equals("invalid")) {
                        System.out.println("Must be a 5-letter word");
                    } else {
                        System.out.println(board.response);
                    }
                    System.out.println("Please enter a word:");
                    move.setWord(scanner.next());
                    move.setName(name);
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(out);
                        oos.writeObject(move);
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        close(socket);
    }

    private Board getRequest() {
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            board = (Board) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return board;
    }

    private void close(Socket socket){
        try {            if(socket!=null && !socket.isClosed()){
                if(!socket.isInputShutdown()){
                    socket.shutdownInput();
                }
                if(!socket.isOutputShutdown()){
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientTcp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to wordle");
        String player;
        System.out.println("What is your name? ");
        player = sc.next();

        ClientTcp clientTcp = new ClientTcp("localhost",5558);
        clientTcp.name = player;
        clientTcp.start();
    }
}
