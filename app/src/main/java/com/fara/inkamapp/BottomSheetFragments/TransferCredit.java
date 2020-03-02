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

public class TransferCredit extends BottomSheetDialogFragment {

    String string;

    private Button transfer;

    public static TransferCredit newInstance(String string) {
        TransferCredit transferCredit = new TransferCredit();
        Bundle args = new Bundle();
        args.putString("string", string);
        transferCredit.setArguments(args);
        return transferCredit;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        string = getArguments().getString("string");
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_transfer_credit, container,
                false);

        // get the views and attach the listener

        transfer = view.findViewById(R.id.btn_transfer_credit);

        transfer.setOnClickListener(new View.OnClickListener() {
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