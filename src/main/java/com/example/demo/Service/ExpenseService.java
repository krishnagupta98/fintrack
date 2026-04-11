package com.example.demo.Service;

import com.example.demo.Entity.Expense;
import com.example.demo.Entity.User;
import com.example.demo.Repository.ExpenseRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.dto.ExpenseRequest;
import com.example.demo.dto.ExpenseSummaryDTO;
import com.example.demo.dto.NotificationRequest;
import com.example.demo.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository repo;
    private final UserRepository userRepo;
    private final NotificationService notificationService;
    private static final Logger logger = LoggerFactory.getLogger(ExpenseService.class);

    public ExpenseService(ExpenseRepository repo, UserRepository userRepo, NotificationService notificationService) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.notificationService = notificationService;
    }

    public User getTheUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("user not found " + username));
    }
    @CacheEvict(value = "expenseSummary", key = "#root.target.getTheUser().id")
    public Expense saveExpense(ExpenseRequest request) {
        User currentUser = getTheUser();

        Expense expense = new Expense();
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setType(request.getType());
        expense.setCategory(request.getCategory());
        expense.setDateTime(LocalDateTime.now());

        expense.setUser(currentUser);

        if(expense.getAmount() > 5000){
            notificationService.sendAsyncNotification(
            new NotificationRequest(currentUser.getUsername(),"Alert : high amount of " + expense.getAmount() + "added.",1)
            );
        }
        return repo.save(expense);
    }

    public List<Expense> getallexpenses() {
        User currentUser = getTheUser();
        return repo.findByUser(currentUser);
    }

    public Expense getexpensefromid(long expenseid) {
        User currentUser = getTheUser();
        return repo.findById(expenseid)
                .filter(expense -> expense.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new  ResourceNotFoundException("expense not found " + expenseid));
    }
        @Transactional
        @CacheEvict(value = "expenseSummary", key = "#root.target.getTheUser().id")
    public Expense updatedexpense(long expenseid, Expense newexpense) {
        User currentUser = getTheUser();
        return repo.findById(expenseid)
                .filter(expense -> expense.getUser().getId().equals(currentUser.getId()))
                .map(existing -> {
                    existing.setTitle(newexpense.getTitle());
                    existing.setAmount(newexpense.getAmount());
                    existing.setCategory(newexpense.getCategory());
                    existing.setType(newexpense.getType());
                    existing.setDateTime(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
                    return repo.save(existing);
                }).orElseThrow(() -> new ResourceNotFoundException("cannot update : user not found or unauthorised id" + expenseid));
    }
        @Transactional
        @CacheEvict(value = "expenseSummary", key = "#root.target.getTheUser().id")
    public void deletebyid(long expenseid) {
        User currentUser = getTheUser();
        Expense expense = repo.findById(expenseid)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot delete: Expense not found with ID: " + expenseid));

        if (!expense.getUser().getId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Unauthorized: You do not own this expense");
        }

        repo.deleteById(expenseid);
    }

    public ExpenseSummaryDTO summaryDTO() {
        User currentUser = getTheUser();
        List<Expense> userExpenses = repo.findByUser(currentUser);

        long total = userExpenses.stream()
                .mapToLong(Expense::getAmount)
                .sum();

        Map<String, Long> breakdown = userExpenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingLong(Expense::getAmount)
                ));
        ExpenseSummaryDTO expenseSummaryDTO = new ExpenseSummaryDTO();
        expenseSummaryDTO.setTotalamount(total);
        expenseSummaryDTO.setCategoryBreakdown(breakdown);
        return expenseSummaryDTO;
    }

    @Cacheable (value = "expenseSummary", key = "#userid")
    public double getTotalExpenses(long L) {
    logger.info("Cache missing : fetching from the db for user {}",L);
        List<Expense> expenses = repo.findByUserId(L);

        return expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }
}