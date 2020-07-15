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

import androidx.appcompat.app.AppCompatActivity;

import com.fara.inkamapp.Activities.PhoneDebtPayment;
import com.fara.inkamapp.BottomSheetFragments.Payment;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class InquiryDebt extends Dialog implements View.OnClickListener {

    private Activity activity;
    private Button nextStep;
    private RelativeLayout midView, endView;
    private TextView midValue, endValue, finalTermTitle, midTermTitle;
    private String _midTerm, _midBillId, _midPaymentId, _finalTerm, _finalBillId, _finalpaymentId,
            amountToPayment, billIdToPayment, paymentIdToPayment, termToPayment, _phoneNumber;
    private BottomSheetDialogFragment bottomSheetDialogFragment;



    public InquiryDebt(Activity a, String midTerm, String midBillId, String midPaymentId, String finalTerm, String finalBillId, String finalPaymentId, String phoneNumber) {
        super(a);
        // TODO Auto-generated constructor stub
        this.activity = a;
        this._midTerm = midTerm;
        this._midBillId = midBillId;
        this._midPaymentId = midPaymentId;
        this._finalTerm = finalTerm;
        this._finalBillId = finalBillId;
        this._finalpaymentId = finalPaymentId;
        this._phoneNumber = phoneNumber;
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
        finalTermTitle = findViewById(R.id.tv_end_of_term_title);
        midTermTitle = findViewById(R.id.tv_mid_term_title);

        nextStep.setOnClickListener(this);
        midView.setOnClickListener(this);
        endView.setOnClickListener(this);

        if (_midTerm != null && _finalTerm != null) {
            endView.setVisibility(View.VISIBLE);
            midView.setVisibility(View.VISIBLE);
            endValue.setText(Numbers.ToPersianNumbers(_finalTerm));
            midValue.setText(Numbers.ToPersianNumbers(_midTerm));

        } else if (_midTerm != null) {
            midView.setVisibility(View.VISIBLE);
            endView.setVisibility(View.GONE);
            midValue.setText(Numbers.ToPersianNumbers(_midTerm));

        } else if (_finalTerm != null) {
            endView.setVisibility(View.VISIBLE);
            midView.setVisibility(View.GONE);
            endValue.setText(Numbers.ToPersianNumbers(_finalTerm));


        }


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_mid_term:
                midView.setBackgroundResource(R.drawable.light_rounded_green_stroke);
                endView.setBackgroundResource(R.drawable.light_rounded_background);
                nextStep.setBackground(getContext().getResources().getDrawable(R.drawable.button_background_green));
                nextStep.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                nextStep.setEnabled(true);
                amountToPayment = midValue.getText().toString();
                billIdToPayment = _midBillId;
                paymentIdToPayment = _midPaymentId;
                termToPayment = midTermTitle.getText().toString();

                break;

            case R.id.rl_end_term:
                midView.setBackgroundResource(R.drawable.light_rounded_background);
                endView.setBackgroundResource(R.drawable.light_rounded_green_stroke);
                nextStep.setBackground(getContext().getResources().getDrawable(R.drawable.button_background_green));
                nextStep.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                nextStep.setEnabled(true);
                amountToPayment = endValue.getText().toString();
                billIdToPayment = _finalBillId;
                paymentIdToPayment = _finalpaymentId;
                termToPayment = finalTermTitle.getText().toString();

                break;

            case R.id.btn_continue:
//                Intent intent = new Intent(activity, PhoneDebtPayment.class);
//                intent.putExtra("billAmount", amountToPayment);
//                intent.putExtra("billID", billIdToPayment);
//                intent.putExtra("paymentID", paymentIdToPayment);
//                intent.putExtra("phoneNumber", _phoneNumber);
//                intent.putExtra("termTitle", termToPayment);
//
//
//                activity.startActivity(intent);
//                dismiss();

                try {

                    Bundle bundle = new Bundle();
                    bundle.putString("billAmount", amountToPayment);
                    bundle.putString("billID", billIdToPayment);
                    bundle.putString("paymentID", paymentIdToPayment);
                    bundle.putString("phoneNumber", _phoneNumber);
                    bundle.putString("termTitle", termToPayment);
                    bundle.putInt("serviceType",1);


                    bottomSheetDialogFragment = Payment.newInstance("Bottom Sheet Payment Dialog");
                    bottomSheetDialogFragment.setArguments(bundle);
                    bottomSheetDialogFragment.show(((AppCompatActivity) activity).getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                    dismiss();

                } catch (Exception e) {
                    e.toString();
                }

                break;
        }
    }
}

