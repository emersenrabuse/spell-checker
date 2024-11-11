import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WordRecommenderTest {
    // TODO: Write your tests for WordRecommender in WordRecommenderTest.java

    // test if dictionary works correctly
    @Test
    public void testDictionaryLoading() {
        WordRecommender recommender = new WordRecommender("engDictionary.txt");
        assertTrue(recommender.isWordInDictionary("test"), "The word 'test' should be in the dictionary.");
    }

        // test similarity function
        @Test
        public void testSimilarityFunction() {
            WordRecommender recommender = new WordRecommender("engDictionary.txt");
            double similarity = recommender.calculateSimilarity("example", "sample");
            assertTrue(similarity > 0, "The similarity score should be greater than 0 for similar words.");
        }

        // test getwordsuggestions function
        @Test
        public void testGetWordSuggestions() {
            WordRecommender recommender = new WordRecommender("engDictionary.txt");
            List<String> suggestions = recommender.getWordSuggestions("exampel");
            assertFalse(suggestions.isEmpty(), "There should be suggestions for the misspelled word 'exampel'.");
        }

        // test similarities
        @Test
        public void testSimilarities() {
            WordRecommender recommender = new WordRecommender("engDictionary.txt");
            List<String> similarities = recommender.getSimilarWords("mouse");
            assertTrue(similarities.contains("house"), "The list of similar words should contain 'house'.");
        }

        // test if dictionary contains a known word
        @Test
        public void testDictionaryContainsKnownWord() {
            WordRecommender recommender = new WordRecommender("engDictionary.txt");
            assertTrue(recommender.isWordInDictionary("person"), "The word 'person' should be in the dictionary.");
        }

        // test if no suggestion is given for a word with no similarity
        @Test
        public void testNoSuggestionForNoSimilarity() {
            WordRecommender recommender = new WordRecommender("engDictionary.txt");
            List<String> suggestions = recommender.getWordSuggestions("yubfeufh");
            assertTrue(suggestions.isEmpty(), "There should be no suggestions for the word 'yubfeufh' with no similarity.");
        }

}
