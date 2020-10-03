package com.fara.inkamapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Adapters.CrimeAdapter;
import com.fara.inkamapp.BottomSheetFragments.Payment;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.HideKeyboard;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.Models.ServiceBillInfo;
import com.fara.inkamapp.Models.TrafficFines;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.zxing.Result;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class ServiceBillsAndCarFines extends HideKeyboard implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private TextView scanBarcode, toastText, purchased;
    private RelativeLayout showBarcode, hideBarcode;
    private ViewGroup contentFrame;
    private int billCode;
    private ImageButton trafficInfo, back;
    private EditText carBillCode, billId, paymentId;
    private Button nextStep;
    private String inputBillID, billIdText, billPaymentText, billAmountToIntent, paymentIdToIntent, billIdToIntent,
            plateNumber, carFinesTotalAmount, addressToIntent, pdfUrlToIntent, currentDateToIntent, extraInfoToIntent, fullNameToIntent, paymentDateToIntent,
            previousDateToIntent, titleToIntent, token, userID, encryptedToken, AesKey, dataToConfirm;

    private SharedPreferences sharedpreferences;
    private BottomSheetDialogFragment bottomSheetDialogFragment;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/IRANSansMobile.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        setContentView(R.layout.activity_service_bills_and_car_fines);
        initVariables();
    }

    private void initVariables(){
        scanBarcode = findViewById(R.id.tv_barcode_scan);
        showBarcode = findViewById(R.id.rl_show_camera);
        hideBarcode = findViewById(R.id.rl_hide_camera);
        trafficInfo = findViewById(R.id.ib_car_id_info);
        carBillCode = findViewById(R.id.et_car_eight_digits);
        billId = findViewById(R.id.et_bills_id);
        paymentId = findViewById(R.id.et_payment_id);
        nextStep = findViewById(R.id.btn_next_step);
        back = findViewById(R.id.ib_back);
        purchased = findViewById(R.id.tv_purchased);
        contentFrame = findViewById(R.id.content_frame);

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        contentFrame.addView(mScannerView);

        if (checkPermission()) {
            //main logic or main code
            showBarcode.setVisibility(View.VISIBLE);
            hideBarcode.setVisibility(View.INVISIBLE);

        } else {
            requestPermission();
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("Token", null);
        userID = sharedpreferences.getString("UserID", null);
        AesKey = sharedpreferences.getString("key", null);
        try {
            encryptedToken = Base64.encode((RSA.encrypt(token, publicKey)));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        purchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PurchasedItems.class);
                if (billCode == 0)
                    intent.putExtra("purchasedType", 7);
                else
                    intent.putExtra("purchasedType", 9);

                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        billCode = intent.getIntExtra("BillCode", 0);

        if (billCode == 0) {
            trafficInfo.setVisibility(View.VISIBLE);
            carBillCode.setVisibility(View.VISIBLE);
            billId.setVisibility(View.GONE);
            paymentId.setVisibility(View.GONE);

        } else if (billCode == 1) {
            billId.setVisibility(View.VISIBLE);
            paymentId.setVisibility(View.VISIBLE);
            trafficInfo.setVisibility(View.GONE);
            carBillCode.setVisibility(View.GONE);
        }

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (billCode == 0) {
                    inputBillID = carBillCode.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), CrimesList.class);
                    intent.putExtra("fineToken", inputBillID);
                    startActivity(intent);

                } else if (billCode == 1) {
                    billIdText = billId.getText().toString();
                    billPaymentText = paymentId.getText().toString();
                    new BillInfo().execute();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    showBarcode.setVisibility(View.VISIBLE);
                    hideBarcode.setVisibility(View.INVISIBLE);
                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ServiceBillsAndCarFines.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Start camera on resume

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
//        setupUI(findViewById(R.id.parent));
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

        if (carBillCode.getVisibility() != View.GONE)
            carBillCode.setText(rawResult.getText());
        else
            billId.setText(rawResult.getText());


        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ServiceBillsAndCarFines.this);
            }
        }, 2000);
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                MY_CAMERA_REQUEST_CODE);
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    private class trafficFinesInfo extends AsyncTask<Void, Void, TrafficFines> {

        TrafficFines results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected TrafficFines doInBackground(Void... params) {

            results = new Caller().getTrafficFinesInfo(inputBillID, userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(TrafficFines trafficFines) {
            super.onPostExecute(trafficFines);
            //TODO we should add other items here too


            if (trafficFines != null) {
                if (trafficFines.get_status().get_code().matches("G00000")) {
                    if (trafficFines.get_carFinesParameters().get_plateNumber() != null)
                        plateNumber = trafficFines.get_carFinesParameters().get_plateNumber();

                    if (!String.valueOf(trafficFines.get_carFinesParameters().get_totalAmount()).isEmpty())
                        carFinesTotalAmount = String.valueOf(trafficFines.get_carFinesParameters().get_totalAmount());

                    if (trafficFines.get_carFinesParameters().get_trafficFinesDetails().get(0).get_billId() != null)
                        billIdToIntent = trafficFines.get_carFinesParameters().get_trafficFinesDetails().get(0).get_billId();

                    if (trafficFines.get_carFinesParameters().get_trafficFinesDetails().get(0).get_paymentId() != null)
                        paymentIdToIntent = trafficFines.get_carFinesParameters().get_trafficFinesDetails().get(0).get_paymentId();


                    Intent intent = new Intent(getApplicationContext(), FinalPayment.class);
                    intent.putExtra("title", getResources().getString(R.string.car_fines));
                    intent.putExtra("plateNumber", plateNumber);
                    intent.putExtra("totalAmount", carFinesTotalAmount);
                    intent.putExtra("paymentID", paymentIdToIntent);
                    intent.putExtra("billID", billIdToIntent);
                    intent.putExtra("paymentType", 1);


                    startActivity(intent);

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), trafficFines.get_status().get_description(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toastText = toast.getView().findViewById(android.R.id.message);
                    toast.getView().setBackgroundResource(R.drawable.toast_background);

                    if (toastText != null) {
                        toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                        toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                        toastText.setGravity(Gravity.CENTER);
                        toastText.setTextSize(14);
                    }
                    toast.show();
                }

            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }
    }

    private class BillInfo extends AsyncTask<Void, Void, ServiceBillInfo> {

        ServiceBillInfo results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected ServiceBillInfo doInBackground(Void... params) {

            String substring = billIdText.substring(0, billIdText.length() - 1);
            int token = Integer.parseInt(substring.substring(substring.length() - 1));

            switch (token) {
                case 1:
                    results = new Caller().getWaterBillInfo(billIdText, userID, encryptedToken);
                    titleToIntent = getString(R.string.water_bill);
                    break;
                case 2:
                    results = new Caller().getElectricityBillInfoData(billIdText, userID, encryptedToken);
                    titleToIntent = getString(R.string.elec_bill);
                    break;
                case 3:
                    results = new Caller().getGasBillInfo(billIdText, userID, encryptedToken);
                    titleToIntent = getString(R.string.gaz_bill);
                    break;

                default:

                    break;
            }

            return results;
        }

        @Override
        protected void onPostExecute(ServiceBillInfo serviceBillInfo) {
            super.onPostExecute(serviceBillInfo);
            //TODO we should add other items here too


            if (serviceBillInfo != null && serviceBillInfo.get_status() != null && serviceBillInfo.get_status().get_code() != "-1") {
                if (serviceBillInfo.get_status().get_code().matches("G00000")) {
                    if (serviceBillInfo.get_serviceBillDetails().get_address() != null)
                        addressToIntent = serviceBillInfo.get_serviceBillDetails().get_address();

                    if (!String.valueOf(serviceBillInfo.get_serviceBillDetails().get_amount()).isEmpty())
                        billAmountToIntent = String.valueOf(serviceBillInfo.get_serviceBillDetails().get_amount());

                    if (serviceBillInfo.get_serviceBillDetails().get_billId() != null)
                        billIdToIntent = serviceBillInfo.get_serviceBillDetails().get_billId();

                    if (serviceBillInfo.get_serviceBillDetails().get_billPdfUrl() != null)
                        pdfUrlToIntent = serviceBillInfo.get_serviceBillDetails().get_billPdfUrl();

                    if (serviceBillInfo.get_serviceBillDetails().get_currentDate() != null)
                        currentDateToIntent = serviceBillInfo.get_serviceBillDetails().get_currentDate();

                    if (serviceBillInfo.get_serviceBillDetails().get_extraInfo() != null)
                        extraInfoToIntent = serviceBillInfo.get_serviceBillDetails().get_extraInfo();

                    if (serviceBillInfo.get_serviceBillDetails().get_fullName() != null)
                        fullNameToIntent = serviceBillInfo.get_serviceBillDetails().get_fullName();

                    if (serviceBillInfo.get_serviceBillDetails().get_paymentDate() != null)
                        paymentDateToIntent = serviceBillInfo.get_serviceBillDetails().get_paymentDate();

                    if (serviceBillInfo.get_serviceBillDetails().get_paymentId() != null)
                        paymentIdToIntent = serviceBillInfo.get_serviceBillDetails().get_paymentId();

                    if (serviceBillInfo.get_serviceBillDetails().get_previousDate() != null)
                        previousDateToIntent = serviceBillInfo.get_serviceBillDetails().get_previousDate();

                    try {

                        Bundle bundle = new Bundle();
                        bundle.putString("title", titleToIntent);
                        bundle.putString("address", addressToIntent);
                        bundle.putString("amount", billAmountToIntent);
                        bundle.putString("billID", billIdToIntent);
                        bundle.putString("pdfURL", pdfUrlToIntent);
                        bundle.putString("currentDate", currentDateToIntent);
                        bundle.putString("extraInfo", extraInfoToIntent);
                        bundle.putString("fullName", fullNameToIntent);
                        bundle.putString("paymentDate", paymentDateToIntent);
                        bundle.putString("paymentID", paymentIdToIntent);
                        bundle.putString("previousDate", previousDateToIntent);
                        bundle.putInt("serviceType", 3);


                        bottomSheetDialogFragment = Payment.newInstance("Bottom Sheet Payment Dialog");
                        bottomSheetDialogFragment.setArguments(bundle);
                        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

                    } catch (Exception e) {
                        e.toString();
                    }

//                    Intent intent = new Intent(getApplicationContext(), FinalPayment.class);
//
//
//                    startActivity(intent);
                } else {

                    Toast toast = Toast.makeText(getApplicationContext(), serviceBillInfo.get_status().get_description(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toastText = toast.getView().findViewById(android.R.id.message);
                    toast.getView().setBackgroundResource(R.drawable.toast_background);

                    if (toastText != null) {
                        toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                        toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                        toastText.setGravity(Gravity.CENTER);
                        toastText.setTextSize(14);
                    }
                    toast.show();
                }

            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }
    }


}
