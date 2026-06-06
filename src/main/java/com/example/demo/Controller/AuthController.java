package com.example.demo.Controller;

import com.example.demo.Entity.LoginRequest;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Security.JwtUtil;
import com.example.demo.Service.BlackListService;
import com.example.demo.Service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private BlackListService blackListService;

    // --- TEMPORARY DIAGNOSTIC EXCEPTION HANDLER ---
    // This intercepts any 500 error and sends the actual Java stack trace back to your Flutter app
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw); 
        String stackTrace = sw.toString();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", ex.getMessage() != null ? ex.getMessage() : ex.toString(),
                        "type", ex.getClass().getName(),
                        "stackTrace", stackTrace
                ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> addNewUser(@RequestBody User user) {
        if (!rateLimiter.isAllowed(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("too many requests. please try again after 1 minute");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("username already exists");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("user successfully created!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if (!rateLimiter.isAllowed(loginRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("too many requests. please try again after 1 minute");
        }
        var userOptional = userRepository.findByUsername(loginRequest.getUsername());
        if (userOptional.isPresent() && bCryptPasswordEncoder.matches(
                loginRequest.getPassword(), userOptional.get().getPassword())) {
            String accessToken = jwtUtil.generateAccessToken(loginRequest.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(loginRequest.getUsername());
            return ResponseEntity.ok(Map.of("accessToken", accessToken, "refreshToken", refreshToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid username or password");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        String username = jwtUtil.extractUsername(refreshToken);
        if (username != null && jwtUtil.validateToken(refreshToken, username)) {
            String newAccessToken = jwtUtil.generateAccessToken(username);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid refresh token");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token){
        if (token !=null && token.startsWith("Bearer ")){
            String jwt = token.substring(7);
            Date expiry = jwtUtil.extractExpiration(jwt);
            blackListService.blackListToken(jwt,expiry.getTime());

            return ResponseEntity.ok("Successfully logged out and token invalidated");
        }
        return ResponseEntity.badRequest().body("Invalid Authorization header.");
    }
}