package com.example.demo.Service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Also import your own classes!
import com.example.demo.Entity.Expense;
import com.example.demo.Repository.ExpenseRepository;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository; // Faking the DB

    @InjectMocks
    private ExpenseService expenseService; // Injecting the fake DB into your service

    @Test
    void testTotalCalculation() {
        // 1. Arrange: Setup fake data
        Expense e1 = new Expense(); e1.setAmount(100);
        Expense e2 = new Expense(); e2.setAmount(250);
        List<Expense> mockList = Arrays.asList(e1, e2);

        // Mock the specific repository call used in your service
        when(expenseRepository.findByUserId(1L)).thenReturn(mockList);

        // 2. Act: Call the business logic method
        double result = expenseService.getTotalExpenses(1L);

        // 3. Assert: Verify the math (100 + 250 = 350)
        assertEquals(350.0, result);

        // Verify the repository was called exactly once with the correct ID
        verify(expenseRepository, times(1)).findByUserId(1L);
    }
    }
