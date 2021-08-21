/*
package com.adivid.mvvmexpensedairy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.adapter.interfaces.OnItemClickListener;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;
import com.adivid.mvvmexpensedairy.utils.Utils;

public class WeekListAdapter extends RecyclerView.Adapter<WeekListAdapter.MyWeekViewHolder> {

    private final OnItemClickListener onItemClickListener;

    public WeekListAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyWeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_list_two, parent,
                false);
        return new MyWeekViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWeekViewHolder holder, int position) {
        ExpenseEntity expense = getItem(position);
        holder.textViewNote.setText(expense.getNote());
        holder.textViewDate.setText(Utils.convertToDisplayDate(expense.getDate()));
        holder.textViewMoney.setText("₹ " + expense.getAmount());

       // setCategoryIcon(holder, expense.getTransaction_category());

        if(expense.getPayment_type().equals("Cash")){
            holder.imageViewPayment.setImageResource(R.drawable.ic_cash);
        }else {
            holder.imageViewPayment.setImageResource(R.drawable.ic_credit_card);
        }

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public static class MyWeekViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textViewNote, textViewDate, textViewMoney;
        private final ImageView imageView, imageViewPayment;
        private final OnItemClickListener onItemClickListener;

        public MyWeekViewHolder(@NonNull View itemView,
                                  OnItemClickListener onItemClickListener) {
            super(itemView);

            this.onItemClickListener = onItemClickListener;
            textViewNote = itemView.findViewById(R.id.tv_note);
            textViewDate = itemView.findViewById(R.id.tv_date);
            textViewMoney = itemView.findViewById(R.id.tv_money);
            imageView = itemView.findViewById(R.id.iv_category_icon);
            imageViewPayment = itemView.findViewById(R.id.imageViewPaymentMode);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public static final DiffUtil.ItemCallback<ExpenseEntity> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ExpenseEntity>() {
                @Override
                public boolean areItemsTheSame(@NonNull ExpenseEntity oldItem, @NonNull ExpenseEntity newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull ExpenseEntity oldItem, @NonNull ExpenseEntity newItem) {
                    return oldItem.hashCode() == newItem.hashCode();
                }
            };



}
*/
