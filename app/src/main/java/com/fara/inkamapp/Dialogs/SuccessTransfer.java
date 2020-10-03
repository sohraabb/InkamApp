package com.fara.inkamapp.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fara.inkamapp.BottomSheetFragments.AddExtraCredit;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.w3c.dom.Text;

public class SuccessTransfer extends Dialog {

    private Activity activity;
    private Button accept;
    private RelativeLayout bottomSheetDialogFragment;
    private boolean variz;

    public SuccessTransfer(Activity a,boolean isVariz) {
        super(a);
        // TODO Auto-generated constructor stub
        variz=isVariz;
        this.activity = a;
    }

    public SuccessTransfer(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        variz=false;
        this.activity = a;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_success_transfer);
        accept = findViewById(R.id.btn_accept);
        TextView tv_message=findViewById(R.id.tv_message);
        if (variz){
            tv_message.setText(R.string.deposit_req_success);
        }

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


    }
}
