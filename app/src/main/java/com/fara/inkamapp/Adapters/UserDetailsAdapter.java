package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Models.PercentageCode;
import com.fara.inkamapp.Models.UserCard;
import com.fara.inkamapp.R;

import java.util.ArrayList;

    public class UserDetailsAdapter extends RecyclerView.Adapter<UserDetailsAdapter.ViewHolder> {

        private ArrayList<PercentageCode> percentageCodes;
        private LayoutInflater mInflater;
        private ItemClickListener mClickListener;
        private Context context;


        // data is passed into the constructor
        public UserDetailsAdapter(Context _context, ArrayList<PercentageCode> _percetageCode) {
            this.mInflater = LayoutInflater.from(_context);
            this.percentageCodes = _percetageCode;
            this.context = _context;
        }

        // inflates the row layout from xml when needed
        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.adapter_add_new_id, parent, false);
            context = parent.getContext();
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final PercentageCode _percentageCode = percentageCodes.get(position);

            if (_percentageCode.get_name() != null)
                holder.phoneNumber.setText(_percentageCode.get_name());

//            if (_percentageCode.get_code() != null)
//                holder.expirationDate.setText(_percentageCode.get_code());

            if (_percentageCode.get_userCount() != null)
                holder.individuals.setText(_percentageCode.get_userCount());

//        if(_cards.get_bankName() !=null)
//            if(_cards.get_bankName().equals("saman"))
//                holder.background.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));



        }

        // binds the data to the view and textview in each row
// total number of rows
        @Override
        public int getItemCount() {
            return (null != percentageCodes ? percentageCodes.size() : 0);
        }

        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView phoneNumber, individuals;
            private ImageButton editPhone, usersId;
            private Button inviteFriends;

            ViewHolder(View itemView) {
                super(itemView);
                phoneNumber = itemView.findViewById(R.id.tv_phone_number);
                individuals = itemView.findViewById(R.id.tv_individuals_value);
                editPhone = itemView.findViewById(R.id.ib_edit_phone_num);
                usersId = itemView.findViewById(R.id.ib_users_id);
                inviteFriends = itemView.findViewById(R.id.btn_invite_friends);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        // convenience method for getting data at click position
        public PercentageCode getItem(int id) {
            return percentageCodes.get(id);
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
