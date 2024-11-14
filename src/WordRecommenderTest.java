import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordRecommenderTest {

    private WordRecommender wordRecommender;

    @BeforeEach
    public void setUp() {
        wordRecommender = new WordRecommender("mockDictionary.txt");
        List<String> mockDictionary = Arrays.asList("hello", "hell", "help", "hero", "halo");
        wordRecommender.setDictionaryWords(mockDictionary);
    }

    @Test
    public void testDictionaryLoading() {
        assertEquals(5, wordRecommender.getDictionaryWords().size());
        assertTrue(wordRecommender.getDictionaryWords().contains("hello"));
    }

    @Test
    public void testGetWordSuggestions() {
        String word = "hell";
        ArrayList<String> suggestions = wordRecommender.getWordSuggestions(word, 1, 0.5, 3);

        assertTrue(suggestions.contains("hello"));
        assertTrue(suggestions.contains("help"));
        assertTrue(suggestions.size() <= 3);
    }


    @Test
    public void testGetWordSuggestions_NoSuggestions() {
        String word = "unknownword";
        int tolerance = 2;
        double commonPercent = 0.5;
        int topN = 4;

        ArrayList<String> suggestions = wordRecommender.getWordSuggestions(word, tolerance, commonPercent, topN);
        assertTrue(suggestions.isEmpty(), "No suggestions should be found for 'unknownword'");
    }

    @Test
    public void testCalculateCommonCharacterPercentage() {
        String word1 = "hero";
        String word2 = "halo";

        double commonPercentage = wordRecommender.getCommonCharacterPercentage(word1, word2);
        assertTrue(commonPercentage > 0);
    }

    @Test
    public void testGetSimilarity() {
        String word1 = "hero";
        String word2 = "halo";

        double similarity = wordRecommender.getSimilarity(word1, word2);
        assertTrue(similarity > 0);
    }


    @Test
    public void testGetSimilarity_DifferentWords() {
        String word1 = "hello";
        String word2 = "world";

        double similarity = wordRecommender.getSimilarity(word1, word2);
        assertTrue(similarity < 1.0, "Similarity between different words should be less than 1.0");
    }

    @Test
    public void testFindLeastSimilarWord() {
        String word = "hello";
        ArrayList<String> candidates = new ArrayList<>();
        candidates.add("hell");
        candidates.add("helo");
        candidates.add("world");

        String leastSimilarWord = wordRecommender.getLeastSimilarWord(word, candidates);
        assertEquals("world", leastSimilarWord, "Least similar word should be 'world'");
    }

    @Test
    public void testGetWordSuggestions_TopNLimit() {
        String word = "hello";
        int tolerance = 2;
        double commonPercent = 0.5;
        int topN = 1;

        ArrayList<String> suggestions = wordRecommender.getWordSuggestions(word, tolerance, commonPercent, topN);
        assertTrue(suggestions.size() <= topN, "Number of suggestions should not exceed 'topN'");
    }

    @Test
    public void testLoadDictionary_FileNotFound() {
        WordRecommender invalidRecommender = new WordRecommender("invalidFile.txt");
    }
}

