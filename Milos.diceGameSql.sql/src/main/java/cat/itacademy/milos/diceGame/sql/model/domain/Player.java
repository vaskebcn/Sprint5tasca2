package cat.itacademy.milos.diceGame.sql.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int player_id;
    @Column
    private String name;
    @Column
    @CreationTimestamp //ALTER TABLE players MODIFY registration_date DATETIME(0); query para quitar decimales
    private Date registrationDate;

    @OneToMany
    private List<Game> games;

    public Player(String name){
        this.name = name;
    }

}
