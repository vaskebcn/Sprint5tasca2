package cat.itacademy.milos.diceGame.mongo.model.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection="players")
@Getter
@Setter
@AllArgsConstructor
public class Player {

    @Id
    private ObjectId id;
    private String name;
    @CreatedDate
    private Date registrationDate;
    private List<Game> games;
    private String objectIdAsString;
    public String getObjectIdAsString() {
        return id.toHexString();
    }
    public void setObjectIdAsString(String objectIdAsString) {
        this.objectIdAsString = objectIdAsString;
        this.id = new ObjectId(objectIdAsString);
    }

    public Player(String name) {
        this.name = name;
        if(this.games==null){
            this.games = new ArrayList<>();
        }

    }
    public  Player(){
        if(this.games==null){
            this.games = new ArrayList<>();
        }
    }

}
