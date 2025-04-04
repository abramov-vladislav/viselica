# Игра "Виселица" (Spring Web)

Простая веб-версия игры "Виселица" на Java с использованием Spring Boot и Thymeleaf.

## Техническое задание

### Цель проекта

Разработать веб-приложение для игры "Виселица" с использованием Spring Web.

### Функциональные требования

1. Приложение должно загадывать случайное слово из словаря
2. Пользователь должен иметь возможность угадывать буквы по одной
3. После каждой введенной буквы должно отображаться текущее состояние слова и виселицы
4. Неправильные буквы должны увеличивать счетчик ошибок (максимум 6 ошибок)
5. По завершении игры должен отображаться результат (победа или поражение)
6. Пользователь должен иметь возможность начать новую игру

## Описание

"Виселица" - это игра, в которой компьютер загадывает слово, а игрок должен его угадать, называя по одной букве. За
каждую неправильную букву рисуется часть виселицы. Если виселица нарисована полностью до того, как слово угадано - игрок
проигрывает.

## Технологии

* Java 23
* Spring Boot 3.2.3
* Thymeleaf
* Lombok 1.18.38
* Maven

## Как запустить

### Запуск с помощью Maven

```bash
mvn spring-boot:run
```

После запуска откройте в браузере адрес http://localhost:8080

## Правила игры

- Компьютер загадывает слово, игрок должен его угадать
- Игрок вводит по одной букве
- За каждую неверную букву рисуется часть виселицы
- У игрока есть 6 попыток до проигрыша
- Повторный ввод буквы не считается ошибкой

## Структура проекта

### Основные компоненты:

- `ViselicaApplication.java` - точка входа приложения
- `GameController.java` - контроллер для обработки HTTP запросов
- `GameService.java` - сервис с игровой логикой
- `game.html` - Thymeleaf шаблон страницы игры
- `words.txt` - словарь слов для игры

## Использование Lombok

В проекте используются аннотации Lombok:

- `@Getter` - для автоматической генерации геттеров
- `@Slf4j` - для генерации логгера 