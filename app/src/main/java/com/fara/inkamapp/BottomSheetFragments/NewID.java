package com.fara.inkamapp.BottomSheetFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class NewID extends BottomSheetDialogFragment {

    String string;

    public static NewID newInstance(String string) {
        NewID newIdBottomSheet = new NewID();
        Bundle args = new Bundle();
        args.putString("string", string);
        newIdBottomSheet.setArguments(args);
        return newIdBottomSheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        string = getArguments().getString("string");
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(DialogFragment.STYLE_NO_FRAME,0);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_new_id, container,
                false);

        // get the views and attach the listener

        return view;

    }

}
