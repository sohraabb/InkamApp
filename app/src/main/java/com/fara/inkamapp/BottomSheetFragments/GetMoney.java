package com.fara.inkamapp.BottomSheetFragments;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fara.inkamapp.Dialogs.SubmitShebaNumber;
import com.fara.inkamapp.Dialogs.SuccessTransfer;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class GetMoney extends BottomSheetDialogFragment {

    String string;
    private Button accept;


    public static GetMoney newInstance(String string) {
        GetMoney getMoneyDialog = new GetMoney();
        Bundle args = new Bundle();
        args.putString("string", string);
        getMoneyDialog.setArguments(args);
        return getMoneyDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        string = getArguments().getString("string");
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_get_money, container,
                false);

        // get the views and attach the listener

        accept = view.findViewById(R.id.btn_accept);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SuccessTransfer successTransfer = new SuccessTransfer(getActivity());
                successTransfer.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                successTransfer.show();

                dismiss();
            }
        });

        return view;

    }
}