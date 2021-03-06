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
import com.fara.inkamapp.Activities.UsersList;
import com.fara.inkamapp.Adapters.AllUserCardsAdapter;
import com.fara.inkamapp.Adapters.UserDetailsAdapter;
import com.fara.inkamapp.BottomSheetFragments.NewID;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.PercentageCode;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;


public class Users extends Fragment {

    private TextView new_ID, toastText, tv_your_income_from_users, tv_users_number;
    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private UserDetailsAdapter mAdapter;
    private RecyclerView allUsersRecycler;
    private ScrollingPagerIndicator scrollingPagerIndicator;
    private RelativeLayout users;
    private int countUsers = 0;

    public Users() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        new_ID = view.findViewById(R.id.tv_id);
        allUsersRecycler = view.findViewById(R.id.rv_percentage_code);
        scrollingPagerIndicator = view.findViewById(R.id.indicator);
        tv_your_income_from_users = view.findViewById(R.id.tv_your_income_from_users);
        tv_users_number = view.findViewById(R.id.tv_users_number);
        users = view.findViewById(R.id.users_number);
        bottomSheetDialogFragment = NewID.newInstance("Bottom Sheet Dialog");

        new_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

            }
        });

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (countUsers > 0) {
                    Intent intent = new Intent(getActivity(), UsersList.class);
                    startActivity(intent);
                }
            }
        });


        new getAllPercentageCodes().execute();
        new getUserWallet().execute();
        return view;
    }

    public void RefreshID() {

        new getAllPercentageCodes().execute();
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getActivity());
    }

    private class getAllPercentageCodes extends AsyncTask<Void, Void, ArrayList<PercentageCode>> {

        ArrayList<PercentageCode> results = null;

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
        protected ArrayList<PercentageCode> doInBackground(Void... params) {
            results = new Caller().getAllPresentageCode(MainActivity._userId, MainActivity._token);

            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<PercentageCode> percentageCodes) {
            super.onPostExecute(percentageCodes);
            //TODO we should add other items here too


            if (percentageCodes != null) {

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                allUsersRecycler.setLayoutManager(layoutManager);
                mAdapter = new UserDetailsAdapter(getActivity(), percentageCodes);
                allUsersRecycler.setAdapter(mAdapter);

                scrollingPagerIndicator.attachToRecyclerView(allUsersRecycler, percentageCodes.size());

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
                String formattedNumber = formatter.format(Double.valueOf(userWallet.getIncome()));
                tv_your_income_from_users.setText(Numbers.ToPersianNumbers(String.valueOf(formattedNumber)));
                tv_users_number.setText(Numbers.ToPersianNumbers(String.valueOf(formatter.format(userWallet.getUserCount()))) + " نفر ");

                countUsers = userWallet.getUserCount();
                //tvIncom.setText(Numbers.ToPersianNumbers(String.valueOf(formatter.format(userWallet.get_income()))));

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

