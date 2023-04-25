package cat.itacademy.milos.diceGame.security.model.repository;

import cat.itacademy.milos.diceGame.security.model.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DicePlayerRepository extends JpaRepository<Player, Integer> {
    Optional<Player> findByName(String name);

    boolean existsByName(String name);

    Optional<Player> findByEmail(String email);


}
