package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fara.inkamapp.R;

public class SendMoneyComplete extends AppCompatActivity {

    private Button sendMoneyDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money_complete);

        sendMoneyDone = findViewById(R.id.btn_send_money);
        sendMoneyDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SendMoneyResult.class);
                startActivity(intent);
            }
        });

    }
}
