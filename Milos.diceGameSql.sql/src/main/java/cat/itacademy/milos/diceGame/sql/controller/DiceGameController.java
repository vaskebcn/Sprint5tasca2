package cat.itacademy.milos.diceGame.sql.controller;

import cat.itacademy.milos.diceGame.sql.model.dto.GameDto;
import cat.itacademy.milos.diceGame.sql.model.dto.Message;
import cat.itacademy.milos.diceGame.sql.model.dto.PlayerDto;
import cat.itacademy.milos.diceGame.sql.model.service.DiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class DiceGameController {

    @Autowired
    DiceService diceService;

    @PostMapping("/add")
    public ResponseEntity<PlayerDto> addPlayer(@RequestBody(required = false) PlayerDto playerDto) {
            if (playerDto != null) {
                return new ResponseEntity<>(diceService.addPlayer(playerDto), HttpStatus.CREATED);
            }else if (playerDto == null) {
                return new ResponseEntity<>(diceService.addPlayerNoName(), HttpStatus.CREATED);
            }else{
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?>editPlayer(@PathVariable(name="id")int id, @RequestBody PlayerDto playerDto){
        try{
            PlayerDto playerDtoToUpdate = diceService.searchPlayerById(id);
            playerDtoToUpdate.setName(playerDto.getName());
            return new ResponseEntity<>(diceService.addPlayer(playerDtoToUpdate), HttpStatus.OK);
        }catch(Exception e){
            Message message = new Message();
            message.setTextMessage("Problem while updating player");
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/games")
    public ResponseEntity<GameDto>playGame(@PathVariable(name="id")int id){
        PlayerDto playerDto = diceService.searchPlayerById(id);
        return new ResponseEntity<>(diceService.addGame(playerDto), HttpStatus.OK);
    }


    @DeleteMapping("/{id}/games")
    public ResponseEntity<?>deleteAllGamesPerPlayer(@PathVariable(name="id")int id){

        int backResponse = diceService.deleteAllGamesFromPlayer(id);

        if(backResponse==0){
            Message message = new Message();
            message.setTextMessage("id = "+id+" deleted, with existsById method");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }else if(backResponse ==-1){
            Message message = new Message();
            message.setTextMessage("couldn't find id = "+id+", with existsById method");
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }else if(backResponse==-2){
            Message message = new Message();
            message.setTextMessage("couldn't find games for player with id = "+id+" in repository");
            return new ResponseEntity<>(message, HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/listScore")
    public ResponseEntity<?>listAllScores(){
        List<PlayerDto> players = diceService.allPlayerAndPercentage();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }


    @GetMapping("/listPercentage")
    public ResponseEntity<?>listAverage(){
        float result = diceService.globalPercentage();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/listPlayerScore/{id}")
    public ResponseEntity<List<GameDto>>listPlayerScore(@PathVariable(name="id")int id){
        List<GameDto>games = diceService.allGamesFromPlayer(id);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }


    @GetMapping("/listPlayerPercentage/{id}")
    public ResponseEntity<PlayerDto>listPlayerPercentage(@PathVariable(name="id")int id){
        PlayerDto playerDto = diceService.allGamesFromPlayerPercentage(id);
        return new ResponseEntity<>(playerDto, HttpStatus.OK);
    }


    @GetMapping("/ranking/loser")
    public ResponseEntity<PlayerDto>getLoser(){
        return new ResponseEntity<>(diceService.getTheWorst(), HttpStatus.OK);
    }


    @GetMapping("/ranking/winner")
    public ResponseEntity<?>getWinner(){
        return new ResponseEntity<>(diceService.getTheBest(), HttpStatus.OK);
    }

}
