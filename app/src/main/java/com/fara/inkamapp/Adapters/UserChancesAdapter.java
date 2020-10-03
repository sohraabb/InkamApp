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
import com.fara.inkamapp.Models.Chances;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserChancesAdapter extends RecyclerView.Adapter<UserChancesAdapter.ViewHolder> {

    private List<Chances> chances;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    public UserChancesAdapter(Context _context, List<Chances> _chances) {
        this.mInflater = LayoutInflater.from(_context);
        this.chances = _chances;
        this.context = _context;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Chances _chances = chances.get(position);

            if (_chances.get_persianDateTime() != null)
                holder.date.setText(_chances.get_persianDateTime());

            if (_chances.get_code() != null)
                holder.code.setText(_chances.get_code());


        } catch (Exception e) {
            e.toString();
        }
    }


    @Override
    public int getItemCount() {
        return chances.size();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_user_chances, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView date, code;


        ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.tv_date);
            code = itemView.findViewById(R.id.tv_code);


            Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                    "fonts/IRANSansMobile.ttf");

            date.setTypeface(typeface);
            code.setTypeface(typeface);

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
