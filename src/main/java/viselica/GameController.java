package viselica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // Главная страница
    @GetMapping("/")
    public String home(Model model) {
        // Возвращаем текущее состояние игры на главную страницу
        addGameAttributesToModel(model);
        return "game";
    }

    // Начать новую игру
    @GetMapping("/new-game")
    public String newGame() {
        gameService.newGame();
        return "redirect:/";
    }

    // Сделать ход
    @PostMapping("/guess")
    public String makeGuess(@RequestParam(value = "letter", required = false) String letterInput) {
        if (letterInput != null && !letterInput.isEmpty()) {
            char letter = letterInput.toLowerCase().charAt(0);
            gameService.makeGuess(letter);
        }
        return "redirect:/";
    }

    // Вспомогательный метод для добавления атрибутов игры в модель
    private void addGameAttributesToModel(Model model) {
        GameService.Game game = gameService.getCurrentGame();

        model.addAttribute("currentState", gameService.formatWord(game.getCurrentStateAsString()));
        model.addAttribute("errorCount", game.getErrorCount());
        model.addAttribute("maxErrors", game.getMaxErrors());
        model.addAttribute("hangmanAscii", gameService.getHangmanAscii(game.getErrorCount()));
        model.addAttribute("gameOver", game.isGameOver());
        model.addAttribute("win", game.isWin());

        if (game.isGameOver()) {
            model.addAttribute("secretWord", game.getSecretWord());
        }
    }
} 