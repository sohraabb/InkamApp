package com.fara.inkamapp.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.toolbox.ImageLoader;
import com.fara.inkamapp.Activities.BusServices;
import com.fara.inkamapp.Activities.MainActivity;
import com.fara.inkamapp.Helpers.CarouselLinearLayout;
import com.fara.inkamapp.Helpers.DateConverter;
import com.fara.inkamapp.Helpers.JalaliCalendar;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.R;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianDateParser;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class ItemFragment extends Fragment {

    private static final String POSITON = "position";
    private static final String SCALE = "scale";
    private static final String DRAWABLE_RESOURE = "resource";
    private static final String DATE = "";
    private int screenWidth;
    private int screenHeight;
    public static TextView selectedDay, selectedDate;
    private static List<String> dateList;
    SliderItemClickListener mListener;

    public interface SliderItemClickListener {
        public void onItemClicked(String date, TextView dayOfWeek, TextView date1);
    }

    public void setSliderItemClickListener(SliderItemClickListener listener) {
        mListener = listener;
    }

    public static Fragment newInstance(Context context, int pos, float scale, String currentDate) throws ParseException {
        Bundle b = new Bundle();
        b.putInt(POSITON, pos);
        b.putFloat(SCALE, scale);

        dateList = new ArrayList<>();

        Dictionary<String, String> dic = new Hashtable<>();
        //  dateList.add(new PersianCalendar().getPersianLongDate());
        PersianCalendar c = new PersianCalendar();
        String currentShamsi = DateConverter.Convert_Miladi_To_Shamsi_Date_ByTime(currentDate).split(" ")[0];

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayMiladi = format.format(new Date());
        String[] separateDate = currentDate.split(" ")[0].split("-");
        String[] separateShamsi = currentShamsi.split("/");
        PersianCalendar j = new PersianCalendar();

        j.set(Integer.parseInt(separateDate[0]), Integer.parseInt(separateDate[1]) - 1, Integer.parseInt(separateDate[2]));
        String currentPersianDate = j.getPersianLongDate();
        for (int i = 1; i < 60; i++) {

            j.add(PersianCalendar.DATE, 1);

            dateList.add(j.getPersianLongDate());
        }
        Collections.reverse(dateList);
        List<String> newList = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        String time = format.format(cal.getTime());

        while (!time.equals(currentDate.split(" ")[0])) {

            String newTime = format.format(cal.getTime()) + " 00:00:00";

            String[] d1 = DateConverter.Convert_Miladi_To_Shamsi_Date_ByTime(newTime).split(" ")[0].split("/");
            PersianCalendar pc = new PersianCalendar();
            pc.setPersianDate(Integer.parseInt(d1[0]), Integer.parseInt(d1[1]) - 1, Integer.parseInt(d1[2]));

            newList.add(0, pc.getPersianLongDate());

            cal.add(Calendar.DATE, 1);
            time = format.format(cal.getTime());
        }
        for (int i = 0; i < newList.size(); i++) {
            dateList.remove(i);
        }


        dateList.addAll(0, newList);
        dateList.add(0, currentPersianDate);
        return Fragment.instantiate(context, ItemFragment.class.getName(), b);
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        mListener = (SliderItemClickListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWidthAndHeight();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null || dateList == null) {
            return null;
        }

        final int postion = this.getArguments().getInt(POSITON);
        float scale = this.getArguments().getFloat(SCALE);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth / 2, screenHeight / 2);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_image, container, false);

        //TextView textView = (TextView) linearLayout.findViewById(R.id.text);
        CarouselLinearLayout root = linearLayout.findViewById(R.id.root_container);
        /*
         ImageView imageView = (ImageView) linearLayout.findViewById(R.id.pagerImg);
        TextView tvPrice = (TextView) linearLayout.findViewById(R.id.tv_Price_of_date);
        */
        final TextView tvDayOfWeek = linearLayout.findViewById(R.id.tv_day_of_week);
        final TextView tvDay = linearLayout.findViewById(R.id.tv_day1);
        LinearLayout llCarousel = linearLayout.findViewById(R.id.ll_carouser);
        //  textView.setText("Carousel item: " + postion);
        llCarousel.setLayoutParams(layoutParams);
if (postion==0){

    tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMainGreen));
    tvDayOfWeek.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMainGreen));
    selectedDay=tvDayOfWeek;
    selectedDate=tvDay;
}

        // imageView.setImageResource(imageArray[postion]);
        String[] s = dateList.get(postion).split(" ");
        tvDayOfWeek.setText(s[0]);
        String month = "";
        switch (s[4]) {
            case "فروردین":
                month = "1";
                break;
            case "اردیبهشت":
                month = "2";
                break;
            case "خرداد":
                month = "3";
                break;
            case "تیر":
                month = "4";
                break;
            case "مرداد":
                month = "5";
                break;
            case "شهریور":
                month = "6";
                break;
            case "مهر":
                month = "7";
                break;
            case "آبان":
                month = "8";
                break;
            case "آذر":
                month = "9";
                break;
            case "دی":
                month = "10";
                break;
            case "بهمن":
                month = "11";
                break;
            case "اسفند":
                month = "12";
                break;
        }
        tvDay.setText(Numbers.ToPersianNumbers( month + "/" + s[2]));
        final String currentDate = s[6] + "/" + month + "/" + s[2];//+" 00:00:00";

        //handling click event
        llCarousel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    if (selectedDay!=null) {
                        selectedDate.setTextColor(Color.BLACK);
                        selectedDay.setTextColor(Color.BLACK);
                    }

                    tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMainGreen));
                    tvDayOfWeek.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMainGreen));

                    Date d = new JalaliCalendar().getGregorianDate(currentDate);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String date = format.format(d);
                    //String date= DateConverter.Convert_Shamsi_To_Miladi_Date(currentDate,0,0);
                    mListener.onItemClicked(date + "T00:00:00",tvDayOfWeek,tvDay);
                }
            }
        });

        root.setScaleBoth(scale);

        return linearLayout;
    }

    /**
     * Get device screen width and height
     */
    private void getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
    }
}