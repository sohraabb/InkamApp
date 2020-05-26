package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.Models.ServiceBillInfo;
import com.fara.inkamapp.Models.TrafficFines;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.zxing.Result;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ServiceBillsAndCarFines extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private TextView scanBarcode, toastText;
    private RelativeLayout showBarcode, hideBarcode;
    private FrameLayout contentFrame;
    private int billCode;
    private ImageButton trafficInfo;
    private EditText carBillCode, billId, paymentId;
    private Button nextStep;
    private String inputBillID, billIdText, billPaymentText, billAmountToIntent, paymentIdToIntent, billIdToIntent,
            plateNumber, carFinesTotalAmount, addressToIntent, pdfUrlToIntent, currentDateToIntent, extraInfoToIntent, fullNameToIntent, paymentDateToIntent,
            previousDateToIntent, titleToIntent;

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


        contentFrame = findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        contentFrame.addView(mScannerView);

        scanBarcode = findViewById(R.id.tv_barcode_scan);
        showBarcode = findViewById(R.id.rl_show_camera);
        hideBarcode = findViewById(R.id.rl_hide_camera);
        trafficInfo = findViewById(R.id.ib_car_id_info);
        carBillCode = findViewById(R.id.et_car_eight_digits);
        billId = findViewById(R.id.et_bills_id);
        paymentId = findViewById(R.id.et_payment_id);
        nextStep = findViewById(R.id.btn_next_step);


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


        scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBarcode.setVisibility(View.VISIBLE);
                hideBarcode.setVisibility(View.INVISIBLE);
            }
        });

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (billCode == 0) {
                    inputBillID = carBillCode.getText().toString();

                    new trafficFinesInfo().execute();

                } else if (billCode == 1) {
                    billIdText = billId.getText().toString();
                    billPaymentText = paymentId.getText().toString();
                    new BillInfo().execute();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("Sori", rawResult.getText()); // Prints scan results
        Log.v("Sori", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
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

            results = new Caller().getTrafficFinesInfo(inputBillID, "2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00");

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
                    results = new Caller().getWaterBillInfo(billIdText, "2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00");
                    titleToIntent = getString(R.string.water_bill);
                    break;
                case 2:
                    results = new Caller().getElectricityBillInfoData(billIdText, "2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00");
                    titleToIntent = getString(R.string.elec_bill);
                    break;
                case 3:
                    results = new Caller().getGasBillInfo(billIdText, "2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00");
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


            if (serviceBillInfo != null) {
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

                    Intent intent = new Intent(getApplicationContext(), FinalPayment.class);
                    intent.putExtra("title", titleToIntent);
                    intent.putExtra("address", addressToIntent);
                    intent.putExtra("amount", billAmountToIntent);
                    intent.putExtra("billID", billIdToIntent);
                    intent.putExtra("pdfURL", pdfUrlToIntent);
                    intent.putExtra("currentDate", currentDateToIntent);
                    intent.putExtra("extraInfo", extraInfoToIntent);
                    intent.putExtra("fullName", fullNameToIntent);
                    intent.putExtra("paymentDate", paymentDateToIntent);
                    intent.putExtra("paymentID", paymentIdToIntent);
                    intent.putExtra("previousDate", previousDateToIntent);
                    intent.putExtra("paymentType", 0);

                    startActivity(intent);
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
