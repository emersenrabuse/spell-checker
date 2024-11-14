o.File;
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

    public int getSimilarity(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];

        // Initialize the dp array
        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j - 1] + (word1.charAt(i - 1) == word2.charAt(j - 1) ? 0 : 1), dp[i - 1][j] + 1), dp[i][j - 1] + 1);
                }
            }
        }

        return dp[len1][len2];
    }

    public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN) {
        ArrayList<String> suggestions = new ArrayList<>();

        // Define max distance to tolerate
        int maxDistance = 2;  // Allow up to 2 edits

        // Iterate through the dictionary words
        for (String dictWord : dictionaryWords) {
            // If the word length is drastically different, skip 
            if (Math.abs(word.length() - dictWord.length()) > 3) {
                continue;  // Skip words with more than 3 length difference
            }

            int distance = getSimilarity(word, dictWord);

            double similarity = 1.0 - (double) distance / Math.max(word.length(), dictWord.length());

            if (similarity >= commonPercent && distance <= maxDistance) {
                suggestions.add(dictWord);
            }

            // Stop if we have reached the desired number of suggestions
            if (suggestions.size() >= topN) {
                break;
            }
        }

        return suggestions;
    }
}
