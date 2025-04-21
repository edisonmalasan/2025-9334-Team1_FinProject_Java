package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordGenerator {
    public static String fileName = "D:\\Coding\\CS222_Testing\\resources\\words.txt"; // To be replaced with SQL implementation
    public static String generateWord(int noOfLetters) {
        List<String> matchingWords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            // Read each line from the file
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Trim any extra spaces

                // Check if the word length matches the given length
                if (line.length() == noOfLetters) {
                    matchingWords.add(line); // Add matching word to the list
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;  // Return null if an error occurs while reading the file
        }

        // If no matching words are found, return null
        if (matchingWords.isEmpty()) {
            return null;
        }

        // Select a random word from the matching words list
        Random random = new Random();
        int randomIndex = random.nextInt(matchingWords.size());
        return matchingWords.get(randomIndex);
    }
}

