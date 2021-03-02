package com.adivid.mvvmexpensedairy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.adapter.interfaces.OnItemClickListener;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;
import com.adivid.mvvmexpensedairy.utils.Utils;

import static com.adivid.mvvmexpensedairy.utils.Constants.*;


public class MainListAdapter extends ListAdapter<ExpenseEntity, MainListAdapter.MainListViewHolder> {

    private final OnItemClickListener onItemClickListener;

    public MainListAdapter(OnItemClickListener onItemClickListener) {
        super(DIFF_CALLBACK);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MainListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_list_two, parent,
                false);
        return new MainListViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MainListViewHolder holder, int position) {
        ExpenseEntity expense = getItem(position);
        holder.textViewNote.setText(expense.getNote());
        holder.textViewDate.setText(Utils.convertToDisplayDate(expense.getDate()));
        holder.textViewMoney.setText("₹ " + expense.getAmount());

        setCategoryIcon(holder, expense.getTransaction_category());

        if(expense.getPayment_type().equals("Cash")){
            holder.imageViewPayment.setImageResource(R.drawable.ic_cash);
        }else {
            holder.imageViewPayment.setImageResource(R.drawable.ic_credit_card);
        }

    }

    public static class MainListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textViewNote, textViewDate, textViewMoney;
        private final ImageView imageView, imageViewPayment;
        private final OnItemClickListener onItemClickListener;

        public MainListViewHolder(@NonNull View itemView,
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




    private void setCategoryIcon(MainListViewHolder holder, String transaction_category) {
        switch (transaction_category) {
            case OTHERS:
                holder.imageView.setImageResource(R.drawable.ic_others);
                break;
            case FOOD_N_DINING:
                holder.imageView.setImageResource(R.drawable.ic_food_and_dining);
                break;
            case SHOPPING:
                holder.imageView.setImageResource(R.drawable.ic_shopping_cart);
                break;
            case TRAVELLING:
                holder.imageView.setImageResource(R.drawable.ic_travel);
                break;
            case ENTERTAINMENT:
                holder.imageView.setImageResource(R.drawable.ic_entertainment);
                break;
            case MEDICAL:
                holder.imageView.setImageResource(R.drawable.ic_medical);
                break;
            case PERSONAL_CARE:
                holder.imageView.setImageResource(R.drawable.ic_personal_care);
                break;
            case EDUCATION:
                holder.imageView.setImageResource(R.drawable.ic_education);
                break;
            case BILLS_AND_UTILITIES:
                holder.imageView.setImageResource(R.drawable.ic_bills);
                break;
            case INVESTMENTS:
                holder.imageView.setImageResource(R.drawable.ic_investment);
                break;
            case RENT:
                holder.imageView.setImageResource(R.drawable.ic_rent);
                break;
            case TAXES:
                holder.imageView.setImageResource(R.drawable.ic_taxes);
                break;
            case INSURANCE:
                holder.imageView.setImageResource(R.drawable.ic_insurance);
                break;
            case GIFTS_AND_DONATIONS:
                holder.imageView.setImageResource(R.drawable.ic_gifts);
                break;


        }
    }

}
