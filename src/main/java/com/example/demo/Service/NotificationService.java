package com.example.demo.Service;


import com.example.demo.Entity.NotificationLog;
import com.example.demo.Repository.NotificationRepository;
import com.example.demo.dto.NotificationRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository){
        this.repository = repository;
    }
        @Async("notificationExecutor")
        @Transactional
        @CacheEvict(value = "notifications", key = "#request.userId()")
        public void sendAsyncNotification(NotificationRequest request){
        NotificationLog logEntry = new NotificationLog();
        logEntry.setUserId(request.userId());
        logEntry.setMessage(request.message());
        logEntry.setStatus("PENDING");
       logEntry= repository.save(logEntry);

         try{
             log.info("Processing notification ID: {}", logEntry.getId());


            logEntry.setStatus("SENT");
            log.info("SUCCESS : Noticafication {} delivered." , logEntry.getId());
            repository.save(logEntry);


         } catch (Exception e) {
             logEntry.setStatus("FAILED");
             repository.save(logEntry);
             log.error("ERROR: Failed to send notification {}: {}", logEntry.getId(), e.getMessage());
         }
    }

    @Cacheable(value = "notifications2",key = "#userId")
    public List<NotificationLog> getnotifications(String userId){
        log.info("cache miss: fetching notification history for user {}",userId);
        return repository.findByUserId(userId);    }
}
