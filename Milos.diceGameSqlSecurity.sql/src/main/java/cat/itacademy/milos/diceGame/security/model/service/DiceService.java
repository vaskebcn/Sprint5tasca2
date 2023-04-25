package cat.itacademy.milos.diceGame.security.model.service;

import cat.itacademy.milos.diceGame.security.model.domain.Player;
import cat.itacademy.milos.diceGame.security.model.dto.GameDto;
import cat.itacademy.milos.diceGame.security.model.dto.PlayerDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface DiceService {

    PlayerDto addPlayer(PlayerDto playerDto);
    PlayerDto addPlayerNoName();
    GameDto addGame(PlayerDto playerDto);
    PlayerDto searchPlayerById(int id);
    GameDto searchGameById(int id);
    int deleteAllGamesFromPlayer(int id);
    boolean checkName(String name);
    PlayerDto searchByEmail(String email);
    String getPlayerEmailByRequest(HttpServletRequest request);
    PlayerDto updatePlayer(String email, String name);
    boolean existsByPlayer(Player player);
    List<PlayerDto> allPlayerAndPercentage();
    List<GameDto> allGamesFromPlayer(int id);
    float globalPercentage();
    PlayerDto allGamesFromPlayerPercentage(int id);
    PlayerDto getTheWorst();
    PlayerDto getTheBest();


}
