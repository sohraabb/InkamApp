package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Helpers.DateConverter;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.BusSummary;
import com.fara.inkamapp.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class BusSearchIinfoAdapter extends RecyclerView.Adapter<BusSearchIinfoAdapter.ViewHolder> {
    private List<BusSummary> services;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position) throws ParseException;
    }

    public BusSearchIinfoAdapter(Context _context, List<BusSummary> serviceTypes) {
        this.mInflater = LayoutInflater.from(_context);
        this.services = serviceTypes;
        this.context = _context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_bus_search_info, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.tvNameCompany.setText(services.get(position).getCompany());
            holder.tvBusName.setText(services.get(position).getType());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(services.get(position).getDepartureDate());
            TimeZone timeZone = TimeZone.getTimeZone("Asia/Tehran");
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK); //this format changeable
            dateFormatter.setTimeZone(timeZone);
            String tt = dateFormatter.format(value).split("T")[1].substring(0, 5);
            holder.tvTime.setText(Numbers.ToPersianNumbers(tt));
            holder.tvAvailableSeat.setText(Numbers.ToPersianNumbers(services.get(position).getAvailableSeats()));
            if (!services.get(position).getDescription().equals("anyType{}")) {
                holder.tvDescription.setText(services.get(position).getDescription());
            }
            float discount = services.get(position).getFinancial().getMaxApplicableDiscountPercentage();
            if (discount > 0 && holder.rvDiscount != null) {
                holder.rvDiscount.setVisibility(View.VISIBLE);
            }
            int price = services.get(position).getFinancial().getPrice();


            DecimalFormat sdd = new DecimalFormat("#,###");
            Double doubleNumber = Double.parseDouble(String.valueOf(services.get(position).getFinancial().getPrice()));
            Double doubleNumber2 = Double.parseDouble(String.valueOf(price - (price * discount / 100)));
            String format = sdd.format(doubleNumber);
            if (holder.tvDiscount != null)
                holder.tvDiscount.setText(Numbers.ToPersianNumbers(String.valueOf(format)));
            holder.tvPrice.setText(Numbers.ToPersianNumbers(sdd.format(doubleNumber2)));
        } catch (Exception e) {
            Log.e("BusSearchInfoAdapter:", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvNameCompany;
        private TextView tvBusName;
        private TextView tvTime;
        private TextView tvPrice;
        private TextView tvAvailableSeat;
        private TextView tvDescription;
        private TextView tvDiscount;
        private RelativeLayout rvDiscount;

        ViewHolder(View itemView) {
            super(itemView);
            tvNameCompany = itemView.findViewById(R.id.tv_bus_info_name);
            tvBusName = itemView.findViewById(R.id.tv_bus_info_type);
            tvTime = itemView.findViewById(R.id.tv_bus_info_time);
            tvPrice = itemView.findViewById(R.id.tv_search_train_price);
            tvAvailableSeat = itemView.findViewById(R.id.tv_seat_left_value);
            tvDescription = itemView.findViewById(R.id.tv_train_class_info);
            rvDiscount = itemView.findViewById(R.id.rl_discount);
            tvDiscount = itemView.findViewById(R.id.tv_price_discount);

            Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMobile.ttf");
            if (type != null) {
                tvNameCompany.setTypeface(type);
                tvBusName.setTypeface(type);
                tvTime.setTypeface(type);
                tvPrice.setTypeface(type);
                tvDescription.setTypeface(type);
                tvAvailableSeat.setTypeface(type);
                if (tvDiscount != null)
                    tvDiscount.setTypeface(type);
            }


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                try {
                    mClickListener.onItemClick(view, getAdapterPosition());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
