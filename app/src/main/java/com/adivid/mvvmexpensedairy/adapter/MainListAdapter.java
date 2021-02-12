package com.adivid.mvvmexpensedairy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.domain.Expense;


public class MainListAdapter extends ListAdapter<Expense, MainListAdapter.MainListViewHolder> {


    public MainListAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public MainListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_list_one,parent,
                false);
        return new MainListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainListViewHolder holder, int position) {
        Expense expense = getItem(position);
        holder.textViewCategory.setText(expense.getTransaction_category());
        holder.textViewDate.setText(expense.getDate());
        holder.textViewMoney.setText(expense.getAmount());

    }

    public static class MainListViewHolder extends RecyclerView.ViewHolder{
        TextView textViewCategory, textViewDate, textViewMoney;
        ImageView imageView;

        public MainListViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewCategory = itemView.findViewById(R.id.tv_category);
            textViewDate = itemView.findViewById(R.id.tv_date);
            textViewMoney = itemView.findViewById(R.id.tv_money);
            imageView = itemView.findViewById(R.id.iv_category_icon);

        }
    }

    public static final DiffUtil.ItemCallback<Expense> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Expense>() {
                @Override
                public boolean areItemsTheSame(@NonNull Expense oldItem, @NonNull Expense newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Expense oldItem, @NonNull Expense newItem) {
                    return oldItem.hashCode() == newItem.hashCode();
                }
            };


}
