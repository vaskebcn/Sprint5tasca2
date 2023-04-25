package cat.itacademy.milos.diceGame.security.controller;


import cat.itacademy.milos.diceGame.security.model.auth.AuthenticationRequest;
import cat.itacademy.milos.diceGame.security.model.auth.AuthenticationResponse;
import cat.itacademy.milos.diceGame.security.model.auth.AuthenticationService;
import cat.itacademy.milos.diceGame.security.model.auth.RegisterRequest;
import cat.itacademy.milos.diceGame.security.model.dto.Message;
import cat.itacademy.milos.diceGame.security.model.service.DiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final DiceService diceService;

    @PostMapping("/register")
    public ResponseEntity<?>register(@RequestBody RegisterRequest request){
        if(diceService.checkName(request.getName())){
            Message message = new Message("User already exists with this name");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST );
        }else if(request.getName().isEmpty()|| request.getName().isBlank()|| request.getName()==null){
            request.setName("anonymous");
            return ResponseEntity.ok(service.register(request));
        }else {
            return ResponseEntity.ok(service.register(request));
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse>authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }

}
