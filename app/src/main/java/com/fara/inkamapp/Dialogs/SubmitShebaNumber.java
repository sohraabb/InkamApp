package com.fara.inkamapp.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.fara.inkamapp.BottomSheetFragments.AddExtraCredit;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SubmitShebaNumber extends Dialog implements
        android.view.View.OnClickListener {

    private Activity activity;
    private Button submit, no;
    private BottomSheetDialogFragment bottomSheetDialogFragment;


    public SubmitShebaNumber(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_submit_sheba);
        submit = findViewById(R.id.btn_submit);
        no = findViewById(R.id.btn_no);
        submit.setOnClickListener(this);
        no.setOnClickListener(this);

        bottomSheetDialogFragment = AddExtraCredit.newInstance("Bottom Sheet Dialog");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                dismiss();
                bottomSheetDialogFragment.show(((AppCompatActivity) activity).getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}