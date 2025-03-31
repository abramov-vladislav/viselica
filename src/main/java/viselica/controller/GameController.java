package viselica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import viselica.model.GameSession;
import viselica.service.GameService;

import javax.servlet.http.HttpSession;

@Controller
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // Главная страница
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // Начать новую игру
    @GetMapping("/new-game")
    public String newGame(HttpSession session, RedirectAttributes redirectAttributes) {
        // Создаем новую игру
        GameSession gameSession = gameService.createNewGame();

        // Сохраняем ID игры в сессии браузера
        session.setAttribute("gameId", gameSession.getId());

        return "redirect:/play";
    }

    // Страница игры
    @GetMapping("/play")
    public String play(HttpSession session, Model model) {
        String gameId = (String) session.getAttribute("gameId");

        // Если нет активной игры, перенаправляем на начальную страницу
        if (gameId == null) {
            return "redirect:/";
        }

        GameSession gameSession = gameService.getGameSession(gameId);
        if (gameSession == null) {
            // Если игровая сессия не найдена, начинаем новую
            return "redirect:/new-game";
        }

        // Передаем данные в шаблон
        model.addAttribute("gameId", gameSession.getId());
        model.addAttribute("currentState", gameService.formatWord(gameSession.getCurrentStateAsString()));
        model.addAttribute("errorCount", gameSession.getErrorCount());
        model.addAttribute("maxErrors", gameSession.getMaxErrors());
        model.addAttribute("hangmanAscii", gameService.getHangmanAscii(gameSession.getErrorCount()));
        model.addAttribute("status", gameSession.getStatus());

        // Если игра завершена
        if (gameSession.getStatus() != GameSession.GameStatus.IN_PROGRESS) {
            model.addAttribute("secretWord", gameSession.getSecretWord());
        }

        return "game";
    }

    // Сделать ход
    @PostMapping("/guess")
    public String makeGuess(@RequestParam("letter") String letterInput,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        String gameId = (String) session.getAttribute("gameId");
        if (gameId == null) {
            return "redirect:/";
        }

        // Проверяем ввод
        if (letterInput == null || letterInput.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Пожалуйста, введите букву");
            return "redirect:/play";
        }

        char letter = letterInput.toLowerCase().charAt(0);

        // Делаем ход
        gameService.makeGuess(gameId, letter);

        return "redirect:/play";
    }
} 