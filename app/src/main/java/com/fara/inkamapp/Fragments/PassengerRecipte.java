package com.fara.inkamapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fara.inkamapp.Adapters.PassengersAdapter;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.BusSummary;
import com.fara.inkamapp.Models.Passengers;
import com.fara.inkamapp.R;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerRecipte#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerRecipte extends Fragment {


    private static BusSummary bus;
    private static List<Passengers> passengersList;
    private static String toCity;
    private static int passengerCount;

    public PassengerRecipte() {
        // Required empty public constructor
    }


    public static PassengerRecipte newInstance(BusSummary b, List<Passengers> passengers, String to, int passCount) {
        PassengerRecipte fragment = new PassengerRecipte();
        Bundle args = new Bundle();
        bus = b;
        toCity = to;
        passengersList = passengers;
        passengerCount = passCount;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_passenger_recipte, container, false);
        v.setRotationY(180);
        TextView tv_current_location = v.findViewById(R.id.tv_current_location);
        TextView tv_current_loc_time = v.findViewById(R.id.tv_current_loc_time);
        TextView tv_date = v.findViewById(R.id.tv_date);
        TextView tv_day = v.findViewById(R.id.tv_day);
        TextView tv_number_passengers = v.findViewById(R.id.tv_number_passengers);
        TextView tv_destination = v.findViewById(R.id.tv_destination);
        TextView tv_destination_time = v.findViewById(R.id.tv_destination_time);
        TextView tv_bus_type = v.findViewById(R.id.tv_bus_type);
        TextView tv_date1 = v.findViewById(R.id.tv_date1);
        TextView tv_day1 = v.findViewById(R.id.tv_day1);
        TextView tv_price = v.findViewById(R.id.tv_price);
        TextView tv_bus_company = v.findViewById(R.id.tv_bus_company);
        TextView tv_price_discount = v.findViewById(R.id.tv_price_discount);
        RelativeLayout rl_discount = v.findViewById(R.id.rl_discount);
        tv_bus_company.setText(bus.getCompany());
        tv_bus_type.setText(bus.getType());
        tv_current_location.setText(bus.getBoardingPoint().getCity());
        tv_destination.setText(toCity);
        String[] separateDate = bus.getDepartureDate().replace("T", " ").split(" ")[0].split("-");

        PersianCalendar j = new PersianCalendar();

        j.set(Integer.parseInt(separateDate[0]), Integer.parseInt(separateDate[1]) - 1, Integer.parseInt(separateDate[2]));
        String currentPersianDate = j.getPersianShortDate();
        tv_date.setText(Numbers.ToPersianNumbers(currentPersianDate));
        tv_day.setText(j.getPersianLongDate().split(" ")[0]);

        //
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        try {
            value = formatter.parse(bus.getDepartureDate());

            TimeZone timeZone = TimeZone.getTimeZone("Asia/Tehran");
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK); //this format changeable
            dateFormatter.setTimeZone(timeZone);
            String tt = dateFormatter.format(value);

            tv_date1.setText(Numbers.ToPersianNumbers(tt.split("T")[1].substring(0, 5)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_current_loc_time.setText("");

        tv_day1.setText("");

        tv_destination_time.setText("");
        tv_number_passengers.setText(Numbers.ToPersianNumbers(String.valueOf( passengerCount)) + " مسافر");
        if (bus.getFinancial().getMaxApplicableDiscountPercentage() > 0) {
            rl_discount.setVisibility(View.VISIBLE);
        }
        DecimalFormat sdd = new DecimalFormat("#,###");
        double amount = bus.getFinancial().getPrice()-(bus.getFinancial().getPrice() * bus.getFinancial().getMaxApplicableDiscountPercentage() / 100);

        amount = amount * passengerCount;

        Double doubleNumber = Double.parseDouble(String.valueOf(bus.getFinancial().getPrice() * passengerCount));
        tv_price_discount.setText(Numbers.ToPersianNumbers(sdd.format(doubleNumber)));

        String format = sdd.format(amount);

        tv_price.setText(Numbers.ToPersianNumbers(format));

        RecyclerView list = v.findViewById(R.id.rv_passengers);
        PassengersAdapter adapter = new PassengersAdapter(getContext(), passengersList);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);


        return v;

    }
}
