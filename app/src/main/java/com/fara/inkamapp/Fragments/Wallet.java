package com.fara.inkamapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.fara.inkamapp.Activities.WalletTransactions;
import com.fara.inkamapp.BottomSheetFragments.SubmitNewCard;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class Wallet extends Fragment {

    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private TextView transactions, addCard;

    public Wallet() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        addCard = view.findViewById(R.id.tv_add_card);
        transactions = view.findViewById(R.id.tv_transactions);

        bottomSheetDialogFragment = SubmitNewCard.newInstance("Bottom Sheet Dialog");


        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

            }
        });

        transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WalletTransactions.class);
                startActivity(intent);
            }
        });


        return view;
    }


}