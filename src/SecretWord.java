import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class SecretWord implements InputValidator {

    private static final int MAX_WRONG_GUESS_ATTEMPT = 5;

    private int wrongGuessAttemptRemaining = MAX_WRONG_GUESS_ATTEMPT;

    private int correctCharacterGuessed = 0;

    private final String word;

    private final Set<Character> guessedCharacters = new LinkedHashSet<>();

    public SecretWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public int getWrongGuessAttemptRemaining() {
        return wrongGuessAttemptRemaining;
    }

    public void makeGuess(Scanner scanner) {
        System.out.print("Please enter a character to make a guess: ");
        char c = ' ';
        boolean isGuessValid = false;

        do {
            try {
                c = Character.toUpperCase(scanner.next(".").charAt(0));
                if (!InputValidator.isEnglishCharacter(c))
                    throw new IllegalArgumentException(
                        "Your last input is invalid. Please enter an English character only: ");

                if(!guessedCharacters.add(c))
                    throw new IllegalArgumentException(
                        "You have already guessed the character. Please enter another character: ");

                isGuessValid = true;
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
        } while (!isGuessValid);

        if (word.toUpperCase().indexOf(c) == -1)
            wrongGuessAttemptRemaining -= 1;
        else {
            for (char ch : word.toUpperCase().toCharArray())
                if (c == ch) correctCharacterGuessed += 1;
        }

        System.out.println();
    }

    public void printSecretWord() {
        for (char c : word.toCharArray()) {
            if (!guessedCharacters.contains(Character.toUpperCase(c)))
                System.out.print("_ ");
            else
                System.out.printf("%s ", c);
        }

        System.out.println();
    }

    public void printGuessedCharacters() {
        System.out.print("Your guessed characters for this secret word: ");
        for (char c : guessedCharacters)
            System.out.printf("%c ", c);
        System.out.println();
    }

    public boolean isEveryCharacterFound() {
        if (guessedCharacters.isEmpty()) return false;

        for (char c : word.toUpperCase().toCharArray()) {
            if (!guessedCharacters.contains(c))
                return false;
        }

        return true;
    }

    public void printCorrectRate() {
        System.out.printf("\t- For the word %s, you have guessed %d out of %d characters correctly. " +
                "Your correct rate is %.0f%%\n",
            word,
            correctCharacterGuessed,
            word.length(),
            (float) correctCharacterGuessed / word.length() * 100);
    }

}
