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
import com.adivid.mvvmexpensedairy.adapter.interfaces.OnItemClickListener;
import com.adivid.mvvmexpensedairy.domain.Expense;


public class MainListAdapter extends ListAdapter<Expense, MainListAdapter.MainListViewHolder> {

    private final OnItemClickListener onItemClickListener;

    public MainListAdapter(OnItemClickListener onItemClickListener) {
        super(DIFF_CALLBACK);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MainListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_list_one,parent,
                false);
        return new MainListViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MainListViewHolder holder, int position) {
        Expense expense = getItem(position);
        holder.textViewCategory.setText(expense.getTransaction_category());
        holder.textViewDate.setText(expense.getDate());
        holder.textViewMoney.setText(expense.getAmount());

    }

    public static class MainListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewCategory, textViewDate, textViewMoney;
        private ImageView imageView;
        private final OnItemClickListener onItemClickListener;

        public MainListViewHolder(@NonNull View itemView,
                                  OnItemClickListener onItemClickListener) {
            super(itemView);

            this.onItemClickListener = onItemClickListener;
            textViewCategory = itemView.findViewById(R.id.tv_category);
            textViewDate = itemView.findViewById(R.id.tv_date);
            textViewMoney = itemView.findViewById(R.id.tv_money);
            imageView = itemView.findViewById(R.id.iv_category_icon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
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
