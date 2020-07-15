package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Activities.InternetPackageActivity;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import java.util.List;
import java.util.Map;

public class NetPackages extends RecyclerView.Adapter<NetPackages.ViewHolder> {

    private Map mAnimals;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;
    private View selectedView;

    // data is passed into the constructor
    public NetPackages(Context context, Map animals) {

        this.mInflater = LayoutInflater.from(context);
        this.mAnimals = animals;
        mContext=context;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_packages, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NetPackages.ViewHolder holder, int position) {
        String animal =(String) mAnimals.get(String.valueOf(position));
        holder.myTextView.setText(animal.replace("_"," "));
    }

    // binds the data to the view and textview in each row
    // total number of rows
    @Override
    public int getItemCount() {
        return mAnimals.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tv_packages);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(selectedView !=null) {
                selectedView.setBackgroundResource(R.drawable.gray_rounded_background);
               TextView tv= (TextView) ((RelativeLayout)selectedView).getChildAt(0);
                tv.setTextColor(mContext.getResources().getColor(R.color.colorBrown));
            }
            view.setBackgroundResource(R.drawable.green_rounded_background);
            TextView tv= (TextView) ((RelativeLayout)view).getChildAt(0);
            tv.setTextColor( mContext.getResources().getColor(R.color.colorWhite));
            selectedView=view;
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return (String)mAnimals.get(id);
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