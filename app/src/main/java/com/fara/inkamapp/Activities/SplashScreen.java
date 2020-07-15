package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.fara.inkamapp.Helpers.DateConverter;
import com.fara.inkamapp.Helpers.JalaliCalendar;
import com.fara.inkamapp.Helpers.JavaSource_Calendar;
import com.fara.inkamapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;

public class SplashScreen extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    //    private String currentDate, expirationDate;
    private GregorianCalendar currentDate, expirationDate;
    private boolean isExpired = true;

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

        setContentView(R.layout.activity_splash_screen);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String expDate = sharedPreferences.getString("expDate", null);
        if(expDate!=null) {
            SimpleDateFormat sdf3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            try {
                Date newDate = sdf3.parse(expDate);
                String daaaate = DateConverter.Format_Date(newDate);


                String[] str = daaaate.split("-");
                int year = Integer.parseInt(str[0].trim());
                int month = Integer.parseInt(str[1].trim());
                String[] str2 = str[2].split(" ");
                int day = Integer.parseInt(str2[0].trim());

                expirationDate = new GregorianCalendar(year, month, day); // midnight
                currentDate = new GregorianCalendar();

                isExpired = currentDate.after(expirationDate);


            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1200);
                    if (sharedPreferences.getBoolean("my_first_time", true)) {
                        //the app is being launched for first time, do something
                        Intent intent = new Intent(getApplicationContext(), IntroSliders.class);
                        startActivity(intent);
                        finish();

                    } else {

                        if (!isExpired) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), LoginInkam.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }


}
