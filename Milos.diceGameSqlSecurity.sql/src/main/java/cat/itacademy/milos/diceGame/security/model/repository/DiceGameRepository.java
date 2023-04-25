package cat.itacademy.milos.diceGame.security.model.repository;

import cat.itacademy.milos.diceGame.security.model.domain.Game;
import cat.itacademy.milos.diceGame.security.model.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiceGameRepository extends JpaRepository<Game, Integer> {
    boolean existsByPlayer(Player player);
//    Game findByGamePlayer_ids(int player_ids);

}
