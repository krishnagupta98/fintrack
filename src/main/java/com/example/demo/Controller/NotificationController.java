package com.example.demo.Controller;

import com.example.demo.Service.NotificationService;
import com.example.demo.Service.RateLimiterService;
import com.example.demo.dto.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;
    private final RateLimiterService rateLimiterService;

    public NotificationController(NotificationService notificationService,RateLimiterService rateLimiterService){
        this.notificationService = notificationService;
        this.rateLimiterService=rateLimiterService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request){

        if (!rateLimiterService.isAllowed(request.userId())){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("slow down! you have sent to many requests.try again in a minute");

        }

        log.info("received notification request for user: {}",request.userId());

        notificationService.sendAsyncNotification(request);

        return ResponseEntity.accepted()
                .body("notification request accepted and is being processed");


    }
}
