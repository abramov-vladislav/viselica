package viselica.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import viselica.model.GameSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class GameService {

    private final DictionaryService dictionaryService;

    // Хранилище активных игровых сессий
    private final Map<String, GameSession> activeSessions = new ConcurrentHashMap<>();

    @Autowired
    public GameService(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    // Создать новую игровую сессию
    public GameSession createNewGame() {
        String randomWord = dictionaryService.getRandomWord();
        GameSession gameSession = new GameSession(randomWord);

        // Сохраняем сессию
        activeSessions.put(gameSession.getId(), gameSession);
        log.info("Создана новая игровая сессия с ID: {}", gameSession.getId());

        return gameSession;
    }

    // Получить игровую сессию по ID
    public GameSession getGameSession(String gameId) {
        GameSession session = activeSessions.get(gameId);
        if (session == null) {
            log.warn("Попытка получить несуществующую игровую сессию: {}", gameId);
        }
        return session;
    }

    // Сделать ход в игре
    public GameSession makeGuess(String gameId, char letter) {
        GameSession session = getGameSession(gameId);
        if (session == null) {
            log.error("Невозможно сделать ход: сессия не найдена {}", gameId);
            return null;
        }

        GameSession.GameStatus status = session.makeGuess(letter);
        log.info("Ход в игре {}: буква '{}', статус игры: {}",
                gameId, letter, status);

        return session;
    }

    // Подготовить массив ASCII представления виселицы для текущего состояния
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

    // Форматировать текущее состояние слова для отображения
    public String formatWord(String currentState) {
        StringBuilder formatted = new StringBuilder();
        for (char c : currentState.toCharArray()) {
            formatted.append(c).append(" ");
        }
        return formatted.toString().trim();
    }
} 