package com.fara.inkamapp.BottomSheetFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Adapters.NetPackages;
import com.fara.inkamapp.Adapters.TransactionServicesType;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class RecentTransactions extends BottomSheetDialogFragment {

    String string;
    private TransactionServicesType transactionServicesAdapter;


    public static RecentTransactions newInstance(String string) {
        RecentTransactions recentTransactions = new RecentTransactions();
        Bundle args = new Bundle();
        args.putString("string", string);
        recentTransactions.setArguments(args);
        return recentTransactions;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        string = getArguments().getString("string");
        // bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_recent_transactions, container,
                false);

        // get the views and attach the listener

        // data to populate the RecyclerView with

        ArrayList<String> services = new ArrayList<>();
        services.add("همه موارد");
        services.add("خرید شاپرکی");
        services.add("خرید شارژ");
        services.add("خرید شارژ");
        services.add("خرید شارژ");

        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.rv_service_type);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        transactionServicesAdapter = new TransactionServicesType(getActivity(), services);
        transactionServicesAdapter.setClickListener(new TransactionServicesType.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                view.setBackgroundResource(R.drawable.green_stroke_background);
            }
        });
        recyclerView.setAdapter(transactionServicesAdapter);


        return view;

    }

}

