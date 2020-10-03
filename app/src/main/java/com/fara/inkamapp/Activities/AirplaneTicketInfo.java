package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.AirplainModels.AirReservationRequest;
import com.fara.inkamapp.Models.AirplainModels.LeaveOptions;
import com.fara.inkamapp.Models.AirplainModels.PricedItineraries;
import com.fara.inkamapp.R;
import com.google.gson.Gson;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.io.Serializable;
import java.text.DecimalFormat;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class AirplaneTicketInfo extends AppCompatActivity {

    private ImageButton back;
    private TextView tvTicketCurrentLocation, tvTicketDestination, tvTicketDay,
            tvTicketDate, tvTicketCurrentLocTime, tvTicketDestinationTime,
            tvAirplaneName, tvAirplaneType, tvFlightNumberValue, tvAirplaneTitleValue,
            tvClassIdValue, tvSpecialTreatmentValue, tvAdultPrice, tvChildPrice, tvBabyPrice;
    private PricedItineraries pricedItineraries;
    private String fromName, toName, date, MyClass;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/IRANSansMobile.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        setContentView(R.layout.activity_airplain_ticket_info);
        initVariables();


    }

    private void initVariables() {
        tvTicketCurrentLocation = findViewById(R.id.tv_ticket_current_location);
        tvTicketDestination = findViewById(R.id.tv_ticket_destination);
        tvTicketDay = findViewById(R.id.tv_ticket_day);
        tvTicketDate = findViewById(R.id.tv_ticket_date);
        tvTicketCurrentLocTime = findViewById(R.id.tv_ticket_current_loc_time);
        tvTicketDestinationTime = findViewById(R.id.tv_ticket_destination_time);
        tvAirplaneName = findViewById(R.id.tv_airplane_name);
        tvAirplaneType = findViewById(R.id.tv_airplane_type);
        tvFlightNumberValue = findViewById(R.id.tv_flight_number_value);
        tvAirplaneTitleValue = findViewById(R.id.tv_airplane_title_value);
        tvClassIdValue = findViewById(R.id.tv_class_id_value);
        tvSpecialTreatmentValue = findViewById(R.id.tv_special_treatment_value);
        tvAdultPrice = findViewById(R.id.tv_adult_price);
        tvChildPrice = findViewById(R.id.tv_child_price);
        tvBabyPrice = findViewById(R.id.tv_baby_price);
        back = findViewById(R.id.ib_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fromName = getIntent().getStringExtra("fromName");
        toName = getIntent().getStringExtra("toName");
        date = getIntent().getStringExtra("date");
        MyClass = getIntent().getStringExtra("MyClass");

        Gson g = new Gson();
        pricedItineraries = g.fromJson(MyClass, PricedItineraries.class);
        LeaveOptions option = pricedItineraries.airItinerary.leaveOptions.get(0);
        tvTicketCurrentLocation.setText(fromName);
        tvTicketDestination.setText(toName);
        tvAirplaneName.setText(option.flightSegments.get(0).operatingAirLine.name);
        tvFlightNumberValue.setText(option.flightSegments.get(0).operatingAirLine.code + option.flightSegments.get(0).flightNumber);
        tvClassIdValue.setText(option.flightSegments.get(0).bookingClassAvail.resBookDesigCode);
        tvSpecialTreatmentValue.setText(Numbers.ToPersianNumbers(option.flightSegments.get(0).departureAirport.terminal));

        DecimalFormat sdd = new DecimalFormat("#,###");
        double doubleNumber = Double.parseDouble(pricedItineraries.airItineraryPricingInfo.ptcFareBreakdowns.get(0).passengerFares.get(0).originalFare.amount);
        String format = sdd.format(doubleNumber);
        tvAdultPrice.setText(Numbers.ToPersianNumbers(format));
        doubleNumber = Double.parseDouble(pricedItineraries.airItineraryPricingInfo.ptcFareBreakdowns.get(1).passengerFares.get(0).originalFare.amount);
        format = sdd.format(doubleNumber);
        tvChildPrice.setText(Numbers.ToPersianNumbers(format));
        doubleNumber = Double.parseDouble(pricedItineraries.airItineraryPricingInfo.ptcFareBreakdowns.get(2).passengerFares.get(0).originalFare.amount);
        format = sdd.format(doubleNumber);
        tvBabyPrice.setText(Numbers.ToPersianNumbers(format));

        if (option.flightSegments.get(0).bookingClassAvail.cabinType.equals("Business")) {
            tvAirplaneType.setText("بیزنس");
        } else if (option.flightSegments.get(0).bookingClassAvail.cabinType.equals("Economy")) {
            tvAirplaneType.setText("اکونومی");
        }
        String[] splitStrDeparture = option.flightSegments.get(0).departureDate.split("T");
        tvTicketCurrentLocTime.setText(Numbers.ToPersianNumbers(splitStrDeparture[splitStrDeparture.length - 1].substring(0, 5)));
        String[] splitStrArrive = option.flightSegments.get(0).arrivalDate.split("T");
        tvTicketDestinationTime.setText(Numbers.ToPersianNumbers(splitStrArrive[splitStrArrive.length - 1].substring(0, 5)));

        String[] separateDate = date.split("T")[0].split("-");
        PersianCalendar j = new PersianCalendar();

        j.set(Integer.parseInt(separateDate[0]), Integer.parseInt(separateDate[1]) - 1, Integer.parseInt(separateDate[2]));
        String currentPersianDate = j.getPersianShortDate();
        tvTicketDate.setText(Numbers.ToPersianNumbers(currentPersianDate));
        tvTicketDay.setText(j.getPersianLongDate().split(" ")[0]);

    }

    public void selectTicketClick(View v) {
        Intent intent = new Intent(getApplicationContext(), AirplaneTicketSelectionProccess.class);

        Gson g = new Gson();
        intent.putExtra("req", g.toJson(pricedItineraries));
        intent.putExtra("toCity", toName);
        intent.putExtra("fromCity", fromName);

        startActivity(intent);

    }
}
