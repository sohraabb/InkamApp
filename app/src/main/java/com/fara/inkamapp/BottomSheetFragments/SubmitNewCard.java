package com.fara.inkamapp.BottomSheetFragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fara.inkamapp.Activities.InternetPackageActivity;
import com.fara.inkamapp.Dialogs.SubmitShebaNumber;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.FourDigitCardFormatWatcher;
import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.Models.UserCard;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SubmitNewCard extends BottomSheetDialogFragment {

    String string;
    private EditText firstEditText;
    private Button submit;
    private String myPreviousText;
    private TextView toastText;
    private JSONObject postData;


    public static SubmitNewCard newInstance(String string) {
        SubmitNewCard submitNewCard = new SubmitNewCard();
        Bundle args = new Bundle();
        args.putString("string", string);
        submitNewCard.setArguments(args);
        return submitNewCard;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        string = getArguments().getString("string");
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_submit_card, container,
                false);

        // get the views and attach the listener

        firstEditText = view.findViewById(R.id.et_first_num);
//        secondEditText = view.findViewById(R.id.et_second_num);
//        thirdEditText = view.findViewById(R.id.et_third_num);
//        fourthEditText = view.findViewById(R.id.et_fourth_num);
        submit = view.findViewById(R.id.btn_submit_card);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                postData = new JSONObject();
                try {

//                    postData.put("CardNumber",RSA.encrypt("6219861029511317"));
                    postData.put("BankId", "a6ad1bcd-cb42-4105-969f-1cd8ae69e7a1");
//                    postData.put("ExpDate", RSA.encrypt("0401"));
//                    postData.put("IBan", "IR350560086180001619619001");
                    postData.put("IsDefault", "true");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new insertUserCard().execute();

//                SubmitShebaNumber submitShebaNumber = new SubmitShebaNumber(getActivity());
//                submitShebaNumber.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                submitShebaNumber.show();
//
//                dismiss();
            }
        });

        firstEditText.addTextChangedListener(new FourDigitCardFormatWatcher(firstEditText));

        return view;

    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getActivity());
    }

    private class insertUserCard extends AsyncTask<Void, Void, ResponseStatus> {

        ResponseStatus results = null;

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
        protected ResponseStatus doInBackground(Void... params) {
            String jsonToInsert = postData.toString().replace("\\n","");
            String cardData = jsonToInsert.replace("\\","");
            results = new Caller().updateUserCard("2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00", cardData);

            return results;
        }

        @Override
        protected void onPostExecute(ResponseStatus responseStatus) {
            super.onPostExecute(responseStatus);
            //TODO we should add other items here too

            if (responseStatus != null) {
                if (responseStatus.get_status().equals("SUCCESS")) {

                } else {

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


}

