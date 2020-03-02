package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fara.inkamapp.R;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ServiceBillsAndCarFines extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private TextView scanBarcode;
    private RelativeLayout showBarcode, hideBarcode;
    private FrameLayout contentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_bills_and_car_fines);

        contentFrame = findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        contentFrame.addView(mScannerView);

        scanBarcode = findViewById(R.id.tv_barcode_scan);
        showBarcode = findViewById(R.id.rl_show_camera);
        hideBarcode = findViewById(R.id.rl_hide_camera);


        scanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBarcode.setVisibility(View.VISIBLE);
                hideBarcode.setVisibility(View.INVISIBLE);
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
}
