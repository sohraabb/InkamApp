package com.fara.inkamapp.BottomSheetFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Chat extends BottomSheetDialogFragment {

    String string;
    BottomSheetBehavior bottomSheetBehavior;

    public static Chat newInstance(String string) {
        Chat chat = new Chat();
        Bundle args = new Bundle();
        args.putString("string", string);
        chat.setArguments(args);
        return chat;
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

        View view = inflater.inflate(R.layout.bottom_sheet_chat, container,
                false);

        // get the views and attach the listener

//        bottomSheetBehavior = BottomSheetBehavior.from(view);
//        bottomSheetBehavior.setPeekHeight(200, true);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        return view;

    }
}
