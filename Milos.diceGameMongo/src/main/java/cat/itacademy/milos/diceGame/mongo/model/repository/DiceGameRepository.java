package cat.itacademy.milos.diceGame.mongo.model.repository;

import cat.itacademy.milos.diceGame.mongo.model.domain.Player;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DiceGameRepository extends MongoRepository<Player, ObjectId> {
    boolean existsByName(String name);

    @Override
    Optional<Player> findById(ObjectId id);

}
