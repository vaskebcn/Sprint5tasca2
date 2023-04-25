package cat.itacademy.milos.diceGame.mongo.model.service;

import cat.itacademy.milos.diceGame.mongo.model.domain.Game;
import cat.itacademy.milos.diceGame.mongo.model.domain.Player;
import cat.itacademy.milos.diceGame.mongo.model.dto.GameDto;
import cat.itacademy.milos.diceGame.mongo.model.dto.PlayerDto;
import cat.itacademy.milos.diceGame.mongo.model.repository.DiceGameRepository;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiceServiceImpl implements DiceService{

    //region autowired

    @Autowired
    DiceGameRepository diceGameRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    //endregion------------------------------------------------------------------


    //region saveMethods
    @Override
    public PlayerDto addPlayer(PlayerDto playerDto) {
        if(!checkName(playerDto.getName()) && playerDto.getName()!=null){
            Player saved = diceGameRepository.save(convertFromDtoToPlayer(playerDto));
            return convertFromPlayerToDto(saved);
        }else if(checkName(playerDto.getName())){
            throw new RuntimeException("name already taken");
        }else{
            throw new RuntimeException("problem creating player");
        }
    }

    public PlayerDto addPlayerNoName(){
        Player saved = diceGameRepository.save(new Player("Anonymous"));
        return convertFromPlayerToDto(saved);
    }

    public void updatePlayerName(ObjectId playerId, String newName) {
        Query query = new Query(Criteria.where("_id").is(playerId));
        Update update = new Update().set("name", newName);
        mongoTemplate.updateFirst(query, update, Player.class);
    }

    @Override
    public GameDto addGame(Player player) {
        Game game = doGame();
        List<Game>games = player.getGames();
        games.add(game);
        Query query = new Query(Criteria.where("_id").is(player.getId()));
        Update update = new Update().set("games", games);
        mongoTemplate.updateFirst(query, update, Player.class);
        GameDto gameDto = convertFromGameToDto(game);
        return gameDto;
    }


    //endregion-------------------------------------------------------------------


    //region findMethods
//    @Override
    public Player searchPlayerById(String id) {
        Player player = diceGameRepository.findAll().stream().filter(s->s.getObjectIdAsString().equals(id)).findFirst().get();
        if(player==null){
            throw new RuntimeException("searchPlayerById failed, player not found");
        }
        return player;
    }

    public boolean checkName(String name){
        boolean checked = diceGameRepository.existsByName(name);
        return checked;
    }

    //endregion----------------------------------------------------


    //region getScoreMethods

    public List<PlayerDto> allPlayerAndPercentage(){
        List<Player>players = diceGameRepository.findAll().stream().collect(Collectors.toList());
        List<PlayerDto>playersDto = new ArrayList<>();
        for (Player player : players) {
            ObjectId id = player.getId();
            float perC= onePlayerPercentage(id);
            PlayerDto playerDto = convertFromPlayerToDto(player);
            if(!Float.isNaN(perC)) {  //set percentage only to the players that played
                playerDto.setPercentage(perC);
            }

            playersDto.add(playerDto);
        }
        return playersDto;
    }

    public float globalPercentage(){

        List<Player>players = diceGameRepository.findAll().stream().collect(Collectors.toList());

        long countWins = 0;
        long totalCountWins = 0;
        long countAll = 0;
        long totalCountAll = 0;

        for (Player player:players) {
            countWins = player.getGames().stream().filter(game -> game.getResult().equals("Winner")).count();
            totalCountWins+=countWins;
        }

        for (Player player:players) {
            countAll = player.getGames().stream().count();
            totalCountAll+=countAll;
        }

//        long countWins = diceGameRepository.findAll().stream()
//                .map(player->player.getGames().stream().parallel().filter(g->g.getResult().equals("Winner"))).count();
//        long countAll = diceGameRepository.findAll().stream()
//                .map(player -> player.getGames().stream()).count();

        return ((float)totalCountWins/totalCountAll)*100;
    }

    public List<GameDto> allGamesFromPlayer(String id){

        Player player = diceGameRepository.findAll().stream()
                .filter(s->s.getObjectIdAsString().equals(id)).findFirst().get();

        List<Game> games = player.getGames().stream().collect(Collectors.toList());

        return games.stream().map(this::convertFromGameToDto).collect(Collectors.toList());
    }

    public PlayerDto allGamesFromPlayerPercentage(String id){
        Player player = diceGameRepository.findAll().stream()
                .filter(s->s.getObjectIdAsString().equals(id)).findFirst().get();

        PlayerDto playerDto = convertFromPlayerToDto(player);

        float perC= onePlayerPercentage(player.getId());
        if(!Float.isNaN(perC)) {
            playerDto.setPercentage(perC);
        }
        return playerDto;
    }

//
//    public PlayerDto getTheWorst(){
//        List<PlayerDto> players = allPlayerAndPercentage();
//        PlayerDto playerDto = players.stream().filter(player->player.getName()!="Anonymous")
//                .min(Comparator.comparingDouble(PlayerDto::getPercentage)).get();
//        return playerDto;
//    }
//
//
//    public PlayerDto getTheBest(){
//        List<PlayerDto> players = allPlayerAndPercentage();
//        PlayerDto playerDto = players.stream().max(Comparator.comparingDouble(PlayerDto::getPercentage)).get();
//        return playerDto;
//    }
//

    public float  onePlayerPercentage(Object id){

        Player playerOne = diceGameRepository.findAll().stream().filter(player -> player.getId().equals(id)).findFirst().get();

        long countWins = playerOne.getGames().stream()
                .filter(s->s.getResult().equals("Winner")).count();

        long countAll = playerOne.getGames().stream().count();

        return ((float)countWins/countAll)*100;
    }

    //endregion


    //region deleteMethods

    public int deleteAllGamesFromPlayer(String id){
        Player player = searchPlayerById(id);
        if(player==null){
            return -1;
        }

        List<Game>games = player.getGames();
        games.clear();

        Query query = new Query(Criteria.where("_id").is(player.getId()));
        Update update = new Update().set("games", games);
        mongoTemplate.updateFirst(query, update, Player.class);

        return 0;
    }

    //endregion----------------------------------------------------------


    //region conversionRegion
    PlayerDto convertFromPlayerToDto(Player player){
        PlayerDto playerDto = modelMapper.map(player, PlayerDto.class);
        return playerDto;
    }

    Player convertFromDtoToPlayer(PlayerDto playerDto){
        Player player = modelMapper.map(playerDto, Player.class);
        return player;
    }

    GameDto convertFromGameToDto(Game game){
        GameDto gameDto = modelMapper.map(game, GameDto.class);
        return gameDto;
    }

    Game convertDtoToGame(GameDto gameDto){
        Game game = modelMapper.map(gameDto, Game.class);
        return game;
    }
    //endregion------------------------------------------------


    //region GameRollMethods
    private Game doGame(){
        int dice1 = diceGenerator();
        int dice2 = diceGenerator();
        int sum = dice1+dice2;
        String result;
        if((dice1+dice2)==7){
            result = "Winner";
        }else{
            result = "Loser";
        }
        Game game = new Game();
        game.setDice1(dice1);
        game.setDice2(dice2);
        game.setSum(game.getDice1()+game.getDice2());
        game.setResult(result);
        return game;
    }

    private int diceGenerator(){
        Random random = new Random();
        int dice = random.nextInt(6)+1;
        return dice;
    }
    //endregion


}
