package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Activities.CrimesList;
import com.fara.inkamapp.Helpers.DateConverter;
import com.fara.inkamapp.Helpers.JalaliCalendar;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.CarFinesParameters;
import com.fara.inkamapp.Models.TrafficFinesDetails;
import com.fara.inkamapp.R;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CrimeAdapter extends RecyclerView.Adapter<CrimeAdapter.ViewHolder> {
    private CarFinesParameters _CarFinesParameters;
    private LayoutInflater mInflater;

    private List<RelativeLayout> allView;
    private ItemClickListener mClickListener;
    private Context context;
    private static boolean getAll;

    public CrimeAdapter(Context _context, CarFinesParameters _CarFinesParameters) {
        this.mInflater = LayoutInflater.from(_context);
        this._CarFinesParameters = _CarFinesParameters;
        this.context = _context;
        allView = new ArrayList<>();

    }

    @Override
    public void onBindViewHolder(@NonNull CrimeAdapter.ViewHolder holder, int position) {
        try {
            TrafficFinesDetails fine = _CarFinesParameters.get_trafficFinesDetails().get(position);
            String[] date = fine.get_dateTime().split(" ")[0].split("/");
            JalaliCalendar j = new JalaliCalendar();
            //Date d= j.getJalaliDate( fine.get_dateTime().split(" ")[0]));
            int y = Integer.parseInt(date[2]);
            int d = Integer.parseInt(date[1]);
            int m = Integer.parseInt(date[0]);

            PersianCalendar p = new PersianCalendar();

            p.set(y, m - 1, d);
            String currentPersianDate = p.getPersianShortDate();
            holder.tv_crime_time.setText(Numbers.ToPersianNumbers(fine.get_dateTime().split(" ")[1].substring(0, 5)));
            holder.tv_crime_date.setText(Numbers.ToPersianNumbers(currentPersianDate));
            //  tvDay.setText(p.getPersianLongDate().split(" ")[0]);


            //holder.tv_crime_date.setText(d.toString());
            holder.tv_crime_type_value.setText(fine.get_delivery());
            holder.tv_crime_city.setText(fine.get_city());
            holder.tv_place_violation.setText(fine.get_location());
            holder.tv_violation_code.setText("");
            holder.tv_crime_serial.setText("");
            holder.tv_plate_number.setText(_CarFinesParameters.get_plateNumber());
            holder.tv_explain_violation.setText(fine.get_type());
            NumberFormat formatter = new DecimalFormat("#,###");
            String formattedNumber = formatter.format(Double.valueOf(fine.get_amount()));
            holder.tv_amount.setText(Numbers.ToPersianNumbers(formattedNumber));
        } catch (Exception e) {
            e.toString();
        }
    }


    @Override
    public int getItemCount() {
        return _CarFinesParameters.get_trafficFinesDetails().size();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_crime, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_crime_time, tv_crime_select, tv_crime_date, tv_crime_type_value, tv_crime_city, tv_place_violation, tv_violation_code, tv_crime_serial, tv_plate_number, tv_explain_violation, tv_amount;
        private RelativeLayout rl_crime_select;


        ViewHolder(View itemView) {
            super(itemView);
            try{
                tv_crime_type_value = itemView.findViewById(R.id.tv_crime_type_value);
                tv_crime_date = itemView.findViewById(R.id.tv_crime_date);
                tv_crime_city = itemView.findViewById(R.id.tv_crime_city);
                tv_place_violation = itemView.findViewById(R.id.tv_place_violation);
                tv_violation_code = itemView.findViewById(R.id.tv_violation_code);
                tv_crime_serial = itemView.findViewById(R.id.tv_crime_serial);

                tv_plate_number = itemView.findViewById(R.id.tv_plate_number);
                tv_explain_violation = itemView.findViewById(R.id.tv_explain_violation);
                rl_crime_select = itemView.findViewById(R.id.rl_crime_select);
                tv_amount = itemView.findViewById(R.id.tv_amount);
                tv_crime_select = itemView.findViewById(R.id.tv_crime_select);
                tv_crime_time = itemView.findViewById(R.id.tv_crime_time);

                rl_crime_select.setBackgroundResource(R.drawable.green_stroke_background);
                tv_crime_select.setTextColor(context.getResources().getColor(R.color.colorMainGreen));
                if (getAll) {
                    rl_crime_select.setBackgroundResource(R.drawable.green_rounded_background);
                    tv_crime_select.setTextColor(context.getResources().getColor(R.color.colorWhite));

                }
                allView.add(rl_crime_select);
                rl_crime_select.setOnClickListener(this);
            } catch (Exception e) {
                e.toString();
            }
        }

        @Override
        public void onClick(View view) {

            if (rl_crime_select.getTag()!=null&&(int)rl_crime_select.getTag()==1) {
                rl_crime_select.setBackgroundResource(R.drawable.green_stroke_background);
                tv_crime_select.setTextColor(context.getResources().getColor(R.color.colorMainGreen));
                rl_crime_select.setTag(-1);
            } else {
                rl_crime_select.setBackgroundResource(R.drawable.green_rounded_background);
                tv_crime_select.setTextColor(context.getResources().getColor(R.color.colorWhite));
                rl_crime_select.setTag(1);
            }
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void selectAll(Boolean all) {
        try {



            for (int i = 0; i < allView.size(); i++) {
                if (all) {
                    getAll=false;
                    allView.get(i).setTag(-1);
                    allView.get(i).setBackgroundResource(R.drawable.green_stroke_background);
                    ((TextView) allView.get(i).getChildAt(1)).setTextColor(context.getResources().getColor(R.color.colorMainGreen));

                }else {
                    getAll = true;
                    allView.get(i).setTag(1);
                    allView.get(i).setBackgroundResource(R.drawable.green_rounded_background);
                    ((TextView) allView.get(i).getChildAt(1)).setTextColor(context.getResources().getColor(R.color.colorWhite));
                }
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.toString();
        }
    }

}
