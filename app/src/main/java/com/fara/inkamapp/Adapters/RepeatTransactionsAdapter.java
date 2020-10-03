package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.BottomSheetFragments.RepeatTransaction;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.Province;
import com.fara.inkamapp.Models.RepeatPurchase;
import com.fara.inkamapp.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class RepeatTransactionsAdapter extends RecyclerView.Adapter<RepeatTransactionsAdapter.ViewHolder> {

    private List<RepeatPurchase> repeatPurchaseList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private String finalAmount;

    // data is passed into the constructor
    public RepeatTransactionsAdapter(Context _context, List<RepeatPurchase> _repeatPurchase) {
        this.mInflater = LayoutInflater.from(_context);
        this.repeatPurchaseList = _repeatPurchase;
        this.context = _context;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_repeat_transactions, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RepeatPurchase _repeatPurchase = repeatPurchaseList.get(position);

        if (_repeatPurchase.get_amountString() != null)
            holder.amount.setText(_repeatPurchase.get_amountString());

        if (_repeatPurchase.get_mobile() != null)
            holder.mobile.setText(Numbers.ToPersianNumbers(_repeatPurchase.get_mobile()));

        if (_repeatPurchase.get_operator() == 0)
            holder.operator_logo.setImageResource(R.drawable.irancell_logo_green);
        else if (_repeatPurchase.get_operator() == 1)
            holder.operator_logo.setImageResource(R.drawable.hamrah_aval_logo_green);
        else if (_repeatPurchase.get_operator() == 2)
            holder.operator_logo.setImageResource(R.drawable.rightel_logo);

        if (_repeatPurchase.get_amount() != 0.0) {
            NumberFormat formatter = new DecimalFormat("#,###");
            finalAmount = formatter.format(Double.valueOf(_repeatPurchase.get_amount()));
        }

        if (_repeatPurchase.get_type().equals("0")) {
            holder.amount.setText("شارژ" + " " + Numbers.ToPersianNumbers(String.valueOf(finalAmount)) + " " + "ریالی");
        } else {
            holder.amount.setText("بسته" + " " + Numbers.ToPersianNumbers(String.valueOf(finalAmount)) + " " + "ریالی");

        }

    }

    // binds the data to the view and textview in each row
    // total number of rows
    @Override
    public int getItemCount() {
        return repeatPurchaseList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView operator_logo;
        private TextView mobile, amount;
        private RelativeLayout background;

        ViewHolder(View itemView) {
            super(itemView);
            operator_logo = itemView.findViewById(R.id.iv_op_logo);
            mobile = itemView.findViewById(R.id.tv_phone);
            amount = itemView.findViewById(R.id.tv_charge);
            background = itemView.findViewById(R.id.rl_background);

            itemView.setOnClickListener(this);

//            background.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    view.setBackgroundResource(R.drawable.green_stroke_background);
//                }
//            });


        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
                background.setBackgroundResource(R.drawable.green_stroke_background);
            }

        }
    }

    // convenience method for getting data at click position
    public RepeatPurchase getItem(int id) {
        return repeatPurchaseList.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
