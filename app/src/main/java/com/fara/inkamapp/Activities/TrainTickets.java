package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fara.inkamapp.R;

public class TrainTickets extends AppCompatActivity implements View.OnClickListener {

    private TextView oneWay, twoWays;
    private Button search;
    private EditText leavingDate, returnDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_tickets);

        oneWay = findViewById(R.id.tv_one_way);
        twoWays = findViewById(R.id.tv_back_forth);
        search = findViewById(R.id.btn_search);
        leavingDate = findViewById(R.id.et_date_leave);
        returnDate = findViewById(R.id.et_date_return);

        oneWay.setOnClickListener(this);
        twoWays.setOnClickListener(this);
        search.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_one_way:
                oneWay.setBackgroundResource(R.drawable.green_rounded_background);
                oneWay.setTextColor(getResources().getColor(R.color.colorWhite));
                twoWays.setBackgroundResource(R.drawable.light_rounded_background);
                twoWays.setTextColor(getResources().getColor(R.color.colorBrown));

                returnDate.setVisibility(View.GONE);

                break;

            case R.id.tv_back_forth:
                twoWays.setBackgroundResource(R.drawable.green_rounded_background);
                twoWays.setTextColor(getResources().getColor(R.color.colorWhite));
                oneWay.setBackgroundResource(R.drawable.light_rounded_background);
                oneWay.setTextColor(getResources().getColor(R.color.colorBrown));

                returnDate.setVisibility(View.VISIBLE);

                break;

            case R.id.btn_search:
                Intent intent = new Intent(getApplicationContext(), TrainTicketInfo.class);
                startActivity(intent);
                break;

        }
    }
}
