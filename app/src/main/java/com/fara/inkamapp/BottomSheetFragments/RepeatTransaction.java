package com.fara.inkamapp.BottomSheetFragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Activities.BusTickets;
import com.fara.inkamapp.Activities.BuyCharge;
import com.fara.inkamapp.Activities.MainActivity;
import com.fara.inkamapp.Activities.PhoneDebt;
import com.fara.inkamapp.Activities.SendMoneyComplete;
import com.fara.inkamapp.Activities.ServiceBillsAndCarFines;
import com.fara.inkamapp.Adapters.DashboardServiceAdapter;
import com.fara.inkamapp.Adapters.NetPackages;
import com.fara.inkamapp.Adapters.RepeatTransactionsAdapter;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.ProductAndService;
import com.fara.inkamapp.Models.RepeatPurchase;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class RepeatTransaction extends BottomSheetDialogFragment {

    String string;
    private RecyclerView mRecyclerView;
    private RepeatTransactionsAdapter mAdapter;
    private TextView toastText;
    private String amount, operator, phone, dataPlanType, chargeType;
    private Button accept;
    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private int serviceType;

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
        setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_repeat_transaction, container,
                false);

        // get the views and attach the listener

        mRecyclerView = view.findViewById(R.id.rv_repeat_transaction);
        accept = view.findViewById(R.id.btn_accept);


//        _type = "0";
        new GetRepeatPurchase().execute();

        return view;

    }

    private class GetRepeatPurchase extends AsyncTask<Void, Void, List<RepeatPurchase>> {

        RepeatPurchase results = null;
        List<RepeatPurchase> repeatPurchaseList = new ArrayList<>();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<RepeatPurchase> doInBackground(Void... params) {

            results = new Caller().getRepeatPurchase(MainActivity._userId, MainActivity._token, "0");
            repeatPurchaseList.add(results);
            return repeatPurchaseList;
        }

        @Override
        protected void onPostExecute(final List<RepeatPurchase> repeatPurchases) {
            super.onPostExecute(repeatPurchases);
            //TODO we should add other items here too


            if (repeatPurchases != null) {

                accept.setVisibility(View.VISIBLE);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                mAdapter = new RepeatTransactionsAdapter(getActivity(), repeatPurchases);
                mRecyclerView.setAdapter(mAdapter);

                mAdapter.setClickListener(new RepeatTransactionsAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        accept.setEnabled(true);
                        accept.setBackgroundResource(R.drawable.button_background_green);
                        accept.setTextColor(getResources().getColor(R.color.colorWhite));

                        serviceType = Integer.parseInt(repeatPurchases.get(position).get_type());
                        amount = String.valueOf(repeatPurchases.get(position).get_amount());
                        operator = String.valueOf(repeatPurchases.get(position).get_operator());
                        phone = repeatPurchases.get(position).get_mobile();
                        dataPlanType = String.valueOf(repeatPurchases.get(position).get_dataPlanType());
                        chargeType = String.valueOf(repeatPurchases.get(position).get_chargeType());

                        if (repeatPurchases.get(position).get_type().equals("0"))
                            serviceType = 0;
                        else
                            serviceType = 4;

                    }
                });


                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        if (serviceType == 0) {
                            bundle.putString("amount", amount);
                            bundle.putString("operator", operator);
                            bundle.putString("type", chargeType);
                            bundle.putString("dataPlanType", dataPlanType);
                            bundle.putString("phone", Numbers.ToEnglishNumbers(phone.replace(" ", "")));
                        } else {
                            bundle.putString("amountWithoutTax", amount);
                            bundle.putString("amountWithTax", amount);
                            bundle.putString("operator", operator);
                            bundle.putString("type", chargeType);
                            bundle.putString("dataPlanType", dataPlanType);
                            bundle.putString("phone", Numbers.ToEnglishNumbers(phone.replace(" ", "")));
                            bundle.putString("describe", "");
                        }
                        bundle.putInt("serviceType", serviceType);

                        bottomSheetDialogFragment = Payment.newInstance("Bottom Sheet Payment Dialog");
                        bottomSheetDialogFragment.setArguments(bundle);
                        bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

                    }
                });

            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }
        }
    }

}