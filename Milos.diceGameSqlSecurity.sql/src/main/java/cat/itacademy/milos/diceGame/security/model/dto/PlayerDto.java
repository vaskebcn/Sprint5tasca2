package cat.itacademy.milos.diceGame.security.model.dto;


import cat.itacademy.milos.diceGame.security.model.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {
    private int player_id;
    private String name;
    private Date registrationDate;
    private String email;
//    private String password;
    private Role role;
    private double percentage;

    public PlayerDto(String name){
        this.name = name;
    }
}
