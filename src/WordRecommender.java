import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class WordRecommender {

    private ArrayList<String> dictionaryWords;

    public WordRecommender(String dictionaryFile) {
        dictionaryWords = new ArrayList<>();
        loadDictionary(dictionaryFile);
    }

    private void loadDictionary(String dictionaryFile) {
        try (Scanner scnr = new Scanner (new File(dictionaryFile))) {
            while (scnr.hasNextLine()) {
                dictionaryWords.add(scnr.nextLine().trim());
            }
        } catch (FileNotFoundException e) {
            System.out.println(Util.FILE_OPENING_ERROR);
        }
    }

    public boolean isWordInDictionary(String word) {
        return dictionaryWords.contains(word);
    }

    public double getSimilarity(String word1, String word2) {
        int commonLetters = 0;
        for (char c : word1.toCharArray()) {
            if (word2.indexOf(c) != -1) commonLetters++;
        }
        return (double) commonLetters / Math.max(word1.length(), word2.length());
    }

    public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN) {
        ArrayList <String> suggestions = new ArrayList<>();
        for (String dictWord : dictionaryWords) {
            double similarity = getSimilarity(word, dictWord);
            if (similarity >= commonPercent) {
                suggestions.add(dictWord);
                if (suggestions.size() >= topN) break;
            }
        }
        return suggestions;
    }


}