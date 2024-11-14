public class WordRecommenderTest {
    public static void main(String[] args) {
        testDictionaryLoading();
        testSimilarityFunction();
        testGetWordSuggestions();
        testNoSuggestionForNoSimilarity();
    }

    public static void testDictionaryLoading() {
        WordRecommender recommender = new WordRecommender("engDictionary.txt");
        boolean result = recommender.isWordInDictionary("test");
        System.out.println("testDictionaryLoading: " + (result ? "PASSED" : "FAILED"));
    }

    public static void testSimilarityFunction() {
        WordRecommender recommender = new WordRecommender("engDictionary.txt");
        int similarity = recommender.getSimilarity("example", "sample");
        boolean result = similarity > 0;
        System.out.println("testSimilarityFunction: " + (result ? "PASSED" : "FAILED"));
    }

    public static void testGetWordSuggestions() {
        WordRecommender recommender = new WordRecommender("engDictionary.txt");
        ArrayList<String> suggestions = recommender.getWordSuggestions("exampel", 2, 0.5, 5);
        boolean result = !suggestions.isEmpty();
        System.out.println("testGetWordSuggestions: " + (result ? "PASSED" : "FAILED"));
    }

    public static void testNoSuggestionForNoSimilarity() {
        WordRecommender recommender = new WordRecommender("engDictionary.txt");
        ArrayList<String> suggestions = recommender.getWordSuggestions("yubfeufh", 2, 0.5, 5);
        boolean result = suggestions.isEmpty();
        System.out.println("testNoSuggestionForNoSimilarity: " + (result ? "PASSED" : "FAILED"));
    }
}