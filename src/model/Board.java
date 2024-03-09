package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Board implements Serializable {
    public Map<String,Integer> players;
    public int finished;

    public String response;
    private int numPlayers;

    public Board() {
        players = new HashMap<>();
        finished = 0;
        response = "";
    }
    public int getNumPlayers() {
        return numPlayers;
    }

    public void addNumPlayers() {
        this.numPlayers++;
    }

    @Override
    public String toString() {
       StringBuilder stringBuilder = new StringBuilder();
       players.forEach((k,v) ->stringBuilder.append("player: "+k + " attempts: " + v+"\n"));
       return stringBuilder.toString();
    }
}

