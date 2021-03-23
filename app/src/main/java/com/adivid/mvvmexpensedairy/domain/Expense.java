package com.adivid.mvvmexpensedairy.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Expense implements Parcelable {

    private int id;

    private String date;

    private String time;

    private String amount;

    private String transaction_type;

    private String transaction_category;

    private String note;

    private String payment_type;

    public Expense(int id, String date, String time, String amount, String transaction_type,
                   String transaction_category, String note, String payment_type) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.transaction_type = transaction_type;
        this.transaction_category = transaction_category;
        this.note = note;
        this.payment_type = payment_type;
    }

    protected Expense(Parcel in) {
        id = in.readInt();
        date = in.readString();
        time = in.readString();
        amount = in.readString();
        transaction_type = in.readString();
        transaction_category = in.readString();
        note = in.readString();
        payment_type = in.readString();
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(amount);
        dest.writeString(transaction_type);
        dest.writeString(transaction_category);
        dest.writeString(note);
        dest.writeString(payment_type);
    }
}
