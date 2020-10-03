package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Helpers.DateConverter;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.NotificationList;
import com.fara.inkamapp.Models.Report;
import com.fara.inkamapp.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PurchasedAdapter extends RecyclerView.Adapter<PurchasedAdapter.ViewHolder> {

    private ArrayList<Report> reportList;
    private LayoutInflater mInflater;
    private ContactsAdapter.ItemClickListener mClickListener;
    private Context context;


    // data is passed into the constructor
    public PurchasedAdapter(Context _context, ArrayList<Report> _reportList) {
        this.mInflater = LayoutInflater.from(_context);
        this.reportList = _reportList;
        this.context = _context;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_purchased, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Report _reports = reportList.get(position);
        NumberFormat formatter = new DecimalFormat("#,###");

        if (_reports.get_statusName() != null && _reports.get_statusName().equals("موفق")) {
            holder.status.setText("خرید " + _reports.get_statusName());
            holder.status.setTextColor(context.getResources().getColor(R.color.colorGreen));
        } else {
            holder.status.setText("خرید " + _reports.get_statusName());
            holder.status.setTextColor(context.getResources().getColor(R.color.colorRed));
        }

        if (_reports.get_amount() != 0.0)
            holder.amount.setText(Numbers.ToPersianNumbers(formatter.format(_reports.get_amount())));

        if (_reports.get_persianDateTime() != null)
            holder.persianDate.setText(_reports.get_persianDateTime());

        if (_reports.get_dateTime() != null) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            SimpleDateFormat outputFormat =
                    new SimpleDateFormat("hh:mm");
            try {
                Date date = inputFormat.parse(String.valueOf(_reports.get_dateTime()));
                String outputText = outputFormat.format(date);
                holder.dateTime.setText(Numbers.ToPersianNumbers(outputText));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (_reports.get_mobile() != null && !_reports.get_mobile().equals("anyType{}"))
            holder.mobile.setText(Numbers.ToPersianNumbers(_reports.get_mobile()));

        if (_reports.get_internetMobile() != null && !_reports.get_internetMobile().equals("anyType{}"))
            holder.mobile.setText(Numbers.ToPersianNumbers(_reports.get_internetMobile()));

        if (_reports.get_refrenceNumber() != null)
            holder.referenceNum.setText(Numbers.ToPersianNumbers(_reports.get_refrenceNumber()));

        if (_reports.get_billID() != null && !_reports.get_billID().equals("anyType{}")) {
            holder.mobileTitle.setText(R.string.bills_id);
            holder.mobile.setText(_reports.get_billID());
        }

//        if (_reports.get_title() != null)
//            holder.title.setText(_notifications.get_title());
//
//        if (_reports.get_persianDateTIme() != null)
//            holder.dateTime.setText(_notifications.get_persianDateTIme());
//
//        if (_reports.get_message() != null)
//            holder.message.setText(_notifications.get_message());


    }

    @Override
    public int getItemCount() {
        return (null != reportList ? reportList.size() : 0);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mobile, dateTime, persianDate, amount, status, referenceNum,
                rial, chargeTitle, dateTitle, mobileTitle, referenceTitle;

        ViewHolder(View itemView) {
            super(itemView);
            persianDate = itemView.findViewById(R.id.tv_purchased_persian_date);
            dateTime = itemView.findViewById(R.id.tv_purchased_time);
            amount = itemView.findViewById(R.id.tv_purchased_amount);
            status = itemView.findViewById(R.id.tv_purchased_status_title);
            mobile = itemView.findViewById(R.id.tv_purchased_phone_number);
            referenceNum = itemView.findViewById(R.id.tv_reference_number);
            rial = itemView.findViewById(R.id.tv_rial);
            chargeTitle = itemView.findViewById(R.id.tv_purchased_topic_title);
            dateTitle = itemView.findViewById(R.id.tv_purchased_date_title);
            mobileTitle = itemView.findViewById(R.id.tv_purchased_phone_number_title);
            referenceTitle = itemView.findViewById(R.id.tv_reference_number_title);


            itemView.setOnClickListener(this);

            Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                    "fonts/IRANSansMobile.ttf");
            persianDate.setTypeface(typeface);
            dateTime.setTypeface(typeface);
            amount.setTypeface(typeface);
            status.setTypeface(typeface);
            mobile.setTypeface(typeface);
            referenceNum.setTypeface(typeface);
            rial.setTypeface(typeface);
            chargeTitle.setTypeface(typeface);
            dateTitle.setTypeface(typeface);
            mobileTitle.setTypeface(typeface);
            referenceTitle.setTypeface(typeface);


        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public Report getItem(int id) {
        return reportList.get(id);
    }

    public void setClickListener(ContactsAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}
