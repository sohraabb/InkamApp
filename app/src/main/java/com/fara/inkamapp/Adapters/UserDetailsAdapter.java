package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.content.Intent;
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

import com.fara.inkamapp.Activities.UserDetails;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.PercentageCode;
import com.fara.inkamapp.Models.UserCard;
import com.fara.inkamapp.R;

import java.util.ArrayList;

public class UserDetailsAdapter extends RecyclerView.Adapter<UserDetailsAdapter.ViewHolder> {

    private ArrayList<PercentageCode> percentageCodes;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private String app_download = "income-app.ir";


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
            holder.phoneNumber.setText(_percentageCode.get_code());

        if (_percentageCode.get_userCount() != null)
            holder.individuals.setText(_percentageCode.get_userCount());
        holder.tvMyFriend.setText(_percentageCode.get_name());

        holder.inviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "مشترک گرامی شما از طرف " + Numbers.ToPersianNumbers(_percentageCode.get_name()) +
                        " به اپلیکیشن اینکام دعوت شده اید.\n با اینکام میتوانید از خرید های همیشگی اطرافیانتان مادام العمر کسب درامد کنید. \n" +
                        " لینک دانلود اپ : " + app_download + "\n" +
                        " توجه :‌ در زمان ثبت نام کد معرف را " + _percentageCode.get_code() + " وارد کنید. ";

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);

                context.startActivity(Intent.createChooser(share, "دعوت دوستان"));
            }
        });


    }

    // binds the data to the view and textview in each row
    // total number of rows
    @Override
    public int getItemCount() {
        return (null != percentageCodes ? percentageCodes.size() : 0);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView phoneNumber, individuals, tvMyFriend;
        private ImageButton editPhone, usersId;
        private Button inviteFriends;
        private RelativeLayout rlUsersId;

        ViewHolder(View itemView) {
            super(itemView);
            phoneNumber = itemView.findViewById(R.id.tv_phone_number);
            individuals = itemView.findViewById(R.id.tv_individuals_value);
            tvMyFriend = itemView.findViewById(R.id.tv_my_friend);
            editPhone = itemView.findViewById(R.id.ib_edit_phone_num);
            usersId = itemView.findViewById(R.id.ib_users_id);
            inviteFriends = itemView.findViewById(R.id.btn_invite_friends);
            rlUsersId = itemView.findViewById(R.id.rl_users_id);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            Intent intent = new Intent(context, UserDetails.class);
            intent.putExtra("code", percentageCodes.get(getAdapterPosition()).get_code());
            intent.putExtra("userCount", percentageCodes.get(getAdapterPosition()).get_userCount());
            intent.putExtra("name", percentageCodes.get(getAdapterPosition()).get_name());
            context.startActivity(intent);
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
