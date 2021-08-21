package com.roshanadke.mvvmexpensedairy.domain.mapper;

import com.roshanadke.mvvmexpensedairy.data.db.ExpenseEntity;
import com.roshanadke.mvvmexpensedairy.domain.FirebaseExpenseDto;

public class FirebaseExpenseMapper implements DomainMapper<ExpenseEntity, FirebaseExpenseDto> {
    @Override
    public FirebaseExpenseDto mapToDomainModel(ExpenseEntity expenseEntity) {
        return new FirebaseExpenseDto(
                expenseEntity.getDate(),
                expenseEntity.getTime(),
                expenseEntity.getAmount(),
                expenseEntity.getTransaction_type(),
                expenseEntity.getTransaction_category(),
                expenseEntity.getNote(),
                expenseEntity.getPayment_type(),
                expenseEntity.getCurrent_time_millis()
        );
    }

    @Override
    public ExpenseEntity mapFromDomainModel(FirebaseExpenseDto firebaseExpenseDto) {
        ExpenseEntity expenseEntity = new ExpenseEntity(
                firebaseExpenseDto.getDate(),
                firebaseExpenseDto.getTime(),
                firebaseExpenseDto.getAmount(),
                firebaseExpenseDto.getTransaction_type(),
                firebaseExpenseDto.getTransaction_category(),
                firebaseExpenseDto.getNote(),
                firebaseExpenseDto.getPayment_type(),
                firebaseExpenseDto.getTimestamp()
        );
        expenseEntity.setDocId(firebaseExpenseDto.getDocId());
        expenseEntity.setFirebaseUid(firebaseExpenseDto.getFirebaseUId());
        return expenseEntity;
    }
}
