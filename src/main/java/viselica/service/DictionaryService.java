package viselica.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class DictionaryService {
    private static final int MIN_WORD_LENGTH = 4;
    private final List<String> words = new ArrayList<>();
    private final Random random = new Random();

    public DictionaryService() {
        loadWords();
    }

    private void loadWords() {
        try {
            // Загружаем словарь из файла ресурсов
            InputStream inputStream = getClass().getResourceAsStream("/words.txt");
            if (inputStream == null) {
                // Если файл не найден, добавим несколько слов по умолчанию
                log.warn("Файл словаря не найден, используются слова по умолчанию");
                addDefaultWords();
                return;
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim().toLowerCase();
                    if (line.length() >= MIN_WORD_LENGTH) {
                        words.add(line);
                    }
                }
            }

            if (words.isEmpty()) {
                log.warn("Словарь пуст, используются слова по умолчанию");
                addDefaultWords();
            } else {
                log.info("Загружено {} слов из словаря", words.size());
            }
        } catch (IOException e) {
            log.error("Ошибка при загрузке словаря: {}", e.getMessage());
            addDefaultWords();
        }
    }

    private void addDefaultWords() {
        words.add("программа");
        words.add("компьютер");
        words.add("разработка");
        words.add("алгоритм");
        words.add("тестирование");
        words.add("интерфейс");
        words.add("переменная");
        words.add("объект");
        words.add("функция");
        words.add("класс");
    }

    public String getRandomWord() {
        if (words.isEmpty()) {
            return "программа"; // Слово по умолчанию
        }
        return words.get(random.nextInt(words.size()));
    }
} 