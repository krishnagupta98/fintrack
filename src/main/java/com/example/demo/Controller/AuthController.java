package com.example.demo.Controller;

import com.example.demo.Entity.LoginRequest;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Security.JwtUtil;
import com.example.demo.Service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RateLimiterService rateLimiter;


    @PostMapping("/register")
    public ResponseEntity<?> addNewser(@RequestBody User user){
        if(!rateLimiter.isAllowed(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("too many requests. please try again after 1 minute");

        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()){
            return ResponseEntity.badRequest().body("username already exists");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("user successfully created !");

    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){

        if(!rateLimiter.isAllowed(loginRequest.getUsername())){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("too many requests. please try again after 1 minute");
        }

     var userOptional = userRepository.findByUsername(loginRequest.getUsername()) ;

        if (userOptional.isPresent() && bCryptPasswordEncoder.matches(loginRequest.getPassword(),(userOptional.get().getPassword()))) {
        String token =jwtUtil.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(Map.of("token",token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid username or password");
        }

}
