package viselica.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class GameSession {
    private final int maxErrors = 6;
    private String id;
    private String secretWord;
    private char[] currentState;
    private Set<Character> guessedLetters = new HashSet<>();
    private Set<Character> incorrectGuesses = new HashSet<>();
    private GameStatus status = GameStatus.IN_PROGRESS;

    // Создаем новую игровую сессию с заданным словом
    public GameSession(String secretWord) {
        this.id = UUID.randomUUID().toString();
        this.secretWord = secretWord.toLowerCase();
        this.currentState = new char[secretWord.length()];

        // Инициализируем текущее состояние символами подчеркивания
        for (int i = 0; i < currentState.length; i++) {
            currentState[i] = '_';
        }
    }

    // Делаем предположение о букве и обновляем состояние
    public GameStatus makeGuess(char letter) {
        letter = Character.toLowerCase(letter);

        // Проверка валидности ввода (только буквы кириллицы)
        if (!isValidInput(letter)) {
            return status; // Невалидный ввод не влияет на статус игры
        }

        // Проверка, была ли уже введена буква
        if (guessedLetters.contains(letter)) {
            return status; // Повторный ввод не считается ошибкой
        }

        guessedLetters.add(letter);

        boolean letterFound = false;
        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == letter) {
                currentState[i] = letter;
                letterFound = true;
            }
        }

        if (!letterFound) {
            incorrectGuesses.add(letter);
        }

        // Проверка на выигрыш
        boolean allLettersGuessed = true;
        for (int i = 0; i < secretWord.length(); i++) {
            if (currentState[i] == '_') {
                allLettersGuessed = false;
                break;
            }
        }

        if (allLettersGuessed) {
            status = GameStatus.WIN;
        } else if (incorrectGuesses.size() >= maxErrors) {
            status = GameStatus.LOSS;
        }

        return status;
    }

    private boolean isValidInput(char letter) {
        // Диапазон букв русского алфавита в Unicode
        return letter >= 'а' && letter <= 'я' || letter == 'ё';
    }

    public String getCurrentStateAsString() {
        return String.valueOf(currentState);
    }

    public int getErrorCount() {
        return incorrectGuesses.size();
    }

    public enum GameStatus {
        IN_PROGRESS, WIN, LOSS
    }
} 