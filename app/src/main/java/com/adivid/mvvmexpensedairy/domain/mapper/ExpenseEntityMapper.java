package com.adivid.mvvmexpensedairy.domain.mapper;

import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;
import com.adivid.mvvmexpensedairy.domain.Expense;

import java.util.Date;

public class ExpenseEntityMapper implements DomainMapper<ExpenseEntity, Expense> {

    @Override
    public Expense mapToDomainModel(ExpenseEntity expenseEntity) {
        return new Expense(
                expenseEntity.getId(),
                expenseEntity.getDate().toString(),
                expenseEntity.getTime(),
                expenseEntity.getAmount(),
                expenseEntity.getTransaction_type(),
                expenseEntity.getTransaction_category(),
                expenseEntity.getNote(),
                expenseEntity.getPayment_type()
        );
    }

    @Override
    public ExpenseEntity mapFromDomainModel(Expense expense) {
        ExpenseEntity entity = new ExpenseEntity(
                new Date(),
                expense.getTime(),
                expense.getAmount(),
                expense.getTransaction_type(),
                expense.getTransaction_category(),
                expense.getNote(),
                expense.getPayment_type(),
                ""
        );
        return entity;
    }
}
