package viselica;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
public class GameService {

    private final List<String> words = new ArrayList<>();
    private final Random random = new Random();
    private Game currentGame;
    public GameService() {
        loadWords();
        newGame();
    }

    // Загрузка словаря
    private void loadWords() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/words.txt");
            if (inputStream == null) {
                addDefaultWords();
                return;
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim().toLowerCase();
                    if (line.length() >= 4) {
                        words.add(line);
                    }
                }
            }

            if (words.isEmpty()) {
                addDefaultWords();
            }
        } catch (IOException e) {
            addDefaultWords();
        }
    }

    // Добавление слов по умолчанию
    private void addDefaultWords() {
        words.add("программа");
        words.add("компьютер");
        words.add("разработка");
        words.add("алгоритм");
        words.add("интерфейс");
        words.add("переменная");
        words.add("объект");
        words.add("функция");
        words.add("класс");
    }

    // Получение случайного слова
    private String getRandomWord() {
        if (words.isEmpty()) {
            return "программа";
        }
        return words.get(random.nextInt(words.size()));
    }

    // Создание новой игры
    public void newGame() {
        currentGame = new Game(getRandomWord());
        log.info("Новая игра создана, загадано слово: {}", currentGame.getSecretWord());
    }

    // Получение текущей игры
    public Game getCurrentGame() {
        return currentGame;
    }

    // Сделать ход в игре
    public void makeGuess(char letter) {
        currentGame.makeGuess(letter);
    }

    // Получение ASCII представления виселицы
    public String getHangmanAscii(int errorCount) {
        String[] hangmanStates = {
                // 0 ошибок
                "  +---+\n" +
                        "  |   |\n" +
                        "      |\n" +
                        "      |\n" +
                        "      |\n" +
                        "      |\n" +
                        "=========",

                // 1 ошибка
                "  +---+\n" +
                        "  |   |\n" +
                        "  O   |\n" +
                        "      |\n" +
                        "      |\n" +
                        "      |\n" +
                        "=========",

                // 2 ошибки
                "  +---+\n" +
                        "  |   |\n" +
                        "  O   |\n" +
                        "  |   |\n" +
                        "      |\n" +
                        "      |\n" +
                        "=========",

                // 3 ошибки
                "  +---+\n" +
                        "  |   |\n" +
                        "  O   |\n" +
                        " /|   |\n" +
                        "      |\n" +
                        "      |\n" +
                        "=========",

                // 4 ошибки
                "  +---+\n" +
                        "  |   |\n" +
                        "  O   |\n" +
                        " /|\\  |\n" +
                        "      |\n" +
                        "      |\n" +
                        "=========",

                // 5 ошибок
                "  +---+\n" +
                        "  |   |\n" +
                        "  O   |\n" +
                        " /|\\  |\n" +
                        " /    |\n" +
                        "      |\n" +
                        "=========",

                // 6 ошибок
                "  +---+\n" +
                        "  |   |\n" +
                        "  O   |\n" +
                        " /|\\  |\n" +
                        " / \\  |\n" +
                        "      |\n" +
                        "========="
        };

        if (errorCount < 0) {
            errorCount = 0;
        } else if (errorCount >= hangmanStates.length) {
            errorCount = hangmanStates.length - 1;
        }

        return hangmanStates[errorCount];
    }

    // Форматирование текущего состояния слова
    public String formatWord(String currentState) {
        StringBuilder formatted = new StringBuilder();
        for (char c : currentState.toCharArray()) {
            formatted.append(c).append(" ");
        }
        return formatted.toString().trim();
    }

    // Класс для хранения состояния игры
    @Getter
    public static class Game {
        private static final int MAX_ERRORS = 6;
        private String secretWord;
        private char[] currentState;
        private Set<Character> guessedLetters = new HashSet<>();
        private Set<Character> incorrectGuesses = new HashSet<>();
        private boolean isGameOver = false;
        private boolean isWin = false;

        public Game(String secretWord) {
            this.secretWord = secretWord.toLowerCase();
            this.currentState = new char[secretWord.length()];
            for (int i = 0; i < currentState.length; i++) {
                currentState[i] = '_';
            }
        }

        // Проверка буквы
        public void makeGuess(char letter) {
            if (isGameOver) {
                return;
            }

            letter = Character.toLowerCase(letter);

            // Если буква уже была, ничего не делаем
            if (guessedLetters.contains(letter)) {
                return;
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

            // Проверяем победу
            boolean allLettersGuessed = true;
            for (char c : currentState) {
                if (c == '_') {
                    allLettersGuessed = false;
                    break;
                }
            }

            if (allLettersGuessed) {
                isWin = true;
                isGameOver = true;
            } else if (incorrectGuesses.size() >= MAX_ERRORS) {
                isGameOver = true;
            }
        }

        public String getCurrentStateAsString() {
            return String.valueOf(currentState);
        }

        public int getErrorCount() {
            return incorrectGuesses.size();
        }

        public int getMaxErrors() {
            return MAX_ERRORS;
        }
    }
} 