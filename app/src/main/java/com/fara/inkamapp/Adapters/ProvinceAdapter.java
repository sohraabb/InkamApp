package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Models.NotificationList;
import com.fara.inkamapp.Models.Province;
import com.fara.inkamapp.R;

import java.util.ArrayList;

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ViewHolder> {

    private ArrayList<Province> provinces;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;


    // data is passed into the constructor
    public ProvinceAdapter(Context _context, ArrayList<Province> _provinces) {
        this.mInflater = LayoutInflater.from(_context);
        this.provinces = _provinces;
        this.context = _context;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_provinces, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Province _provinceList = provinces.get(position);

        if (_provinceList.get_name() != null)
            holder.name.setText(_provinceList.get_name());

    }

    // binds the data to the view and textview in each row
// total number of rows
    @Override
    public int getItemCount() {
        return (null != provinces ? provinces.size() : 0);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_province_name);
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMobile.ttf");
            name.setTypeface(typeface);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Province getItem(int id) {
        return provinces.get(id);
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
