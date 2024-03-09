package model;

import java.io.Serializable;

    /**
    * Clase que representa el movimiento de un jugador en el juego Wordle.
    */
public class Move implements Serializable {
    String name;
    String word;

    public String getName() {
        return name;
    }

    /**
    * Establece el nombre del jugador para el movimiento.
    */
    public void setName(String name) {
        this.name = name;
    }

    public String getWord() {
        return word;
    }


    /**
     * Establece la palabra ingresada por el jugador para el movimiento.
     */
    public void setWord(String word) {
        this.word = word;
    }
}
