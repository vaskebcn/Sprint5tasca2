package cat.itacademy.milos.diceGame.sql.model.service;

import cat.itacademy.milos.diceGame.sql.model.domain.Game;
import cat.itacademy.milos.diceGame.sql.model.domain.Player;
import cat.itacademy.milos.diceGame.sql.model.dto.GameDto;
import cat.itacademy.milos.diceGame.sql.model.dto.PlayerDto;

import java.util.List;

public interface DiceService {

    PlayerDto addPlayer(PlayerDto playerDto);
    PlayerDto addPlayerNoName();
    GameDto addGame(PlayerDto playerDto);
    PlayerDto searchPlayerById(int id);
    GameDto searchGameById(int id);
    int deleteAllGamesFromPlayer(int id);
    boolean checkName(String name);
    boolean existsByPlayer(Player player);
    List<PlayerDto> allPlayerAndPercentage();
    List<GameDto> allGamesFromPlayer(int id);
    float globalPercentage();
    PlayerDto allGamesFromPlayerPercentage(int id);
    PlayerDto getTheWorst();
    PlayerDto getTheBest();

}
