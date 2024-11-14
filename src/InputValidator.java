public interface InputValidator {
    static boolean isEnglishCharacter(char c) {
        char cu = Character.toUpperCase(c);
        return !((int) cu < 65 || (int) cu > 90);
    }
}
