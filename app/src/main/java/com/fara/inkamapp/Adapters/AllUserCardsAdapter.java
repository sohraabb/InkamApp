package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Models.UserCard;
import com.fara.inkamapp.R;

import java.util.ArrayList;
import java.util.List;

public class AllUserCardsAdapter extends RecyclerView.Adapter<AllUserCardsAdapter.ViewHolder> {

    private ArrayList<UserCard> userCards;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;


    // data is passed into the constructor
    public AllUserCardsAdapter(Context _context, ArrayList<UserCard> _userCards) {
        this.mInflater = LayoutInflater.from(_context);
        this.userCards = _userCards;
        this.context = _context;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_all_user_cards, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UserCard _cards = userCards.get(position);

        if (_cards.get_cardNumber() != null)
            holder.cardNum.setText(_cards.get_cardNumber());

        if (_cards.get_expDate() != null)
            holder.expirationDate.setText(_cards.get_expDate());

        if (_cards.get_bankName() != null)
            holder.name.setText(_cards.get_bankName());

//        if(_cards.get_bankName() !=null)
//            if(_cards.get_bankName().equals("saman"))
//                holder.background.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));



    }

    // binds the data to the view and textview in each row
// total number of rows
    @Override
    public int getItemCount() {
        return (null != userCards ? userCards.size() : 0);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView cardNum, shebaNum, name, expirationDate;
        private ImageButton edit;
        private ImageView cardIcon;
        private RelativeLayout background;

        ViewHolder(View itemView) {
            super(itemView);
            shebaNum = itemView.findViewById(R.id.tv_card_sheba);
            cardNum = itemView.findViewById(R.id.tv_card_num);
            expirationDate = itemView.findViewById(R.id.tv_card_expiration);
            name = itemView.findViewById(R.id.tv_card_name);
            edit = itemView.findViewById(R.id.ib_edit_card);
            cardIcon = itemView.findViewById(R.id.iv_bank);
            background = itemView.findViewById(R.id.rl_card);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public UserCard getItem(int id) {
        return userCards.get(id);
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

