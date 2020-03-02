package com.fara.inkamapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.fara.inkamapp.BottomSheetFragments.GetMoney;
import com.fara.inkamapp.BottomSheetFragments.SendMoney;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.w3c.dom.Text;

public class PaymentServices extends Fragment {

    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private TextView getMoney, sendMoney;

    public PaymentServices() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_services, container, false);

        getMoney = view.findViewById(R.id.tv_get_money);
        sendMoney = view.findViewById(R.id.tv_send_money);



        getMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogFragment = GetMoney.newInstance("Bottom Sheet Get Money Dialog");
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(),bottomSheetDialogFragment.getTag());

            }
        });

        sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogFragment = SendMoney.newInstance("Bottom Sheet Send Money Dialog");
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(),bottomSheetDialogFragment.getTag());
            }
        });



        return view;
    }



}
