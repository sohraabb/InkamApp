package com.fara.inkamapp.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fara.inkamapp.R;

public class CodeReceiver extends Dialog {

    private Activity activity;
    private Button accept;
    private RelativeLayout bottomSheetDialogFragment;

    public CodeReceiver(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_code_receiver);
        accept = findViewById(R.id.btn_accept);


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


    }
}

