package cat.itacademy.milos.diceGame.sql.model.service;

import cat.itacademy.milos.diceGame.sql.model.domain.Game;
import cat.itacademy.milos.diceGame.sql.model.domain.Player;
import cat.itacademy.milos.diceGame.sql.model.dto.GameDto;
import cat.itacademy.milos.diceGame.sql.model.dto.PlayerDto;
import cat.itacademy.milos.diceGame.sql.model.repository.DiceGameRepository;
import cat.itacademy.milos.diceGame.sql.model.repository.DicePlayerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class DiceServiceImpl implements DiceService{

    //region autowired

    @Autowired
    DicePlayerRepository dicePlayerRepository;

    @Autowired
    DiceGameRepository diceGameRepository;

    @Autowired
    ModelMapper modelMapper;

    //endregion------------------------------------------------------------------


    //region saveMethods
    @Override
    public PlayerDto addPlayer(PlayerDto playerDto) {
        if(!checkName(playerDto.getName()) && playerDto.getName()!=null){
            Player saved = dicePlayerRepository.save(convertFromDtoToPlayer(playerDto));
            return convertFromPlayerToDto(saved);
        }else if(playerDto.getName()==null||playerDto.getName().isEmpty()){
            return addPlayerNoName();
        }else if(checkName(playerDto.getName())){
            throw new RuntimeException("name already taken");
        }else{
            throw new RuntimeException("problem creating player");
        }
    }

    public PlayerDto addPlayerNoName(){
        Player saved = dicePlayerRepository.save(new Player("Anonymous"));
        return convertFromPlayerToDto(saved);
    }

    @Override
    public GameDto addGame(PlayerDto playerDto) {
        Game game = doGame();
        game.setPlayer(convertFromDtoToPlayer(playerDto));
        GameDto gameDto = convertFromGameToDto(diceGameRepository.save(game));
        return gameDto;
    }
    //endregion-------------------------------------------------------------------


    //region findMethods
    @Override
    public PlayerDto searchPlayerById(int id) {
        Player player = dicePlayerRepository.findById(id).orElseThrow(()->new RuntimeException("no player found with id "+id));
        return convertFromPlayerToDto(player);
    }

    public GameDto searchGameById(int id) {
        Optional<Game> game = diceGameRepository.findById(id);
        GameDto gameDto = null;
        if(game.isPresent()){
            gameDto = convertFromGameToDto(game.get());
        }
        return gameDto;
    }

    public boolean existsByPlayer(Player player){
        boolean result = diceGameRepository.existsByPlayer(player);
        return result;
    }

    public boolean checkName(String name){
        boolean checked = dicePlayerRepository.existsByName(name);
        return checked;
    }

    //endregion----------------------------------------------------


    //region getScoreMethods
    public List<PlayerDto> allPlayerAndPercentage(){
        List<PlayerDto>players = dicePlayerRepository.findAll().stream().map(this::convertFromPlayerToDto).collect(Collectors.toList());
        for (PlayerDto player : players) {
            int id = player.getPlayer_id();
            float perC= onePlayerPercentage(id);
            if(!Float.isNaN(perC)) {  //set percentage only to the players that played
                player.setPercentage(perC);
            }
        }
        return players;
    }

    public float globalPercentage(){
        long countWins = diceGameRepository.findAll().stream().filter(game->game.getResult().equals("Winner")).count();
        long countAll = diceGameRepository.findAll().stream().count();
        return ((float)countWins/countAll)*100;
    }


    public List<GameDto> allGamesFromPlayer(int id){

        List<GameDto>games = diceGameRepository.findAll().stream()
                .filter(game1->game1.getPlayer().getPlayer_id()==id)
                .map(this::convertFromGameToDto).collect(Collectors.toList());

        return games;
    }


    public PlayerDto allGamesFromPlayerPercentage(int id){
        PlayerDto playerDto = convertFromPlayerToDto(dicePlayerRepository.findById(id).get());
        float perC= onePlayerPercentage(id);
        if(!Float.isNaN(perC)) {
            playerDto.setPercentage(perC);
        }
        return playerDto;
    }


    public PlayerDto getTheWorst(){
        List<PlayerDto> players = allPlayerAndPercentage();
        PlayerDto playerDto = players.stream().filter(player->player.getName()!="Anonymous")
                .min(Comparator.comparingDouble(PlayerDto::getPercentage)).get();
        return playerDto;
    }


    public PlayerDto getTheBest(){
        List<PlayerDto> players = allPlayerAndPercentage();
        PlayerDto playerDto = players.stream().max(Comparator.comparingDouble(PlayerDto::getPercentage)).get();
        return playerDto;
    }


    public float  onePlayerPercentage(int id){
        long countWins = diceGameRepository.findAll().stream()
                .filter(game -> game.getPlayer().getPlayer_id()==id)
                .filter(game -> game.getResult().equals("Winner")).count();

        long countAll = diceGameRepository.findAll().stream()
                .filter(game -> game.getPlayer().getPlayer_id()==id)
                .count();

        return ((float)countWins/countAll)*100;
    }

    //endregion


    //region updateMethods
    public PlayerDto updatePlayer(int id, PlayerDto playerDto){
        Optional<Player> player = dicePlayerRepository.findById(id);
        Player playerUpdate = player.get();
        if(player.isPresent()){
            playerUpdate.setName(playerDto.getName());
        }
        PlayerDto playerDtoReturn = convertFromPlayerToDto(playerUpdate);
        return playerDtoReturn;
    }

    //endregion----------------------------------------------------------


    //region deleteMethods

    public int deleteAllGamesFromPlayer(int id){
        Optional<Player> player = dicePlayerRepository.findById(id);
        Player player1 = null;
        if(player.isEmpty()){
            return -1;
        }else{
            player1 = player.get();
        }
        //one way
        if(diceGameRepository.existsByPlayer(player1)) {
            diceGameRepository.findAll()
                    .stream().filter(game -> game.getPlayer().getPlayer_id() == id)
                    .forEach(games -> diceGameRepository.deleteById(games.getGame_id()));
            return 0;
        }else{
            return -2;
        }
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
