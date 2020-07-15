package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Models.ChargesList;
import com.fara.inkamapp.R;

import java.util.ArrayList;

public class ChargeAmountAdapter extends RecyclerView.Adapter<ChargeAmountAdapter.ViewHolder> {

    private ArrayList<ChargesList> chargesLists;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;


    // data is passed into the constructor
    public ChargeAmountAdapter(Context _context, ArrayList<ChargesList> _chargeList) {
        this.mInflater = LayoutInflater.from(_context);
        this.chargesLists = _chargeList;
        this.context = _context;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_charge_list, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChargesList _charges = chargesLists.get(position);

        if (_charges.get_amount() != null)
            holder.amount.setText(_charges.get_amount());

        if (_charges.get_logo() != 0)
            holder.logo.setImageResource(_charges.get_logo());

//        if (_contacts.get_description() != null)
//            holder.name.setText(_contacts.get_description());

//        if(_cards.get_bankName() !=null)
//            if(_cards.get_bankName().equals("saman"))
//                holder.background.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));


    }

    // binds the data to the view and textview in each row
// total number of rows
    @Override
    public int getItemCount() {
        return (null != chargesLists ? chargesLists.size() : 0);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView logo;
        private TextView amount;

        ViewHolder(View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.iv_operator_logo);
            amount = itemView.findViewById(R.id.charge_amount);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public ChargesList getItem(int id) {
        return chargesLists.get(id);
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


