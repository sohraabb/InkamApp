package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.R;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class PhoneDebtPayment extends AppCompatActivity {

    private TextView tvPhone, tvTermTitle, tvTermAmount;
    private String _phone, _termTitle, _termAmount, _billId, _paymentId;

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

        setContentView(R.layout.activity_phone_debt_payment);
        tvPhone = findViewById(R.id.tv_phone_number);
        tvTermTitle = findViewById(R.id.tv_term_title);
        tvTermAmount = findViewById(R.id.tv_term_value);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            _phone = intent.getString("phoneNumber");
            _termTitle = intent.getString("termTitle");
            _termAmount = intent.getString("billAmount");
            _billId = intent.getString("billID");
            _paymentId = intent.getString("paymentID");

            tvPhone.setText(_phone);
            tvTermTitle.setText(_termTitle);
            tvTermAmount.setText(Numbers.ToPersianNumbers(_termAmount));
        }
    }
}
