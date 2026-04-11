package com.example.demo.Repository;

import com.example.demo.Entity.Expense;
import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {

    @Query("SELECT SUM(e.amount) FROM Expense e")
    Long gettotalamount();

    @Query("SELECT e.category, SUM(e.amount) FROM Expense e GROUP BY e.category")
    List<Object[]> getCategorySummaries();

    List<Expense> findByUser(User user);

    List<Expense> findByUserId(Long userId);
}
