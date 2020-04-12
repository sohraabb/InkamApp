package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fara.inkamapp.Dialogs.InquiryDebt;
import com.fara.inkamapp.Dialogs.SuccessTransfer;
import com.fara.inkamapp.R;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class PhoneDebt extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mobileView, telephoneView;
    private TextView mobileText, telephoneText, instanceText;
    private EditText mobile, telephone;
    private Button inquiry;
    private Activity _context;

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

        setContentView(R.layout.activity_phone_debt);

        mobileView = findViewById(R.id.rl_mobile);
        telephoneView = findViewById(R.id.rl_telephone);
        mobileText = findViewById(R.id.tv_mobile);
        telephoneText = findViewById(R.id.tv_telephone);
        instanceText = findViewById(R.id.tv_phone_instance);
        mobile = findViewById(R.id.et_enter_phone_number);
        telephone = findViewById(R.id.et_enter_telephone_number);
        inquiry = findViewById(R.id.btn_inquiry);

        inquiry.setOnClickListener(this);
        mobileView.setOnClickListener(this);
        telephoneView.setOnClickListener(this);
        instanceText.setText("۳۲۳۲۳۴۵ ۰۳۴");

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_mobile:
                mobileView.setBackgroundResource(R.drawable.green_rounded_background);
                mobileText.setTextColor(getResources().getColor(R.color.colorWhite));
                telephoneView.setBackgroundResource(R.drawable.light_rounded_background);
                telephoneText.setTextColor(getResources().getColor(R.color.colorBrown));
                instanceText.setText("۰۹۱۲۳۴۵۶۷۸۹");

                mobile.setVisibility(View.VISIBLE);
                telephone.setVisibility(View.INVISIBLE);

                break;

            case R.id.rl_telephone:

                telephoneView.setBackgroundResource(R.drawable.green_rounded_background);
                telephoneText.setTextColor(getResources().getColor(R.color.colorWhite));
                mobileView.setBackgroundResource(R.drawable.light_rounded_background);
                mobileText.setTextColor(getResources().getColor(R.color.colorBrown));
                instanceText.setText("۳۲۳۲۳۴۵ ۰۳۴");

                telephone.setVisibility(View.VISIBLE);
                mobile.setVisibility(View.INVISIBLE);

                break;

            case R.id.btn_inquiry:
                InquiryDebt inquiryDebt = new InquiryDebt(this);
                inquiryDebt.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                inquiryDebt.show();
        }
    }
}
