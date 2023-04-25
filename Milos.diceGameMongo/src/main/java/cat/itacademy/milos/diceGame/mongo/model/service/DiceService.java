package cat.itacademy.milos.diceGame.mongo.model.service;

import cat.itacademy.milos.diceGame.mongo.model.domain.Player;
import cat.itacademy.milos.diceGame.mongo.model.dto.GameDto;
import cat.itacademy.milos.diceGame.mongo.model.dto.PlayerDto;
import org.bson.types.ObjectId;

import java.util.List;

public interface DiceService {

    PlayerDto addPlayer(PlayerDto playerDto);
    PlayerDto addPlayerNoName();
    void updatePlayerName(ObjectId playerId, String newName);
    GameDto addGame(Player player);
    Player searchPlayerById(String id);
    int deleteAllGamesFromPlayer(String id);
    boolean checkName(String name);
    List<PlayerDto> allPlayerAndPercentage();
    List<GameDto> allGamesFromPlayer(String id);
    float globalPercentage();
    PlayerDto allGamesFromPlayerPercentage(String id);
//    PlayerDto getTheWorst();
//    PlayerDto getTheBest();

}
