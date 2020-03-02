package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fara.inkamapp.R;

public class CardToCardTransfer extends AppCompatActivity {

    private Button nextStep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_to_card_transfer);

        nextStep = findViewById(R.id.btn_next_step);

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CardToCardTransfer2.class);
                startActivity(intent);
            }
        });
    }
}
