package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que representa el estado del juego Wordle.
 */
public class Board implements Serializable {
    public Map<String,Integer> players;
    public int finished;

    public String response; //Respuesta de la comprobación de la lógica
    private int numPlayers;

    public Board() {
        players = new HashMap<>();
        finished = 0;
        response = "";
    }

    /**
    * Obtiene el número actual de jugadores.
    */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Incrementa el contador de jugadores.
     */
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

