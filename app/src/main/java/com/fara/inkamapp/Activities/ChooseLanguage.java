package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fara.inkamapp.R;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class ChooseLanguage extends AppCompatActivity {

    private TextView farsiLang, kurdiLang, turkeyLang;
    private Button btn_select;

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

        setContentView(R.layout.activity_choose_language);

        farsiLang = findViewById(R.id.tv_lang_farsi);
        kurdiLang = findViewById(R.id.tv_lang_kurdi);
        turkeyLang = findViewById(R.id.tv_lang_turkey);
        btn_select = findViewById(R.id.btn_select_lang);


        kurdiLang.setOnClickListener(view -> {
            farsiLang.setTextColor(getResources().getColor(R.color.colorNightRider));
            kurdiLang.setTextColor(getResources().getColor(R.color.colorMainGreen));
            turkeyLang.setTextColor(getResources().getColor(R.color.colorNightRider));

        });

        farsiLang.setOnClickListener(view -> {
            farsiLang.setTextColor(getResources().getColor(R.color.colorMainGreen));
            kurdiLang.setTextColor(getResources().getColor(R.color.colorNightRider));
            turkeyLang.setTextColor(getResources().getColor(R.color.colorNightRider));

        });

        turkeyLang.setOnClickListener(view -> {
            farsiLang.setTextColor(getResources().getColor(R.color.colorNightRider));
            kurdiLang.setTextColor(getResources().getColor(R.color.colorNightRider));
            turkeyLang.setTextColor(getResources().getColor(R.color.colorMainGreen));

        });

        btn_select.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), IntroSliders.class);
            startActivity(intent);
        });

    }
}
