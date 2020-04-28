package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fara.inkamapp.Models.ProductAndService;
import com.fara.inkamapp.R;

import java.util.List;

public class DashboardServiceAdapter extends RecyclerView.Adapter<DashboardServiceAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private List<ProductAndService> productAndServiceList;

    // data is passed into the constructor
    public DashboardServiceAdapter(Context _context, List<ProductAndService> _data) {
        this.mInflater = LayoutInflater.from(_context);
        this.productAndServiceList = _data;
        this.context = _context;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_dashboard_service, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductAndService productAndService = productAndServiceList.get(position);

        if (productAndService.get_name() != null)
            holder.title.setText(productAndService.get_name());

//        RequestOptions defaultOption = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.drawable.pic_default)
//                .error(R.drawable.pic_default);

        if (productAndService.get_picURL() != null) {

//            Glide.with(context)
//                    .load(productAndService.get_picURL())
//                    .apply(defaultOption)
//                    .into(holder.icon);

        } else {

//            Glide.with(context)
//                    .load(productAndService.get_picURL())
//                    .apply(defaultOption)
//                    .into(holder.icon);
        }
    }


    // binds the data to the view and textview in each row
// total number of rows
    @Override
    public int getItemCount() {
        return (null != productAndServiceList ? productAndServiceList.size() : 0);

    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private ImageView icon;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            icon = itemView.findViewById(R.id.iv_icon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public ProductAndService getItem(int id) {
        return productAndServiceList.get(id);
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
