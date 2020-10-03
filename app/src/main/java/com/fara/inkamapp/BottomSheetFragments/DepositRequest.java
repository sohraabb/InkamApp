package com.fara.inkamapp.BottomSheetFragments;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fara.inkamapp.Activities.MainActivity;
import com.fara.inkamapp.Dialogs.SuccessTransfer;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class DepositRequest extends BottomSheetDialogFragment {
    private Button submit, no;
    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private EditText et_amount, et_iban, et_name;
    private TextView toastText;
    private String amount,sheba,name;

    public static DepositRequest newInstance(String string) {
        DepositRequest req = new DepositRequest();
        Bundle args = new Bundle();
        args.putString("string", string);
        req.setArguments(args);
        return req;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_deposit_request, container,
                false);

        // get the views and attach the listener
        submit = view.findViewById(R.id.btn_submit);
        et_iban = view.findViewById(R.id.et_iban);
        et_name = view.findViewById(R.id.et_name);
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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             amount=  Numbers.ToEnglishNumbers( et_amount.getText().toString().replace(",",""));
             sheba=et_iban.getText().toString();
             name=et_name.getText().toString();
                new InsertDepositTransaction().execute();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
      //  this._context = context;
       // mClickListener = (AddExtraCredit.ItemClickListener) context;

    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getActivity());
    }

    private class InsertDepositTransaction extends AsyncTask<Void, Void, ResponseStatus> {

        ResponseStatus results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getContext(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor( getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected ResponseStatus doInBackground(Void... params) {
            try {
                results = new Caller().InsertDepositTransaction(MainActivity._userId, MainActivity._token, Base64.encode((RSA.encrypt(sheba, publicKey))),name,Base64.encode((RSA.encrypt(amount, publicKey))));
            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(ResponseStatus res) {
            super.onPostExecute(res);
            //TODO we should add other items here too

            if (res != null&&res.get_status().equals("SUCCESS")) {

                SuccessTransfer successTransfer = new SuccessTransfer(getActivity(),true);
                successTransfer.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                successTransfer.show();
                //if (mClickListener != null) {
               //     mClickListener.onTransferItemClick();
               // }
                dismiss();

            } else {
                Toast toast = Toast.makeText(getContext(), res.get_message(), Toast.LENGTH_SHORT);
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
