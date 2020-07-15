package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Models.Passengers;
import com.fara.inkamapp.R;

import java.util.ArrayList;
import java.util.List;

public class PassengersAdapter extends RecyclerView.Adapter<PassengersAdapter.ViewHolder> {
    private List<Passengers> services;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    public PassengersAdapter(Context _context, List<Passengers> passengers) {
        this.mInflater = LayoutInflater.from(_context);
        this.services = passengers;
        this.context = _context;
    }

    public void setValue(Passengers value) {
        if (services==null){
            services= new ArrayList<>();
        }
        services.add(value);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_passenger, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = services.get(position).getFirstName() + " " + services.get(position).getLastName();
        if (services.get(position).getGender().equals("Male")) {
            name = "آقای " + name;
        } else {
            name = "خانوم " + name;
        }
        holder.myTextView.setText(name);
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
