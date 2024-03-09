package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Logic {
    String winnerWord;
    final int numAttempts = 6;
    final int wordLetter = 5;

    ArrayList<String> wordsList = new ArrayList<>();

    String redWords = "\u001B[31m";
    String greenWords = "\u001B[32m";
    String yellowWords = "\u001B[33m";
    String originalColor = "\u001B[00m";

    public Logic() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/words/words.txt");
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = reader.readLine();

                while (line != null) {
                    wordsList.add(line.trim());
                    line = reader.readLine();
                }
                reader.close();
            } else {
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        winnerWord = randomWord();
    }

    public String check(String word, int attempts) {
        String response;

        if (word.toUpperCase().equals(winnerWord)) {
            response = "winner";
        } else if (attempts == numAttempts) {
            response = "loser";
        } else if (word.length() != wordLetter) {
            response = "invalid";
        } else {
            response = checkWinner(word, winnerWord);
        }
        return response;
    }

    public String randomWord() {
        Random random = new Random();
        int position = random.nextInt(wordsList.size()); //Extrae una palabra aleatoria según el índice
        return  wordsList.get(position);
    }

    public String checkWinner(String clientWord, String winnerWord) {
        clientWord = clientWord.toUpperCase();

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < clientWord.length(); i++) {
            char c = clientWord.charAt(i);
            if (winnerWord.indexOf(c) == i) {
                result.append(greenWords).append(c).append(originalColor);
            } else if (winnerWord.indexOf(c) != -1) {
                result.append(yellowWords).append(c).append(originalColor);
            } else {
                result.append(redWords).append(c).append(originalColor);
            }
        }
        return result.toString();
    }
    public String getWinnerWord() {
        return winnerWord;
    }
}
