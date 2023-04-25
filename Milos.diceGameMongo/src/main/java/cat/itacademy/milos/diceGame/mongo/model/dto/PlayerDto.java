package cat.itacademy.milos.diceGame.mongo.model.dto;

import cat.itacademy.milos.diceGame.mongo.model.domain.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PlayerDto {
    private String id;
    private String name;
    private Date registrationDate;
    private List<Game> games;
    private double percentage;

    public PlayerDto(String name) {
        this.name = name;
        this.games = new ArrayList<>();
    }

    public PlayerDto() {
        this.games = new ArrayList<>();
    }
}
