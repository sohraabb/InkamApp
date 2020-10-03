package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fara.inkamapp.Helpers.HideKeyboard;
import com.fara.inkamapp.Helpers.RelativeLayoutTouchListener;
import com.fara.inkamapp.R;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class SendMoneyResult extends HideKeyboard {

    private RelativeLayout chatLayout;
    private TextView tv_title, tv_failed, tv_amount_title, tv_amount, tv_amount_rial,
            tv_consistency_number_title, tv_consistency_number, tv_date,
            tv_destination_card_title, tv_destination_card, tv_own_card_title, tv_own_card, tv_operator;
    private ImageView iv_failed;

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

        setContentView(R.layout.activity_send_money_result);
        tv_title = findViewById(R.id.tv_title);
        tv_failed = findViewById(R.id.tv_failed);
        tv_amount_title = findViewById(R.id.tv_amount_title);
        tv_amount = findViewById(R.id.tv_amount);
        tv_amount_rial = findViewById(R.id.tv_amount_rial);
        tv_consistency_number_title = findViewById(R.id.tv_consistency_number_title);
        tv_consistency_number = findViewById(R.id.tv_consistency_number);
        tv_date = findViewById(R.id.tv_date);
        tv_destination_card_title = findViewById(R.id.tv_destination_card_title);
        tv_destination_card = findViewById(R.id.tv_destination_card);
        tv_own_card_title = findViewById(R.id.tv_own_card_title);
        tv_own_card = findViewById(R.id.tv_own_card);
        iv_failed = findViewById(R.id.iv_failed);
        tv_operator = findViewById(R.id.tv_operator);
        String type = getIntent().getStringExtra("type");
        String operator = getIntent().getStringExtra("operator");
        boolean success = getIntent().getBooleanExtra("success", false);
        tv_amount.setText(getIntent().getStringExtra("amount"));

        if ("charge".equals(type)) {
            tv_title.setText(R.string.buy_charge);
            tv_amount_title.setText(R.string.charge);
            tv_operator.setText(operator);
            tv_amount_title.setText(R.string.charge);
            tv_amount_title.setText(R.string.charge);
            tv_amount_title.setText(R.string.charge);
            if (success) {
                tv_failed.setText(R.string.success_payment);
                tv_failed.setTextColor(getResources().getColor(R.color.colorGreen));
                iv_failed.setImageResource(R.drawable.ic_circular_check);
            }
        }
        chatLayout = findViewById(R.id.rl_chat);
        chatLayout.setOnTouchListener(new RelativeLayoutTouchListener(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.parent));
    }
}
