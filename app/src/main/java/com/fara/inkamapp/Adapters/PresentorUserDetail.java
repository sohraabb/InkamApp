package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class PresentorUserDetail extends RecyclerView.Adapter<PresentorUserDetail.ViewHolder> {

    private List<User> percentageCodes;
    private LayoutInflater mInflater;
    private UserDetailsAdapter.ItemClickListener mClickListener;
    private Context context;


    // data is passed into the constructor
    public PresentorUserDetail(Context _context, List<User> _percetageCode) {
        this.mInflater = LayoutInflater.from(_context);
        this.percentageCodes = _percetageCode;
        this.context = _context;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_user_detail, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User _percentageCode = percentageCodes.get(position);

        StringBuilder myName = new StringBuilder(Numbers.ToPersianNumbers(_percentageCode.getUserName()));
        myName.setCharAt(myName.length() - 6, '*');
        myName.setCharAt(myName.length() - 5, '*');
        myName.setCharAt(myName.length() - 4, '*');

        holder.phoneNumber.setText(myName);
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(Double.valueOf(_percentageCode.getTodayProfit()));
        holder.tv_price.setText(Numbers.ToPersianNumbers(formattedNumber));

    }

    // binds the data to the view and textview in each row
// total number of rows
    @Override
    public int getItemCount() {
        return (null != percentageCodes ? percentageCodes.size() : 0);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView phoneNumber, tv_price, type, rial;


        ViewHolder(View itemView) {
            super(itemView);
            phoneNumber = itemView.findViewById(R.id.tv_user_phone_number);
            tv_price = itemView.findViewById(R.id.tv_price);
            type = itemView.findViewById(R.id.tv_detail_type);
            rial = itemView.findViewById(R.id.tv_rial);

            Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                    "fonts/IRANSansMobile.ttf");
            phoneNumber.setTypeface(typeface);
            tv_price.setTypeface(typeface);
            type.setTypeface(typeface);
            rial.setTypeface(typeface);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

        }
    }

    // convenience method for getting data at click position
    public User getItem(int id) {
        return percentageCodes.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(UserDetailsAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
