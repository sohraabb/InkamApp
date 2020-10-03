package com.fara.inkamapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Models.AirplainModels.AirTravelers;
import com.fara.inkamapp.Models.Passengers;
import com.fara.inkamapp.R;

import java.util.ArrayList;
import java.util.List;

public class PassengersAdapter extends RecyclerView.Adapter<PassengersAdapter.ViewHolder> {
    private List<Passengers> services;
    private List<AirTravelers> traveler;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;


    public PassengersAdapter(Context _context, List<Passengers> passengers) {
        this.mInflater = LayoutInflater.from(_context);
        this.services = passengers;
        this.context = _context;
    }
    public PassengersAdapter(Activity _context, List<AirTravelers> passengers) {
        this.mInflater = LayoutInflater.from(_context);
        this.traveler = passengers;
        this.context = _context.getApplicationContext();
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
        String name="";
        if (services!=null) {
             name = services.get(position).getFirstName() + " " + services.get(position).getLastName();
            if (services.get(position).getGender().equals("Male")) {
                name = "آقای " + name;
            } else {
                name = "خانوم " + name;
            }
            holder.tvAge.setVisibility(View.INVISIBLE);
        }else {
            name = traveler.get(position).PersianPersonName.GivenName + " " + traveler.get(position).PersianPersonName.Surname;
            if (traveler.get(position).Gender.equals("Male")) {
                name = "آقای " + name;
            } else {
                name = "خانوم " + name;
            }
            holder.tvAge.setVisibility(View.VISIBLE);
            switch (traveler.get(position).PassengerType){
                case "1":
                    holder.tvAge.setText("بزرگسال");
                    break;
                case "2":
                    holder.tvAge.setText("کودک");
                    break;
                case "3":
                    holder.tvAge.setText("نوزاد");
                    break;
            }
        }
        holder.myTextView.setText(name);
    }

    @Override
    public int getItemCount() {
        return services!=null? services.size():traveler.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView myTextView,tvAge;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tv_name);
            tvAge = itemView.findViewById(R.id.tv_age);
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
