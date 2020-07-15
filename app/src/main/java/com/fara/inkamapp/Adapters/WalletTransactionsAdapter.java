package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Helpers.JalaliCalendar;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.Report;
import com.fara.inkamapp.R;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class WalletTransactionsAdapter extends RecyclerView.Adapter<WalletTransactionsAdapter.ViewHolder> {

    private ArrayList<Report> reportList;
    private LayoutInflater mInflater;

    private Context context;

    public WalletTransactionsAdapter(Context context, ArrayList<Report> reportList) {
        this.context = context;
        this.reportList = reportList;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            View view = mInflater.inflate(R.layout.adapter_wallet_transactions, parent, false);
            context = parent.getContext();

            return new ViewHolder(view);
        }catch (Exception e){
            String s=e.toString();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(reportList.get(position).get_amount());

        holder.tv_transaction_type.setText(reportList.get(position).get_description());


        if (reportList.get(position).get_type().equals("واریز")) {
            holder.tv_credit_type.setText(R.string.increase_credit);
            holder.tv_credit_type.setTextColor(context.getResources().getColor(R.color.colorMainGreen));
            holder.tv_price.setText("+"+Numbers.ToPersianNumbers(formattedNumber));
            holder.tv_price.setTextColor(context.getResources().getColor(R.color.colorMainGreen));
            holder.tv_rial.setTextColor(context.getResources().getColor(R.color.colorMainGreen));
        } else {
            holder.tv_credit_type.setText(R.string.decrease_credit);
            holder.tv_credit_type.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.tv_price.setText("-"+Numbers.ToPersianNumbers(formattedNumber));
            holder.tv_price.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.tv_rial.setTextColor(context.getResources().getColor(R.color.colorRed));

        }

        holder.tv_bank_code.setText(Numbers.ToPersianNumbers(reportList.get(position).get_transactionNumber().replace("anyType{}","")));

        String[] date = reportList.get(position).get_dateTime().split("T")[0].split("-");
        JalaliCalendar j = new JalaliCalendar();
        //Date d= j.getJalaliDate( fine.get_dateTime().split(" ")[0]));
        int d = Integer.parseInt(date[2]);
        int m = Integer.parseInt(date[1]);
        int y = Integer.parseInt(date[0]);

        PersianCalendar p = new PersianCalendar();

        p.set(y, m - 1, d);
        String currentPersianDate = p.getPersianShortDate();
        holder.tv_time.setText(Numbers.ToPersianNumbers(reportList.get(position).get_dateTime().split("T")[1].substring(0, 5)));
        holder.tv_date.setText(Numbers.ToPersianNumbers(currentPersianDate));
        holder.tv_status.setText(reportList.get(position).get_statusName().replace("anyType{}",""));
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_transaction_type, tv_price, tv_credit_type, tv_date, tv_time, tv_bank_code,tv_rial,tv_status;

        private ImageView profilePic;

        ViewHolder(View itemView) {
            super(itemView);

            tv_transaction_type = itemView.findViewById(R.id.tv_transaction_type);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_credit_type = itemView.findViewById(R.id.tv_credit_type);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_bank_code = itemView.findViewById(R.id.tv_bank_code);
            tv_rial=itemView.findViewById(R.id.tv_rial);
            tv_status=itemView.findViewById(R.id.tv_status);

        }


    }
}
