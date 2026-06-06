package com.example.demo.Controller;

import com.example.demo.Entity.Expense;
import com.example.demo.Service.ExpenseService;
import com.example.demo.dto.ExpenseRequest;
import com.example.demo.dto.ExpenseSummaryDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService service;
    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        List<Expense> expenses = service.getallexpenses();
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable("id") long expenseId) {
        Expense expense = service.getexpensefromid(expenseId);
        return ResponseEntity.ok(expense);
    }

    @PostMapping
    public ResponseEntity<Expense> addNewExpense(@Valid @RequestBody ExpenseRequest request, Principal principal) {
        Expense savedExpense = service.saveExpense(request);
        return new ResponseEntity<>(savedExpense, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> update(@PathVariable("id") long expenseId,
                                         @Valid @RequestBody Expense newExpense) {
        Expense updated = service.updatedexpense(expenseId, newExpense);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long expenseId) {
        service.deletebyid(expenseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<ExpenseSummaryDTO> getSummary() {
        ExpenseSummaryDTO summaryDTO = service.summaryDTO();
        return ResponseEntity.ok(summaryDTO);
    }
}