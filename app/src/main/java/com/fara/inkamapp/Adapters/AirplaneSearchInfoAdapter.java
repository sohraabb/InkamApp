package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.AirplainModels.LeaveOptions;
import com.fara.inkamapp.Models.AirplainModels.PricedItineraries;
import com.fara.inkamapp.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AirplaneSearchInfoAdapter extends RecyclerView.Adapter<AirplaneSearchInfoAdapter.ViewHolder> {
    private List<PricedItineraries> services;
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

    public AirplaneSearchInfoAdapter(Context _context, List<PricedItineraries> serviceTypes) {
        this.mInflater = LayoutInflater.from(_context);
        this.services = serviceTypes;
        this.context = _context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_airplain_search_info, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            LeaveOptions option = services.get(position).airItinerary.leaveOptions.get(0);
            holder.tv_air_plane_name.setText(option.flightSegments.get(0).operatingAirLine.name);
            String[] splitStrDeparture = option.flightSegments.get(0).departureDate.split("T");
            holder.tv_time_start.setText(Numbers.ToPersianNumbers( splitStrDeparture[splitStrDeparture.length - 1].substring(0, 5)));
            String[] splitStrArrive = option.flightSegments.get(0).arrivalDate.split("T");
            holder.tv_time_end.setText(Numbers.ToPersianNumbers( splitStrArrive[splitStrArrive.length - 1].substring(0, 5)));

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            // Date value = formatter.parse(services.get(position).getDepartureDate());
            TimeZone timeZone = TimeZone.getTimeZone("Asia/Tehran");
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK); //this format changeable
            dateFormatter.setTimeZone(timeZone);
            // String tt = dateFormatter.format(value).split("T")[1].substring(0, 5);
            // holder.tvTime.setText(Numbers.ToPersianNumbers(tt));
            holder.tv_seat_left_value.setText(Numbers.ToPersianNumbers( option.resBookDesigQuantity + " نفر "));

            if (option.isChartered) {
                holder.tv_air_plane_charter.setText("چارتر");
            } else {
                holder.tv_air_plane_charter.setText("سیستمی");
            }
            if (option.flightSegments.get(0).bookingClassAvail.cabinType.equals("Business")) {
                holder.tv_air_plane_economy.setText("بیزنس");
            } else if (option.flightSegments.get(0).bookingClassAvail.cabinType.equals("Economy")) {
                holder.tv_air_plane_economy.setText("اکونومی");
            }
            DecimalFormat sdd = new DecimalFormat("#,###");
            Double doubleNumber = Double.parseDouble(String.valueOf(services.get(position).airItineraryPricingInfo.ptcFareBreakdowns.get(0).passengerFares.get(0).originalFare.amount));
            //   Double doubleNumber2 = Double.parseDouble(String.valueOf( price-(price*discount/100)));
            String format = sdd.format(doubleNumber);
            holder.tv_search_train_price.setText(Numbers.ToPersianNumbers(String.valueOf(format)));

        } catch (Exception e) {
            Log.e("BusSearchInfoAdapter:", e.toString());

        }
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_air_plane_name, tv_air_plane_charter, tv_air_plane_economy, tv_time_start, tv_time_end, tv_search_train_price, tv_seat_left_value;

        private RelativeLayout rvDiscount;

        ViewHolder(View itemView) {
            super(itemView);
            tv_air_plane_name = itemView.findViewById(R.id.tv_air_plane_name);
            tv_air_plane_charter = itemView.findViewById(R.id.tv_air_plane_charter);
            tv_air_plane_economy = itemView.findViewById(R.id.tv_air_plane_economy);
            tv_time_start = itemView.findViewById(R.id.tv_time_start);
            tv_time_end = itemView.findViewById(R.id.tv_time_end);
            tv_search_train_price = itemView.findViewById(R.id.tv_search_train_price);
            tv_seat_left_value = itemView.findViewById(R.id.tv_seat_left_value);

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
