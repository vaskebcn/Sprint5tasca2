package cat.itacademy.milos.diceGame.mongo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {
    private int dice1;
    private int dice2;
    private int sum;
    private String result;

}
