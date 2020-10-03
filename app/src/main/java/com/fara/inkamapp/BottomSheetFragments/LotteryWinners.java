package com.fara.inkamapp.BottomSheetFragments;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Activities.MainActivity;
import com.fara.inkamapp.Adapters.LotteryAdapter;
import com.fara.inkamapp.Adapters.WinnersAdapter;
import com.fara.inkamapp.Helpers.DateConverter;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LotteryWinners extends BottomSheetDialogFragment {

    String string;
    private TextView toastText;
    private String stringDate;
    private Date dateTime;
    private RecyclerView rvWinners;
    private WinnersAdapter winnersAdapter;


    public static LotteryWinners newInstance(String string) {
        LotteryWinners lotteryWinners = new LotteryWinners();
        Bundle args = new Bundle();
        args.putString("string", string);
        lotteryWinners.setArguments(args);
        return lotteryWinners;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        string = getArguments().getString("string");
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_lottery_winners, container,
                false);

        rvWinners = view.findViewById(R.id.rv_winners);

        if (getArguments() != null)
            stringDate = getArguments().getString("dateTime");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateTime = format.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        new GetWinners().execute();

        return view;

    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getActivity());
    }


    private class GetWinners extends AsyncTask<Void, Void, List<User>> {

        List<User> results = null;

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
        protected List<User> doInBackground(Void... params) {

            results = new Caller().GetWinnersByDate(MainActivity._userId, MainActivity._token, stringDate);

            return results;
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            //TODO we should add other items here too

            if (users != null) {

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                rvWinners.setLayoutManager(layoutManager);
                winnersAdapter = new WinnersAdapter(getActivity(), users);
                rvWinners.setAdapter(winnersAdapter);

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