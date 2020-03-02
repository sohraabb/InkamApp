package com.fara.inkamapp.BottomSheetFragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fara.inkamapp.Activities.SendMoneyComplete;
import com.fara.inkamapp.Dialogs.SuccessTransfer;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SendMoney extends BottomSheetDialogFragment {

    String string;
    private Button accept;


    public static SendMoney newInstance(String string) {
        SendMoney sendMoneyDialog = new SendMoney();
        Bundle args = new Bundle();
        args.putString("string", string);
        sendMoneyDialog.setArguments(args);
        return sendMoneyDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        string = getArguments().getString("string");
        // bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(DialogFragment.STYLE_NO_FRAME,0);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_send_money, container,
                false);

        // get the views and attach the listener

        accept = view.findViewById(R.id.btn_accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SendMoneyComplete.class);
                startActivity(intent);
            }
        });


        return view;

    }

}
