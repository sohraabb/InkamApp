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
import com.fara.inkamapp.Models.JModel;
import com.fara.inkamapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChancesAdapter extends RecyclerView.Adapter<ChancesAdapter.ViewHolder> {

    private ArrayList<JModel> chancesList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;


    // data is passed into the constructor
    public ChancesAdapter(Context _context, ArrayList<JModel> _chances) {
        this.mInflater = LayoutInflater.from(_context);
        this.chancesList = _chances;
        this.context = _context;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_chances, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final JModel _chances = chancesList.get(position);

            holder.names.setText(_chances.name);
            holder.values.setText(_chances.value);

    }

    // binds the data to the view and textview in each row
    // total number of rows
    @Override
    public int getItemCount() {
        return (null != chancesList ? chancesList.size() : 0);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView names, values;

        ViewHolder(View itemView) {
            super(itemView);
            names = itemView.findViewById(R.id.tv_names);
            values = itemView.findViewById(R.id.tv_values);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public JModel getItem(int id) {
        return chancesList.get(id);
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


