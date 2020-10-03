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

import com.fara.inkamapp.Models.ChargesList;
import com.fara.inkamapp.R;

import java.util.ArrayList;

public class PurchasedTopicsAdapter extends RecyclerView.Adapter<PurchasedTopicsAdapter.ViewHolder> {

    private ArrayList<String> purchasedType;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private int type;


    // data is passed into the constructor
    public PurchasedTopicsAdapter(Context _context, ArrayList<String> _purchasedType, int _type) {
        this.mInflater = LayoutInflater.from(_context);
        this.purchasedType = _purchasedType;
        this.context = _context;
        this.type = _type;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_purchased_topics, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(purchasedType.get(position));

        if (type == position) {
            holder.background.setBackgroundResource(R.drawable.green_rounded_background);
            holder.title.setTextColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.background.setBackgroundResource(R.drawable.light_rounded_background);
            holder.title.setTextColor(context.getResources().getColor(R.color.colorBrown));
        }

    }

    // binds the data to the view and textview in each row total number of rows


    @Override
    public int getItemCount() {
        return (null != purchasedType ? purchasedType.size() : 0);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private RelativeLayout background;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_purchased);
            background = itemView.findViewById(R.id.rl_back_topic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
                type = getAdapterPosition();
                notifyDataSetChanged();

//                if (type == getAdapterPosition()) {
//                    background.setBackgroundResource(R.drawable.green_rounded_background);
//                    title.setTextColor(context.getResources().getColor(R.color.colorWhite));
//                }
            }
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return purchasedType.get(id);
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


