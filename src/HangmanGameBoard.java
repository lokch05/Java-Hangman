import java.io.Console;
import java.util.*;

public class HangmanGameBoard implements InputValidator{
    private final Scanner scanner = new Scanner(System.in);
    private final List<SecretWord> secretWords = new ArrayList<>();
    private int wordGuessed = 0;
    private int score = 0;

    private void enterSecretWords() {
        System.out.println(
            "Welcome to Java Hangman.");
        System.out.println("Please select a host and let the host enter some secret words to start the game.");
        System.out.print("Please enter the secret word(s) separated by a whitespace character: ");

        Console console = System.console();
        String s;
        String[] words = {};

        boolean isInputInvalid = false;
        do {
            try {
                if (console != null) { // Program running in an OS Terminal
                    char[] ca = console.readPassword();
                    s = String.valueOf(ca);
                } else  // Program running in an IDE Terminal
                    s = scanner.nextLine();

                words = s.split(" ");
                for (String word : words) {
                    for (char c : word.toCharArray()) {
                        if (!InputValidator.isEnglishCharacter(c))
                            throw new IllegalArgumentException(
                                "Non English character detected in your input! " +
                                    "Please enter the secret word(s) separated by a whitespace character again: ");
                    }
                }

                isInputInvalid = true;
            }
            catch (Exception e) {
                System.out.print(e.getMessage());
            }
        } while (!isInputInvalid);

        Collections.shuffle(Arrays.asList(words));
        for (String word : words)
            this.secretWords.add(new SecretWord(word));

        System.out.println();
    }

    public void startGame() {
        enterSecretWords();
        while (!isGameOver()) {
            printPrompt();
            makeGuess();
        }
        printFinalScore();
    }

    private boolean isGameOver() {
        for (SecretWord sw : secretWords) {
            if (!sw.isEveryCharacterFound() ||
                sw.getWrongGuessAttemptRemaining() <= 0)
                return false;
        }
        return true;
    }

    private void printPrompt() {
        SecretWord secretWord = secretWords.get(wordGuessed);
        String singularOrPlural =
                secretWord.getWrongGuessAttemptRemaining() > 1 ? "attempts" : "attempt";
        System.out.printf(
            "Guess the %s secret word. You have %d wrong guess %s remaining for this word.\n",
            toOrdinal(wordGuessed + 1),
                secretWord.getWrongGuessAttemptRemaining(),
            singularOrPlural);
        secretWord.printSecretWord();
        secretWord.printGuessedCharacters();
    }

    private String toOrdinal(int order) {
        if (order % 10 - 1 == 0)
            return String.format("%dst", order);
        else if (order % 10 - 2 == 0)
            return String.format("%dnd", order);
        else if (order % 10 - 3 == 0)
            return String.format("%drd", order);
        return String.format("%dth", order);
    }

    private void makeGuess() {
        SecretWord secretWord = secretWords.get(wordGuessed);
        secretWord.makeGuess(scanner);
        if (secretWord.isEveryCharacterFound() || secretWord.getWrongGuessAttemptRemaining() <= 0) {
            String message = secretWord.isEveryCharacterFound() ?
                "You have successfully guessed the word!" :
                "You have no guess attempt remaining for this word.";
            System.out.printf("%s The word you were guessing is: %s\n\n", message, secretWord.getWord());
            if (secretWord.isEveryCharacterFound()) score++;
            wordGuessed++;
        }
    }

    private void printFinalScore() {
        if (secretWords.isEmpty()) return;

        System.out.printf(
            "The game is finished! You have guessed %d out of %d words correctly! " +
                "Your correct rate for each word is:\n",
            score,
            secretWords.size());
        for (SecretWord secretWord : secretWords)
            secretWord.printCorrectRate();
    }

}
