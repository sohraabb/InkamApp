package com.fara.inkamapp.BottomSheetFragments;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fara.inkamapp.Activities.InternetPackageActivity;
import com.fara.inkamapp.Activities.MainActivity;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.Models.WalletCredit;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ir.pec.mpl.pecpayment.view.PaymentInitiator;

public class AddExtraCredit extends BottomSheetDialogFragment {

    String string;
    private Button addCredit;
    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private TextView toastText;
    public MainActivity activity;
    private Context _context;



    public static AddExtraCredit newInstance(String string) {
        AddExtraCredit addExtraCredit = new AddExtraCredit();
        Bundle args = new Bundle();
        args.putString("string", string);
        addExtraCredit.setArguments(args);
        return addExtraCredit;
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

        View view = inflater.inflate(R.layout.bottom_sheet_extra_credit, container,
                false);

        // get the views and attach the listener

        addCredit = view.findViewById(R.id.btn_add_credit);

//        bottomSheetDialogFragment = TransferCredit.newInstance("Bottom Sheet Dialog");

        addCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IncreaseWalletCredit().execute();

//                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
//                dismiss();
            }
        });

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this._context = context;

    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getActivity());
    }

    private class IncreaseWalletCredit extends AsyncTask<Void, Void, WalletCredit> {

        WalletCredit results = null;

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
        protected WalletCredit doInBackground(Void... params) {

            results = new Caller().IncreaseWalletCreditRequest("2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00", Long.parseLong("09374227117"), Long.parseLong("10000"));

            return results;
        }

        @Override
        protected void onPostExecute(WalletCredit walletCredit) {
            super.onPostExecute(walletCredit);
            //TODO we should add other items here too

                if (walletCredit.get_data().get_token() != null) {
                    String token = walletCredit.get_data().get_token();
                    Intent intent = new Intent(_context, PaymentInitiator.class);
                    intent.putExtra("Type", "1");
                    intent.putExtra("Token", token);
                    intent.putExtra("OrderID", 123);
                    startActivityForResult(intent, 1);
                    //                new IncreaseWalletCreditRequestConfirm().execute();

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


    private class IncreaseWalletCreditRequestConfirm extends AsyncTask<Void, Void, String> {

        String results = null;

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
        protected String doInBackground(Void... params) {
            results = new Caller().increaseWalletCreditRequestConfirm("2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00", "", 12000);

            return results;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            //TODO we should add other items here too

            if (response != null) {

                Intent intent = new Intent(getActivity(), InternetPackageActivity.class);
                startActivity(intent);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                Toast.makeText(getActivity(), "good to go", Toast.LENGTH_SHORT).show();
                break;
        }

    }

}
