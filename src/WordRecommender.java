import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordRecommender {

    private ArrayList<String> dictionaryWords;

    public WordRecommender(String dictionaryFile) {
        dictionaryWords = new ArrayList<>();
        loadDictionary(dictionaryFile);
    }

    private void loadDictionary(String dictionaryFile) {
        try (Scanner scnr = new Scanner(new File(dictionaryFile))) {
            while (scnr.hasNextLine()) {
                dictionaryWords.add(scnr.nextLine().trim());
            }
        } catch (FileNotFoundException e) {
            System.out.println(Util.FILE_OPENING_ERROR);
        }
    }

    public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN) {
        ArrayList<String> suggestions = new ArrayList<>();

        for (String dictWord : dictionaryWords) {
            if (Math.abs(word.length() - dictWord.length()) <= tolerance) {
                double commonCharacterPercentage = calculateCommonCharacterPercentage(word, dictWord);

                if (commonCharacterPercentage >= commonPercent) {
                    double similarity = getSimilarity(word, dictWord);

                    if (suggestions.size() < topN) {
                        suggestions.add(dictWord);
                    } else {
                        String leastSimilarWord = findLeastSimilarWord(word, suggestions);
                        if (similarity > getSimilarity(word, leastSimilarWord)) {
                            suggestions.remove(leastSimilarWord);
                            suggestions.add(dictWord);
                        }
                    }
                }
            }
        }

        return suggestions;
    }

    private double calculateCommonCharacterPercentage(String word1, String word2) {
        Set<Character> aSet = new HashSet<>();
        Set<Character> bSet = new HashSet<>();

        for (char ch : word1.toCharArray()) aSet.add(ch);
        for (char ch : word2.toCharArray()) bSet.add(ch);

        Set<Character> intersection = new HashSet<>(aSet);
        intersection.retainAll(bSet);

        Set<Character> union = new HashSet<>(aSet);
        union.addAll(bSet);

        return (double) intersection.size() / union.size();
    }

    public double getSimilarity(String word1, String word2) {
        int leftSimilarity = calculateLeftSimilarity(word1, word2);
        int rightSimilarity = calculateRightSimilarity(word1, word2);

        return (leftSimilarity + rightSimilarity) / 2.0;
    }

    private int calculateLeftSimilarity(String word1, String word2) {
        int minLength = Math.min(word1.length(), word2.length());
        int similarity = 0;

        for (int i = 0; i < minLength; i++) {
            if (word1.charAt(i) == word2.charAt(i)) similarity++;
            else break;
        }
        return similarity;
    }

    private int calculateRightSimilarity(String word1, String word2) {
        int similarity = 0;
        int length1 = word1.length();
        int length2 = word2.length();

        for (int i = 1; i <= Math.min(length1, length2); i++) {
            if (word1.charAt(length1 - i) == word2.charAt(length2 - i)) similarity++;
            else break;
        }
        return similarity;
    }

    private String findLeastSimilarWord(String word, ArrayList<String> candidates) {
        String leastSimilar = candidates.get(0);
        double minSimilarity = getSimilarity(word, leastSimilar);

        for (String candidate: candidates) {
            double similarity = getSimilarity(word, candidate);
            if (similarity < minSimilarity) {
                minSimilarity = similarity;
                leastSimilar = candidate;
            }
        }
        return leastSimilar;
    }

    // getters for testing

    public void setDictionaryWords(List<String> words) {
        this.dictionaryWords = new ArrayList<>(words);
    }

    public ArrayList<String> getDictionaryWords() {
        return dictionaryWords;
    }

    public double getCommonCharacterPercentage(String word1, String word2) {
        return calculateCommonCharacterPercentage(word1, word2);
    }

    public String getLeastSimilarWord(String word, ArrayList<String> candidates) {
        return findLeastSimilarWord(word, candidates);
    }
}
