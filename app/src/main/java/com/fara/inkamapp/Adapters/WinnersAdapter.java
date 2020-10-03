package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WinnersAdapter extends RecyclerView.Adapter<WinnersAdapter.ViewHolder> {

    private List<User> userList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    public WinnersAdapter(Context _context, List<User> _users) {
        this.mInflater = LayoutInflater.from(_context);
        this.userList = _users;
        this.context = _context;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            User _user = userList.get(position);

            if (!_user.getProfilePicURL().equals("anyType") && _user.getProfilePicURL() != null) {
                Picasso.with(context)
                        .load("http://" + _user.getProfilePicURL()).resize(100, 100).centerCrop().placeholder(R.drawable.profile_pic).into(holder.userImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.i("Sohrab P", "Success");

                    }

                    @Override
                    public void onError() {

                    }

                });

            }

            if (!_user.getFirstName().equals("anyType") && !_user.getLastName().equals("anyType")) {
                holder.name.setText(_user.getFirstName() + " " + _user.getLastName());
            }

            holder.chances.setText(Numbers.ToPersianNumbers(String.valueOf(_user.getChanceCount())));


        } catch (Exception e) {
            e.toString();
        }
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_winners, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name, chances, chancesTxt;
        private CircleImageView userImage;


        ViewHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.iv_user_image);
            name = itemView.findViewById(R.id.tv_winner_name);
            chances = itemView.findViewById(R.id.tv_chances_value);
            chancesTxt = itemView.findViewById(R.id.tv_chances_text);

            Typeface iranSansBold = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMobile_Bold.ttf");
            Typeface iranSansMedium = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMobile_Medium.ttf");

            chancesTxt.setTypeface(iranSansBold);
            chances.setTypeface(iranSansBold);
            name.setTypeface(iranSansMedium);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
}
