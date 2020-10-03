package com.fara.inkamapp.BottomSheetFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fara.inkamapp.Activities.InternetPackageActivity;
import com.fara.inkamapp.Dialogs.SubmitShebaNumber;
import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.FourDigitCardFormatWatcher;
import com.fara.inkamapp.Helpers.RSA;
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

import static com.fara.inkamapp.Activities.LoginInkam.publicKey;
import static com.fara.inkamapp.Activities.MainActivity.MyPREFERENCES;

public class SubmitNewCard extends BottomSheetDialogFragment {

    String string;
    private EditText et_card_number, et_date_month, et_date_year;
    private Button submit;
    private String myPreviousText, cardNo, expDate, userID, token;
    private TextView toastText;
    private JSONObject postData;
    private RelativeLayout card_background;
    private ImageView cardLogo;
    private SharedPreferences sharedpreferences;
    private int MAX_LENGTH = 2;

    private RefershCardListener mListener;

    public interface RefershCardListener {
        public void RefreshCard();
    }

    public void setRefershCardListener(RefershCardListener itemClickListener) {
        this.mListener = itemClickListener;
    }

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
        setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mListener = (RefershCardListener) context;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_submit_card, container,
                false);

        // get the views and attach the listener

        et_card_number = view.findViewById(R.id.et_card_number);
        submit = view.findViewById(R.id.btn_submit_card);
        card_background = view.findViewById(R.id.rl_add_card);
        cardLogo = view.findViewById(R.id.iv_bank);
        et_date_month = view.findViewById(R.id.et_card_month_date);
        et_date_year = view.findViewById(R.id.et_card_year_date);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        try {
            token = Base64.encode((RSA.encrypt(sharedpreferences.getString("Token", null), publicKey)));
            userID = sharedpreferences.getString("UserID", null);

        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        et_date_year.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == MAX_LENGTH) {
                    et_date_month.requestFocus();
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardNo = et_card_number.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", "");
                expDate = et_date_year.getText().toString() + et_date_month.getText().toString();

                postData = new JSONObject();
                try {

                    postData.put("CardNumber", Base64.encode(RSA.encrypt(cardNo, publicKey)));
                    postData.put("ExpDate", Base64.encode(RSA.encrypt(expDate, publicKey)));
                    postData.put("IsDefault", "true");


                } catch (JSONException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
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

        et_card_number.addTextChangedListener(new FourDigitCardFormatWatcher(et_card_number, card_background, cardLogo));

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
            String jsonToInsert = postData.toString().replace("\\n", "");
            String cardData = jsonToInsert.replace("\\", "");
            results = new Caller().insertUserCard(userID, token, cardData);

            return results;
        }

        @Override
        protected void onPostExecute(ResponseStatus responseStatus) {
            super.onPostExecute(responseStatus);
            //TODO we should add other items here too

            if (responseStatus != null) {
                if (responseStatus.get_status().equals("SUCCESS")) {
                    mListener.RefreshCard();
                    dismiss();

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

