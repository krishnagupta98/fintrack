package com.example.demo.dto;

import java.util.Map;

public class ExpenseSummaryDTO {
        private Long totalamount;
        private Map<String,Long> categoryBreakdown;


    public ExpenseSummaryDTO() {
    }

    public ExpenseSummaryDTO(Map<String, Long> categoryBreakdown, Long totalamount) {
        this.categoryBreakdown = categoryBreakdown;
        this.totalamount = totalamount;
    }

    public Long getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(Long totalamount) {
        this.totalamount = totalamount;
    }

    public Map<String, Long> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(Map<String, Long> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }
}
