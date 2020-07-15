package com.fara.inkamapp.BottomSheetFragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InternetPackageBottomSheet extends BottomSheetDialogFragment {

    private String string;
    private TextView toastText;
    private Button next;
    private JSONObject postData;
    private RelativeLayout hamrahAval, rightel, irancell;
    private int previousOperatorType = 0;
    private int operatorValue = 0;
    private EditText phoneNumber;
    private ImageButton contact;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PICK_CONTACT = 856;

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
        contact = view.findViewById(R.id.ib_contact);


        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                }

            }
        });
        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isIrancell(s.toString())){
                    irancell.setBackgroundResource(R.drawable.green_stroke_background);
                    hamrahAval.setBackgroundResource(R.drawable.white_rounded_background);
                    rightel.setBackgroundResource(R.drawable.white_rounded_background);
                    operatorValue=0;
                }else if(isHamrahAval(s.toString())){
                    hamrahAval.setBackgroundResource(R.drawable.green_stroke_background);
                    irancell.setBackgroundResource(R.drawable.white_rounded_background);
                    rightel.setBackgroundResource(R.drawable.white_rounded_background);
                    operatorValue=1;
                }else if(isRightel(s.toString())){
                    rightel.setBackgroundResource(R.drawable.green_stroke_background);
                    hamrahAval.setBackgroundResource(R.drawable.white_rounded_background);
                    irancell.setBackgroundResource(R.drawable.white_rounded_background);
                    operatorValue=3;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), InternetPackageActivity.class);
                intent.putExtra("CellNumbers", phoneNumber.getText().toString());
                intent.putExtra("Operator", operatorValue);
                startActivity(intent);
            }
        });

        return view;
    }

    private boolean isIrancell(String input) {
        Pattern p = Pattern.compile("^09[0|3][0-9]{8}$");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    private boolean isRightel(String input) {
        Pattern p = Pattern.compile("^09[2][0-9]{8}$");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    private boolean isHamrahAval(String input) {
        Pattern p = Pattern.compile("^09[1][0-9]{8}$");
        Matcher m = p.matcher(input);
        return m.matches();
    }


}

