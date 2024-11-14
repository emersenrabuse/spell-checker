import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

public class SpellChecker {

    private WordRecommender wordRecommender;

    public SpellChecker() {
        // use constructor later if needed
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
                ArrayList<String> suggestions = wordRecommender.getWordSuggestions(word, 2, 0.61, 5);  // Adjusted threshold to 0.6

                if (!suggestions.contains(word)) {
                    System.out.printf(Util.MISSPELL_NOTIFICATION, word);
                    System.out.println(Util.FOLLOWING_SUGGESTIONS);

                    for (int i = 0; i < suggestions.size(); i++) {
                        System.out.printf(Util.SUGGESTION_ENTRY, i + 1, suggestions.get(i));
                    }
                    char choice = getReplacementChoice();
                    if (choice == 'r') {
                        outputWriter.print(suggestions.get(0) + " ");
                    } else if (choice == 'a') {
                        outputWriter.print(word + " ");
                    } else if (choice == 't') {
                        outputWriter.print(getManualReplacement() + " ");
                    }
                } else {
                    outputWriter.print(word + " ");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        }
    }


    private char getReplacementChoice() {
        Scanner s = new Scanner(System.in);
        while (true) {
            System.out.print(Util.THREE_OPTION_PROMPT);
            char choice = s.next().charAt(0);
            if (choice == 'r' || choice == 'a' || choice == 't') {
                return choice;
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
