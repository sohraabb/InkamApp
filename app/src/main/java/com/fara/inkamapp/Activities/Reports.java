package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fara.inkamapp.Helpers.HideKeyboard;
import com.fara.inkamapp.R;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class Reports extends HideKeyboard implements View.OnClickListener {

    private ImageButton back;
    private RelativeLayout getMoneyBack, sendMoneyBack, groupMoneyBack;
    private TextView getMoneyText, sendMoneyText, groupMoneyText;
    private EditText search;
    private RecyclerView reports;

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

        setContentView(R.layout.activity_reports);

        back = findViewById(R.id.ib_back);
        getMoneyBack = findViewById(R.id.rl_report_get_money);
        sendMoneyBack = findViewById(R.id.rl_report_send_money);
        groupMoneyBack = findViewById(R.id.rl_group_payment);
        groupMoneyText = findViewById(R.id.tv_report_group_payment);
        getMoneyText = findViewById(R.id.tv_report_receive_money);
        sendMoneyText = findViewById(R.id.tv_report_transfer_money);
        search = findViewById(R.id.et_search);
        reports = findViewById(R.id.rv_reports);

        getMoneyBack.setOnClickListener(this);
        sendMoneyBack.setOnClickListener(this);
        groupMoneyBack.setOnClickListener(this);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.parent));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_group_payment:
                sendMoneyBack.setBackgroundResource(R.drawable.light_rounded_background);
                sendMoneyText.setTextColor(getResources().getColor(R.color.colorBrown));

                getMoneyBack.setBackgroundResource(R.drawable.light_rounded_background);
                getMoneyText.setTextColor(getResources().getColor(R.color.colorBrown));

                groupMoneyBack.setBackgroundResource(R.drawable.green_rounded_background);
                groupMoneyText.setTextColor(getResources().getColor(R.color.colorWhite));


                break;

            case R.id.rl_report_get_money:
                groupMoneyBack.setBackgroundResource(R.drawable.light_rounded_background);
                groupMoneyText.setTextColor(getResources().getColor(R.color.colorBrown));

                sendMoneyBack.setBackgroundResource(R.drawable.light_rounded_background);
                sendMoneyText.setTextColor(getResources().getColor(R.color.colorBrown));

                getMoneyBack.setBackgroundResource(R.drawable.green_rounded_background);
                getMoneyText.setTextColor(getResources().getColor(R.color.colorWhite));

                break;

            case R.id.rl_report_send_money:
                groupMoneyBack.setBackgroundResource(R.drawable.light_rounded_background);
                groupMoneyText.setTextColor(getResources().getColor(R.color.colorBrown));

                getMoneyBack.setBackgroundResource(R.drawable.light_rounded_background);
                getMoneyText.setTextColor(getResources().getColor(R.color.colorBrown));

                sendMoneyBack.setBackgroundResource(R.drawable.green_rounded_background);
                sendMoneyText.setTextColor(getResources().getColor(R.color.colorWhite));

                break;

            default:
                break;


        }
    }
}
