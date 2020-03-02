package com.fara.inkamapp.BottomSheetFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Activities.SendMoneyComplete;
import com.fara.inkamapp.Adapters.NetPackages;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class RepeatTransaction extends BottomSheetDialogFragment {

    String string;

    public static RepeatTransaction newInstance(String string) {
        RepeatTransaction repeatTransaction = new RepeatTransaction();
        Bundle args = new Bundle();
        args.putString("string", string);
        repeatTransaction.setArguments(args);
        return repeatTransaction;
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

        View view = inflater.inflate(R.layout.bottom_sheet_repeat_transaction, container,
                false);

        // get the views and attach the listener



        return view;

    }

}