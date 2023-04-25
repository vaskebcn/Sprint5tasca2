package cat.itacademy.milos.diceGame.security.controller;

import cat.itacademy.milos.diceGame.security.model.dto.GameDto;
import cat.itacademy.milos.diceGame.security.model.dto.Message;
import cat.itacademy.milos.diceGame.security.model.dto.PlayerDto;
import cat.itacademy.milos.diceGame.security.model.service.DiceService;
import jakarta.servlet.http.HttpServletRequest;
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

//    @PostMapping("/add")
//    public ResponseEntity<PlayerDto> addPlayer(@RequestBody(required = false) PlayerDto playerDto) {
//            if (playerDto != null) {
//                return new ResponseEntity<>(diceService.addPlayer(playerDto), HttpStatus.CREATED);
//            }else if (playerDto == null) {
//                return new ResponseEntity<>(diceService.addPlayerNoName(), HttpStatus.CREATED);
//            }else{
//                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//            }
//    }

    @PutMapping("/update")
    public ResponseEntity<?>editPlayer(@RequestBody PlayerDto playerDto, HttpServletRequest request){
       String userEmail = diceService.getPlayerEmailByRequest(request);
        try{
            String name = playerDto.getName();
            return new ResponseEntity<>(diceService.updatePlayer(userEmail, name), HttpStatus.OK);
        }catch(Exception e){
            Message message = new Message();
            message.setTextMessage("Problem while updating player");
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/games")
    public ResponseEntity<?>playGame(HttpServletRequest request){
        try {
            String userEmail = diceService.getPlayerEmailByRequest(request);
            int id = diceService.searchByEmail(userEmail).getPlayer_id();
            PlayerDto playerDto = diceService.searchPlayerById(id);
            return new ResponseEntity<>(diceService.addGame(playerDto), HttpStatus.OK);
        }catch (Exception e){
            Message message = new Message();
            message.setTextMessage("Problem shaking dices");
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @DeleteMapping("/games")
    public ResponseEntity<?>deleteAllGamesPerPlayer(HttpServletRequest request){

        String userEmail = diceService.getPlayerEmailByRequest(request);
        int id = diceService.searchByEmail(userEmail).getPlayer_id();

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
        try {
            List<PlayerDto> players = diceService.allPlayerAndPercentage();
            return new ResponseEntity<>(players, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/listPercentage")
    public ResponseEntity<?>listAverage(){
        try {
            float result = diceService.globalPercentage();
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/listPlayerScore")
    public ResponseEntity<List<GameDto>>listPlayerScore(HttpServletRequest request){

        String userEmail = diceService.getPlayerEmailByRequest(request);
        int id = diceService.searchByEmail(userEmail).getPlayer_id();
         try {
             List<GameDto> games = diceService.allGamesFromPlayer(id);
             return new ResponseEntity<>(games, HttpStatus.OK);
         }catch(Exception e){
             return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
         }
    }


    @GetMapping("/listPlayerPercentage")
    public ResponseEntity<PlayerDto>listPlayerPercentage(HttpServletRequest request){

        String userEmail = diceService.getPlayerEmailByRequest(request);
        int id = diceService.searchByEmail(userEmail).getPlayer_id();

        try {
            PlayerDto playerDto = diceService.allGamesFromPlayerPercentage(id);
            return new ResponseEntity<>(playerDto, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/ranking/loser")
    public ResponseEntity<PlayerDto>getLoser(){
        try {
            return new ResponseEntity<>(diceService.getTheWorst(), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/ranking/winner")
    public ResponseEntity<?>getWinner(){
        try{
             return new ResponseEntity<>(diceService.getTheBest(), HttpStatus.OK);
        }catch(Exception e){
             return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
