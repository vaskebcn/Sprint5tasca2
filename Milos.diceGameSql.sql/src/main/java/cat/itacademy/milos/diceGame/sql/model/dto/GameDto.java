package cat.itacademy.milos.diceGame.sql.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {
    private int game_id;
    private int dice1;
    private int dice2;
    private int sum;
    private String result;
    private PlayerDto playerDto;

}
