package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fara.inkamapp.R;

public class BuyCharge extends AppCompatActivity {

    private RelativeLayout charge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_charge);

        charge = findViewById(R.id.rl_charge);

        charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BuyCharge2.class);
                startActivity(intent);
            }
        });
    }
}
