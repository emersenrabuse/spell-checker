import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

public class SpellChecker {

    private WordRecommender wordRecommender;

    public SpellChecker() {

    }

    public void start() {
        Scanner scnr = new Scanner(System.in);

        String dictionaryFileName = getValidFileName(Util.DICTIONARY_PROMPT, Util.FILE_OPENING_ERROR, scnr);
        System.out.printf(Util.DICTIONARY_SUCCESS_NOTIFICATION, dictionaryFileName);

        wordRecommender = new WordRecommender(dictionaryFileName);

        String spellCheckFileName = getValidFileName(Util.FILENAME_PROMPT, Util.FILE_OPENING_ERROR, scnr);
        System.out.printf(Util.FILE_SUCCESS_NOTIFICATION, spellCheckFileName, spellCheckFileName.replace(".txt", "_chk.txt"));

        spellCheckFile(spellCheckFileName);
    }

    private String getValidFileName(String prompt, String errorMessage, Scanner scnr) {
        while (true) {
            System.out.printf(prompt);
            String fileName = scnr.nextLine().trim();
            File file = new File("src/" + fileName);

            if (file.exists()) {
                return file.getPath();
            }
            System.out.println(errorMessage);
        }
    }

    private void spellCheckFile(String fileName) {
        try (Scanner fileScanner = new Scanner(new File(fileName));
             PrintWriter outputWriter = new PrintWriter(fileName.replace(".txt", "_chk.txt"))) {

            while (fileScanner.hasNext()) {
                String word = fileScanner.next();
                ArrayList<String> suggestions = wordRecommender.getWordSuggestions(word, 2, 0.5, 4);  // Adjusted threshold to 0.6

                if (!suggestions.contains(word)) {
                    System.out.printf(Util.MISSPELL_NOTIFICATION, word);

                    if (suggestions.isEmpty()) {
                        // No suggestions available
                        System.out.printf(Util.NO_SUGGESTIONS);
                        char choice = getReplacementChoice(false);
                        if (choice == 'a') {
                            outputWriter.print(word + " ");
                        } else if (choice == 't') {
                            outputWriter.print(getManualReplacement() + " ");
                        }
                    } else {
                        // Suggestions available
                        System.out.println(Util.FOLLOWING_SUGGESTIONS);
                        for (int i = 0; i < suggestions.size(); i++) {
                            System.out.printf(Util.SUGGESTION_ENTRY, i + 1, suggestions.get(i));
                        }
                        char choice = getReplacementChoice(true);
                        if (choice == 'r') {
                            int selectedIndex = getUserSuggestionChoice(suggestions.size());
                            outputWriter.print(suggestions.get(selectedIndex) + " ");
                        } else if (choice == 'a') {
                            outputWriter.print(word + " ");
                        } else if (choice == 't') {
                            outputWriter.print(getManualReplacement() + " ");
                        }
                    }
                } else {
                    outputWriter.print(word + " ");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        }
    }

    private char getReplacementChoice(boolean hasSuggestions) {
        Scanner s = new Scanner(System.in);
        while (true) {
            if (hasSuggestions) {
                System.out.print(Util.THREE_OPTION_PROMPT);
            } else {
                System.out.print(Util.TWO_OPTION_PROMPT);
            }

            char choice = s.next().charAt(0);
            if (hasSuggestions && (choice == 'r' || choice == 'a' || choice == 't')) {
                return choice;
            } else if (!hasSuggestions && (choice == 'a' || choice == 't')) {
                return choice;
            } else {
                System.out.printf(Util.INVALID_RESPONSE);
            }
        }
    }

    private int getUserSuggestionChoice(int numSuggestions) {
        Scanner s = new Scanner(System.in);
        while (true) {
            System.out.printf(Util.AUTOMATIC_REPLACEMENT_PROMPT);
            int choice = s.nextInt();
            if (choice>= 1 && choice <= numSuggestions) {
                return choice - 1;
            } else {
                System.out.printf(Util.INVALID_RESPONSE);
            }
        }
    }

    private String getManualReplacement(){
        Scanner s = new Scanner(System.in);
        System.out.printf(Util.MANUAL_REPLACEMENT_PROMPT);
        return s.nextLine();
    }
}
