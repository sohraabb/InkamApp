package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fara.inkamapp.R;

public class Notifications extends AppCompatActivity implements View.OnClickListener {

    private TextView messages, notify;
    private RecyclerView notifLists, msgList;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notifLists = findViewById(R.id.rv_notifications);
        msgList = findViewById(R.id.rv_messages);
        back = findViewById(R.id.ib_back);


        messages = findViewById(R.id.tv_messages);
        messages.setOnClickListener(this);
        notify = findViewById(R.id.tv_notify);
        notify.setOnClickListener(this);
        back.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_notify:
                notify.setBackgroundResource(R.drawable.green_rounded_background);
                notify.setTextColor(getResources().getColor(R.color.colorWhite));

                messages.setBackgroundResource(R.drawable.light_rounded_background);
                messages.setTextColor(getResources().getColor(R.color.colorBrown));

                break;
            case R.id.tv_messages:
                messages.setBackgroundResource(R.drawable.green_rounded_background);
                messages.setTextColor(getResources().getColor(R.color.colorWhite));

                notify.setBackgroundResource(R.drawable.light_rounded_background);
                notify.setTextColor(getResources().getColor(R.color.colorBrown));

                break;

            case R.id.ib_back:
                onBackPressed();

                break;
        }
    }
}

