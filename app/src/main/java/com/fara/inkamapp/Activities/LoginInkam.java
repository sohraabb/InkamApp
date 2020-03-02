package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fara.inkamapp.R;

public class LoginInkam extends AppCompatActivity {

    private Button btn_continue;
    private EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_inkam);

        phoneNumber = findViewById(R.id.et_phone_number);
        btn_continue = findViewById(R.id.btn_continue);

        phoneNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length() < 10){
                    btn_continue.setEnabled(false);
                } else {
                    btn_continue.setEnabled(true);
                    btn_continue.setBackgroundResource(R.drawable.button_background_green);
                    btn_continue.setTextColor(getResources().getColor(R.color.colorWhite));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SmsVerification.class);
                startActivity(intent);
            }
        });
    }
}
