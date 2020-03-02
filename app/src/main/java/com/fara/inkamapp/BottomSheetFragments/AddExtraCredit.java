package com.fara.inkamapp.BottomSheetFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddExtraCredit extends BottomSheetDialogFragment {

    String string;
    private Button addCredit;
    private BottomSheetDialogFragment bottomSheetDialogFragment;


    public static AddExtraCredit newInstance(String string) {
        AddExtraCredit addExtraCredit = new AddExtraCredit();
        Bundle args = new Bundle();
        args.putString("string", string);
        addExtraCredit.setArguments(args);
        return addExtraCredit;
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

        View view = inflater.inflate(R.layout.bottom_sheet_extra_credit, container,
                false);

        // get the views and attach the listener

        addCredit = view.findViewById(R.id.btn_add_credit);

        bottomSheetDialogFragment = TransferCredit.newInstance("Bottom Sheet Dialog");


        addCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                dismiss();
            }
        });

        return view;

    }
}