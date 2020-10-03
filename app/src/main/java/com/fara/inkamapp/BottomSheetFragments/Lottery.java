package com.fara.inkamapp.BottomSheetFragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.fara.inkamapp.Activities.LotteryHistory;
import com.fara.inkamapp.Activities.MainActivity;
import com.fara.inkamapp.Activities.PhoneDebt;
import com.fara.inkamapp.Dialogs.IncreaseChances;
import com.fara.inkamapp.Dialogs.InquiryDebt;
import com.fara.inkamapp.Dialogs.YourChances;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.Fund;
import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Lottery extends BottomSheetDialogFragment {

    String string;
    private TextView toastText, balance, allChances, yourChances, increaseChances;
    private ImageButton history;
    private RelativeLayout userChances;


    public static Lottery newInstance(String string) {
        Lottery lotteryBottomSheet = new Lottery();
        Bundle args = new Bundle();
        args.putString("string", string);
        lotteryBottomSheet.setArguments(args);
        return lotteryBottomSheet;
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

        View view = inflater.inflate(R.layout.bottom_sheet_lottery, container,
                false);

        history = view.findViewById(R.id.ib_history);
        balance = view.findViewById(R.id.tv_amount);
        allChances = view.findViewById(R.id.tv_chances_value);
        yourChances = view.findViewById(R.id.tv_your_chances_value);
        increaseChances = view.findViewById(R.id.tv_how_to_chance);
        userChances = view.findViewById(R.id.rl_your_chances);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LotteryHistory.class);
                startActivity(intent);
            }
        });

        increaseChances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IncreaseChances increaseChances = new IncreaseChances(getActivity());
                increaseChances.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                increaseChances.show();
                Window window = increaseChances.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        userChances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YourChances userChances = new YourChances(getActivity());
                userChances.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                userChances.show();
                Window window = userChances.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        new GetAllFundReports().execute();

        return view;

    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getActivity());
    }


    private class GetAllFundReports extends AsyncTask<Void, Void, Fund> {

        Fund results = null;

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
        protected Fund doInBackground(Void... params) {

            results = new Caller().GetFundReport(MainActivity._userId, MainActivity._token);

            return results;
        }

        @Override
        protected void onPostExecute(Fund fundReport) {
            super.onPostExecute(fundReport);
            //TODO we should add other items here too

            if (fundReport != null) {
                balance.setText(Numbers.ToPersianNumbers(String.valueOf(fundReport.get_balance())));
                allChances.setText(Numbers.ToPersianNumbers(String.valueOf(fundReport.get_totalChanceCount())));
                yourChances.setText(Numbers.ToPersianNumbers(String.valueOf(fundReport.get_userChanceCount())));

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
