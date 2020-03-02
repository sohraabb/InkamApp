package com.fara.inkamapp.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fara.inkamapp.Activities.PhoneDebtPayment;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class InquiryDebt extends Dialog implements View.OnClickListener{

    private Activity activity;
    private Button nextStep;
    private RelativeLayout midView, endView;
    private TextView midValue, endValue;


    public InquiryDebt(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_inqury_debt);


        endValue = findViewById(R.id.tv_end_of_term_value);
        endView = findViewById(R.id.rl_end_term);
        midValue = findViewById(R.id.tv_mid_term_value);
        midView = findViewById(R.id.rl_mid_term);
        nextStep = findViewById(R.id.btn_continue);

        nextStep.setOnClickListener(this);
        midView.setOnClickListener(this);
        endView.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_mid_term:
                midView.setBackgroundResource(R.drawable.light_rounded_green_stroke);
                endView.setBackgroundResource(R.drawable.light_rounded_background);

                break;

            case R.id.rl_end_term:
                midView.setBackgroundResource(R.drawable.light_rounded_background);
                endView.setBackgroundResource(R.drawable.light_rounded_green_stroke);

                break;

            case R.id.btn_continue:
                Intent intent = new Intent(activity, PhoneDebtPayment.class);
                activity.startActivity(intent);
                dismiss();

                break;
        }
    }
}

