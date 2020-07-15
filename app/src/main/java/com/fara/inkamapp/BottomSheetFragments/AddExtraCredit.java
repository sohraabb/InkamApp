package com.fara.inkamapp.BottomSheetFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fara.inkamapp.Activities.MainActivity;
import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.PaymentResult;
import com.fara.inkamapp.Models.WalletCredit;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.top.lib.mpl.view.PaymentInitiator;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class AddExtraCredit extends BottomSheetDialogFragment implements View.OnClickListener {

    String string;
    private Button addCredit;
    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private TextView toastText, tv_1, tv_500, tv_300, tv_20, tv_50, tv_10, tvSelected;
    public MainActivity activity;
    private Context _context;
    private String dataToConfirm, amount;
    private EditText et_amount;
    private SharedPreferences sharedpreferences;
    private ItemClickListener mClickListener;
    private String AesKey;
    private SharedPreferences sharedPreferences;


    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick();
    }

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
        sharedPreferences = _context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        AesKey = sharedPreferences.getString("key", null);
        addCredit = view.findViewById(R.id.btn_add_credit);
        et_amount = view.findViewById(R.id.et_amount);
        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() != 0) {
                    String str = s.toString().replace(",", "");
                    NumberFormat formatter = new DecimalFormat("#,###");
                    String formattedNumber = formatter.format(Double.valueOf(Numbers.ToEnglishNumbers(str)));
                    et_amount.removeTextChangedListener(this);
                    et_amount.setText(Numbers.ToPersianNumbers(formattedNumber));
                    et_amount.setSelection(et_amount.length());
                    et_amount.addTextChangedListener(this);
                    //
                }
            }
        });

        tv_1 = view.findViewById(R.id.tv_1);
        tv_500 = view.findViewById(R.id.tv_500);
        tv_300 = view.findViewById(R.id.tv_300);
        tv_20 = view.findViewById(R.id.tv_20);
        tv_50 = view.findViewById(R.id.tv_50);
        tv_10 = view.findViewById(R.id.tv_10);
        tv_1.setOnClickListener(this);
        tv_500.setOnClickListener(this);
        tv_300.setOnClickListener(this);
        tv_20.setOnClickListener(this);
        tv_50.setOnClickListener(this);
        tv_10.setOnClickListener(this);


//        bottomSheetDialogFragment = TransferCredit.newInstance("Bottom Sheet Dialog");

        addCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = Numbers.ToEnglishNumbers(et_amount.getText().toString().replace(",", ""));
                new IncreaseWalletCredit().execute();

//                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
//                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        et_amount.setText(((TextView) v).getText().toString().replace("ریال", ""));
        amount = Numbers.ToEnglishNumbers(et_amount.getText().toString().replace(",", ""));
//        v.setBackgroundResource(R.drawable.edittext_background_black_stroke);
//        if (tvSelected != null) {
//            tvSelected.setBackgroundResource(R.drawable.edittext_background_black_stroke);
//        }
//        tvSelected = ((TextView) v);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this._context = context;
        mClickListener=(ItemClickListener)context;

    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getActivity());
    }

    private class IncreaseWalletCredit extends AsyncTask<Void, Void, PaymentResult> {

        PaymentResult results = null;

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
        protected PaymentResult doInBackground(Void... params) {

            String res = null;
            try {
                try {
                    res = new Caller().IncreaseWalletCreditRequest(MainActivity._userId, MainActivity._token, Long.parseLong(MainActivity._userName.substring(1)), Base64.encode((RSA.encrypt(amount, publicKey))));

                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            } catch (Exception ee) {
                String s = ee.toString();
            }
            Gson gson = new Gson();
            try {
                results = gson.fromJson(AESEncyption.decryptMsg(res, AesKey), PaymentResult.class);
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidParameterSpecException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(PaymentResult walletCredit) {
            super.onPostExecute(walletCredit);
            //TODO we should add other items here too

            if (walletCredit.getData().getToken() != null) {
                String token = walletCredit.getData().getToken();

                // for OrderID Random Number

                Intent intent = new Intent(_context, PaymentInitiator.class);
                intent.putExtra("Type", "1");
                intent.putExtra("Token", token);
                intent.putExtra("OrderID", walletCredit.getData().getOrderId());
                intent.putExtra("TSPEnabled", 1);

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

            try {
                results = new Caller().increaseWalletCreditRequestConfirm(MainActivity._userId, MainActivity._token, dataToConfirm, Base64.encode((RSA.encrypt(amount, publicKey))));
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            //TODO we should add other items here too

            if (response != null) {

                Toast toast = Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT);
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
                if (mClickListener != null) {
                    mClickListener.onItemClick();
                }
                dismiss();
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
                dataToConfirm = data.getStringExtra("enData").replace("\\", "");
                String one = data.getStringExtra("message");
                String sts = String.valueOf(data.getIntExtra("status", -1));
                if (sts.equals("0")) {
                    new IncreaseWalletCreditRequestConfirm().execute();
                }
                break;
        }

    }

}
