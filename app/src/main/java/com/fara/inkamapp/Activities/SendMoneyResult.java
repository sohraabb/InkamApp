package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.fara.inkamapp.Helpers.RelativeLayoutTouchListener;
import com.fara.inkamapp.R;

public class SendMoneyResult extends AppCompatActivity {

    private RelativeLayout chatLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money_result);

        chatLayout = findViewById(R.id.rl_chat);
        chatLayout.setOnTouchListener(new RelativeLayoutTouchListener(this));

    }
}
