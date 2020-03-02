package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.fara.inkamapp.R;
import com.jyn.vcview.VerificationCodeView;

public class SmsVerification extends AppCompatActivity {

    private static final String LOG_TAG = "Agha_Sori";
    private VerificationCodeView smsCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification);

        smsCodeView = findViewById(R.id.et_sms_verification);

        smsCodeView.setmEtWidth(100);


        smsCodeView.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {
            @Override
            public void onTextChange(View view, String content) {

            }

            @Override
            public void onComplete(View view, String content) {
                Intent intent = new Intent(getApplicationContext(), LoginUsePassword.class);
                startActivity(intent);
            }
        });

    }

}
