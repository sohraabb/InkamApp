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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fara.inkamapp.Activities.BuyPackages;
import com.fara.inkamapp.Activities.InternetPackageActivity;
import com.fara.inkamapp.Dialogs.SuccessTransfer;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Models.OperatorType;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class InternetPackageBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    private String string;
    private TextView toastText;
    private Button next;
    private JSONObject postData;
    private RelativeLayout hamrahAval, rightel, irancell;
    private int previousOperatorType = 0;
    private int operatorValue=0;
    private EditText phoneNumber;


    public static InternetPackageBottomSheet newInstance(String string) {
        InternetPackageBottomSheet internetPackageBottomSheet = new InternetPackageBottomSheet();
        Bundle args = new Bundle();
        args.putString("string", string);
        internetPackageBottomSheet.setArguments(args);
        return internetPackageBottomSheet;
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

        View view = inflater.inflate(R.layout.bottom_sheet_internet_package, container,
                false);

        // get the views and attach the listener

        next = view.findViewById(R.id.btn_continue);
        rightel = view.findViewById(R.id.rl_rightel);
        hamrahAval = view.findViewById(R.id.rl_hamrah_aval);
        irancell = view.findViewById(R.id.rl_irancell);
        phoneNumber = view.findViewById(R.id.et_phone_number);

        rightel.setOnClickListener(this);
        hamrahAval.setOnClickListener(this);
        irancell.setOnClickListener(this);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                postData = new JSONObject();
//                try {
//                    postData.put("CellNumbers", phoneNumber.getText().toString());
//                    postData.put("ChargeType", "0");
//                    postData.put("BankId", "08");
//                    postData.put("DeviceType", "59");
//                    postData.put("Operator", operatorValue);
//                    postData.put("Merchant", "irancell");
//                    postData.put("packageDescription", "0");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                Intent intent = new Intent(getActivity(), InternetPackageActivity.class);
                intent.putExtra("CellNumbers", phoneNumber.getText().toString());
                intent.putExtra("Operator", operatorValue);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        int temp = 0;

        temp = v.getId();

        rightel.setBackgroundResource(R.drawable.edittext_background_black_stroke);
        hamrahAval.setBackgroundResource(R.drawable.edittext_background_black_stroke);
        irancell.setBackgroundResource(R.drawable.edittext_background_black_stroke);


        if (temp == previousOperatorType) {
            temp = 0;
            previousOperatorType = 0;
        }


        switch (temp) {

            case R.id.rl_rightel: {

                rightel.setBackgroundResource(R.drawable.green_stroke_background);
                operatorValue = OperatorType.Rightel.getValue();

            }

            break;
            case R.id.rl_hamrah_aval: {
                hamrahAval.setBackgroundResource(R.drawable.green_stroke_background);
                operatorValue = OperatorType.Mci.getValue();

            }
            break;

            case R.id.rl_irancell: {
                irancell.setBackgroundResource(R.drawable.green_stroke_background);
                operatorValue = OperatorType.Mtn.getValue();

            }
            break;

        }
        previousOperatorType = temp;

    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getActivity());
    }

    private class InternetPackageReserve extends AsyncTask<Void, Void, ReserveTopupRequest> {

        ReserveTopupRequest results = null;

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
        protected ReserveTopupRequest doInBackground(Void... params) {
            results = new Caller().internetPackageReserve("2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00", postData.toString());

            return results;
        }

        @Override
        protected void onPostExecute(ReserveTopupRequest reserveTopUpRequest) {
            super.onPostExecute(reserveTopUpRequest);
            //TODO we should add other items here too

            if (reserveTopUpRequest.get_token() != null) {

                Intent intent = new Intent(getActivity(), InternetPackageActivity.class);
                startActivity(intent);

//                Intent intent = new Intent(getActivity(), BuyPackages.class);
//                intent.putExtra("orderID", reserveTopUpRequest.get_reserveNumber());
//                intent.putExtra("data", "");
//                startActivity(intent);

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

