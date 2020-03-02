package com.fara.inkamapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.fara.inkamapp.BottomSheetFragments.NewID;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Users extends Fragment {

    private TextView new_ID;
    private BottomSheetDialogFragment bottomSheetDialogFragment;

    public Users() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        new_ID = view.findViewById(R.id.tv_id);

        bottomSheetDialogFragment = NewID.newInstance("Bottom Sheet Dialog");

        new_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

            }
        });

        return view;
    }


}

