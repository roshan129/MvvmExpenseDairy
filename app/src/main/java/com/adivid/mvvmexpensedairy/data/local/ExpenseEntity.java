package com.adivid.mvvmexpensedairy.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ExpenseEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;

    private String time;

    private String amount;

    private String transaction_type;

    private String transaction_category;

    private String note;

    private String current_time_millis;

    public ExpenseEntity(String date, String time, String amount,
                         String transaction_type, String transaction_category, String note,
                         String current_time_millis) {
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.transaction_type = transaction_type;
        this.transaction_category = transaction_category;
        this.note = note;
        this.current_time_millis = current_time_millis;
    }

    public String getCurrent_time_millis() {
        return current_time_millis;
    }

    public void setCurrent_time_millis(String current_time_millis) {
        this.current_time_millis = current_time_millis;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getTransaction_category() {
        return transaction_category;
    }

    public void setTransaction_category(String transaction_category) {
        this.transaction_category = transaction_category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
