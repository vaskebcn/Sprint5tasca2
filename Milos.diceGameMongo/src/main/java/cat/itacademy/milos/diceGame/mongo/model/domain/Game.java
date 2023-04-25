package cat.itacademy.milos.diceGame.mongo.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

//@Document(collection="games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    private int dice1;
    private int dice2;
    private int sum;
    private String result;

}
