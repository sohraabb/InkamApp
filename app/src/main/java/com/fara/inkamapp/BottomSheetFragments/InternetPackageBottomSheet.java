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

import com.fara.inkamapp.Activities.InternetPackageActivity;
import com.fara.inkamapp.Dialogs.SuccessTransfer;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class InternetPackageBottomSheet extends BottomSheetDialogFragment {

    String string;

    private Button next;

    public static InternetPackageBottomSheet newInstance(String string) {
        InternetPackageBottomSheet internetPackageBottomSheet = new InternetPackageBottomSheet();
        Bundle args = new Bundle();
        args.putString("string", string);
        internetPackageBottomSheet.setArguments(args);
        return internetPackageBottomSheet;
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

        View view = inflater.inflate(R.layout.bottom_sheet_internet_package, container,
                false);

        // get the views and attach the listener

        next = view.findViewById(R.id.btn_continue);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InternetPackageActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }
}

