package com.adivid.mvvmexpensedairy.domain.mapper;

import com.adivid.mvvmexpensedairy.data.local.ExpenseEntity;
import com.adivid.mvvmexpensedairy.domain.Expense;

public class ExpenseEntityMapper implements DomainMapper<ExpenseEntity, Expense> {

    @Override
    public Expense mapToDomainModel(ExpenseEntity expenseEntity) {
        return new Expense(
                expenseEntity.getId(),
                expenseEntity.getDate(),
                expenseEntity.getTime(),
                expenseEntity.getAmount(),
                expenseEntity.getTransaction_type(),
                expenseEntity.getTransaction_category(),
                expenseEntity.getNote()
        );
    }

    @Override
    public ExpenseEntity mapFromDomainModel(Expense expense) {
        ExpenseEntity entity = new ExpenseEntity(
                expense.getDate(),
                expense.getTime(),
                expense.getAmount(),
                expense.getTransaction_type(),
                expense.getTransaction_category(),
                expense.getNote(),
                ""
        );
        return entity;
    }
}
