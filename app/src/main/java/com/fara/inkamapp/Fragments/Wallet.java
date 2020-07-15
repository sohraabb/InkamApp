package com.fara.inkamapp.Fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Activities.MainActivity;
import com.fara.inkamapp.Activities.WalletTransactions;
import com.fara.inkamapp.Adapters.AllUserCardsAdapter;
import com.fara.inkamapp.BottomSheetFragments.AddExtraCredit;
import com.fara.inkamapp.BottomSheetFragments.DepositRequest;
import com.fara.inkamapp.BottomSheetFragments.SubmitNewCard;
import com.fara.inkamapp.BottomSheetFragments.TransferCredit;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.Models.UserCard;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;


/**
 * A simple {@link Fragment} subclass.
 */
public class Wallet extends Fragment {

    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private TextView transactions, addCard, toastText, walletBalance, tvPurchaseWalletCashValue, tvUserWalletCashValue;
    private RelativeLayout addCredit, transferCredit;
    private RecyclerView userCardsRecycler;
    private ScrollingPagerIndicator scrollingPagerIndicator;

    public Wallet() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);

        addCard = view.findViewById(R.id.tv_add_card);
        transactions = view.findViewById(R.id.tv_transactions);
        addCredit = view.findViewById(R.id.rl_add_credit);
        transferCredit = view.findViewById(R.id.rl_transfer_credit);
        userCardsRecycler = view.findViewById(R.id.rv_cards_slide);
        scrollingPagerIndicator = view.findViewById(R.id.indicator);
        walletBalance = view.findViewById(R.id.tv_wallet_cash_value);
        tvPurchaseWalletCashValue = view.findViewById(R.id.tv_purchase_wallet_cash_value);
        tvUserWalletCashValue = view.findViewById(R.id.tv_user_wallet_cash_value);
        RelativeLayout rl_request_cash_out_credit = view.findViewById(R.id.rl_request_cash_out_credit);


        new getAllUserCard().execute();
        new getUserWallet().execute();

        rl_request_cash_out_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogFragment = DepositRequest.newInstance("Bottom Sheet Get Money Dialog");
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogFragment = SubmitNewCard.newInstance("Bottom Sheet Dialog");
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

        addCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogFragment = AddExtraCredit.newInstance("Bottom Sheet Get Money Dialog");
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        transferCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogFragment = TransferCredit.newInstance("Bottom Sheet Get Money Dialog");
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        return view;
    }

    public void RefreshWallet() {

        new getUserWallet().execute();
    }

    public void RefreshCard() {

        new getAllUserCard().execute();
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getActivity());
    }

    private class getAllUserCard extends AsyncTask<Void, Void, ArrayList<UserCard>> {

        ArrayList<UserCard> results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_net, Toast.LENGTH_SHORT);
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

        @Override
        protected ArrayList<UserCard> doInBackground(Void... params) {

            results = new Caller().getAllUserCard(MainActivity._userId, MainActivity._token);

            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<UserCard> userCards) {
            super.onPostExecute(userCards);
            //TODO we should add other items here too

            if (userCards != null) {
                if (userCards.size() == 0) {
                    addCard.setVisibility(View.VISIBLE);
                    userCardsRecycler.setVisibility(View.GONE);
                    scrollingPagerIndicator.setVisibility(View.GONE);

                } else {
                    userCardsRecycler.setVisibility(View.VISIBLE);
                    scrollingPagerIndicator.setVisibility(View.VISIBLE);
                    addCard.setVisibility(View.GONE);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
                    userCardsRecycler.setLayoutManager(layoutManager);
                    AllUserCardsAdapter allUserCardsAdapter = new AllUserCardsAdapter(getActivity(), userCards);
                    userCardsRecycler.setAdapter(allUserCardsAdapter);

                    scrollingPagerIndicator.attachToRecyclerView(userCardsRecycler);
                    scrollingPagerIndicator.setCurrentPosition(0);

                }


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

    private class getUserWallet extends AsyncTask<Void, Void, User> {

        User results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_net, Toast.LENGTH_SHORT);
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

        @Override
        protected User doInBackground(Void... params) {

            results = new Caller().getUserById(MainActivity._userId, MainActivity._token);

            return results;
        }

        @Override
        protected void onPostExecute(User userWallet) {
            super.onPostExecute(userWallet);
            //TODO we should add other items here too


            if (userWallet != null) {
                NumberFormat formatter = new DecimalFormat("#,###");
                String formattedNumber = formatter.format(Double.valueOf(userWallet.getCashOfWallet()));
                walletBalance.setText(Numbers.ToPersianNumbers(String.valueOf(formattedNumber)));
                tvPurchaseWalletCashValue.setText(Numbers.ToPersianNumbers(String.valueOf(formatter.format(userWallet.getTodayProfit()))));
                tvUserWalletCashValue.setText(Numbers.ToPersianNumbers(String.valueOf(formatter.format(userWallet.getIncome()))));

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