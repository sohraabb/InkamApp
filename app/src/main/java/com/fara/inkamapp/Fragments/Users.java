package com.fara.inkamapp.Fragments;

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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Adapters.AllUserCardsAdapter;
import com.fara.inkamapp.Adapters.UserDetailsAdapter;
import com.fara.inkamapp.BottomSheetFragments.NewID;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Models.PercentageCode;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class Users extends Fragment {

    private TextView new_ID, toastText;
    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private UserDetailsAdapter mAdapter;
    private RecyclerView allUsersRecycler;
    private ScrollingPagerIndicator scrollingPagerIndicator;

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

        bottomSheetDialogFragment = NewID.newInstance("Bottom Sheet Dialog");

        new_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

            }
        });

        new getAllPercentageCodes().execute();

        return view;
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
            results = new Caller().getAllPresentageCode("2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00");

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

                scrollingPagerIndicator.attachToRecyclerView(allUsersRecycler);

            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANYekanRegular.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }
    }

}

