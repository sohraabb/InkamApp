package com.fara.inkamapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.BottomSheetFragments.Payment;
import com.fara.inkamapp.BuildConfig;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.DateConverter;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.JalaliCalendar;
import com.fara.inkamapp.Helpers.JavaSource_Calendar;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class BuyResult extends AppCompatActivity {
    private TextView tv_title, tv_failed, tv_amount_title, tv_amount, tv_amount_rial,
            tv_consistency_number_title, tv_consistency_number, tv_date,
            tv_destination_card_title, tv_destination_card, tv_own_card_title, tv_own_card, tv_operator, tv_package_title, addRepeatPurchase, toastText;
    private ImageView iv_failed;
    private RelativeLayout rv_last, rv_package, rl_line;
    private LinearLayout ll_factor;
    private ImageButton btn_share;
    private SharedPreferences sharedPreferences;
    private String _token, _userID, _encryptedToken, _type, _operator, _mobile, _serviceType, _amount, phoneNum, refNumber;
    private String _chargeType = " ";
    private String _dataPlanType = " ";
    private JSONObject info;
    private String operatorToInsert, typeToInsert;

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

        setContentView(R.layout.activity_buy_result);
        try {

            initVariables();

            _type = getIntent().getStringExtra("type");
            _chargeType = getIntent().getStringExtra("chargeType");
            _dataPlanType = getIntent().getStringExtra("dataplanType");
            _operator = getIntent().getStringExtra("operator");
            phoneNum = getIntent().getStringExtra("phoneNum");
            boolean success = getIntent().getBooleanExtra("success", false);

            NumberFormat formatter = new DecimalFormat("#,###");
            _amount = formatter.format(Double.parseDouble(getIntent().getStringExtra("amount")));
            tv_amount.setText(Numbers.ToPersianNumbers(_amount));
            String describe = getIntent().getStringExtra("describe");

            _mobile = getIntent().getStringExtra("mobile");
            refNumber = getIntent().getStringExtra("refrenceNumber");
            String billID = getIntent().getStringExtra("billID");
            JalaliCalendar jalaliCalendar = new JalaliCalendar();
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            String dateformatted = dateFormat.format(date);
//            System.out.println(dateformatted);


            try {
                String dateCorrection = DateConverter.Convert_Miladi_To_Shamsi_Date_ByTime(getIntent().getStringExtra("date").substring(0, 16).replace("T", " "));
                if (dateCorrection != null)
                    tv_date.setText(Numbers.ToPersianNumbers(dateCorrection));
            } catch (Exception e) {
                String dateCorrection = Numbers.ToPersianNumbers(getIntent().getStringExtra("date"));
                if (dateCorrection != null)
                    tv_date.setText(Numbers.ToPersianNumbers(dateCorrection + " " + dateformatted));

            }


            if (success) {
                tv_failed.setText(R.string.success_payment);
                tv_failed.setTextColor(getResources().getColor(R.color.colorGreen));
                iv_failed.setImageResource(R.drawable.ic_circular_check);
                addRepeatPurchase.setVisibility(View.VISIBLE);
            } else {
                JavaSource_Calendar javaSource_calendar = new JavaSource_Calendar();
                tv_date.setText(Numbers.ToPersianNumbers(javaSource_calendar.getIranianDate() + " " + dateformatted));


            }
            switch (_type) {
                case "charge":
                    tv_title.setText(R.string.buy_charge);
                    tv_amount_title.setText(R.string.charge);
                    switch (_operator) {
                        case "0":
                            _operator = "ایرانسل";
                            operatorToInsert = "0";
                            break;
                        case "1":
                            _operator = "همراه اول";
                            operatorToInsert = "1";
                            break;
                        case "2":
                            _operator = "رایتل";
                            operatorToInsert = "2";
                            break;
                    }
                    tv_operator.setText(_operator);
                    tv_consistency_number_title.setText(R.string.mobile);
                    tv_consistency_number.setText(Numbers.ToPersianNumbers(_mobile));
                    tv_destination_card_title.setText(R.string.reference_number);
                    if (refNumber != null)
                        tv_destination_card.setText(Numbers.ToPersianNumbers(refNumber));
                    rv_last.setVisibility(View.INVISIBLE);
                    typeToInsert = "0";

                    break;

                case "internet":
                    tv_title.setText(R.string.buy_packages);

                    switch (_operator) {
                        case "0":
                            _operator = "ایرانسل";
                            operatorToInsert = "0";
                            break;
                        case "1":
                            _operator = "همراه اول";
                            operatorToInsert = "1";
                            break;
                        case "2":
                            _operator = "رایتل";
                            operatorToInsert = "2";
                            break;
                    }

                    rv_package.setVisibility(View.VISIBLE);
                    tv_operator.setText(_operator);
                    if (describe != null)
                        tv_package_title.setText(Numbers.ToPersianNumbers(describe));
                    tv_amount_title.setText(R.string.internet_package);
                    tv_consistency_number_title.setText(R.string.mobile);
                    tv_consistency_number.setText(Numbers.ToPersianNumbers(_mobile));

                    tv_destination_card_title.setText(R.string.reference_number);
                    if (refNumber != null)
                        tv_destination_card.setText(Numbers.ToPersianNumbers(refNumber));

                    rv_last.setVisibility(View.INVISIBLE);
                    typeToInsert = "1";

                    break;

                case "bill":
                    tv_title.setText(R.string.pay_bill);
                    tv_amount_title.setText(R.string.bill);
                    tv_operator.setVisibility(View.INVISIBLE);
                    rl_line.setVisibility(View.GONE);

                    if (billID != null) {
                        tv_consistency_number_title.setText(R.string.bills_id);
                        tv_consistency_number.setText(billID);
                    }

                    if (phoneNum != null) {
                        tv_destination_card_title.setText(R.string.phone_num);
                        tv_destination_card.setText(Numbers.ToPersianNumbers(refNumber));
                    } else {
                        tv_destination_card_title.setVisibility(View.INVISIBLE);
                        tv_destination_card.setVisibility(View.INVISIBLE);
                    }

                    rv_last.setVisibility(View.INVISIBLE);
                    addRepeatPurchase.setVisibility(View.GONE);

                    break;
                case "phone":

                    tv_title.setText(R.string.pay_bill);
                    tv_amount_title.setText(R.string.phone_bill);

                    tv_operator.setVisibility(View.INVISIBLE);
                    rl_line.setVisibility(View.GONE);
                    if (refNumber != null)
                        tv_consistency_number.setText(Numbers.ToPersianNumbers(refNumber));
                    tv_destination_card_title.setText(R.string.phone_num);
                    tv_destination_card.setText(Numbers.ToPersianNumbers(phoneNum));
                    rv_last.setVisibility(View.INVISIBLE);
                    addRepeatPurchase.setVisibility(View.GONE);

                    break;
            }
            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!checkPermission()) {
                        takeScreenshot();
                    } else {
                        if (checkPermission()) {
                            requestPermissionAndContinue();
                        } else {
                            takeScreenshot();
                        }
                    }

                }
            });

            addRepeatPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new InsertRepeatPurchase().execute();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initVariables() {
        btn_share = findViewById(R.id.btn_share);
        rv_package = findViewById(R.id.rv_package);
        tv_package_title = findViewById(R.id.tv_package_title);
        ll_factor = findViewById(R.id.ll_factor);
        tv_title = findViewById(R.id.tv_title);
        tv_failed = findViewById(R.id.tv_failed);
        tv_amount_title = findViewById(R.id.tv_amount_title);
        tv_amount = findViewById(R.id.tv_amount);
        tv_amount_rial = findViewById(R.id.tv_amount_rial);
        tv_consistency_number_title = findViewById(R.id.tv_consistency_number_title);
        tv_consistency_number = findViewById(R.id.tv_consistency_number);
        tv_date = findViewById(R.id.tv_date);
        tv_destination_card_title = findViewById(R.id.tv_destination_card_title);
        tv_destination_card = findViewById(R.id.tv_destination_card);
//            tv_own_card_title = findViewById(R.id.tv_own_card_title);
//            tv_own_card = findViewById(R.id.tv_own_card);
        iv_failed = findViewById(R.id.iv_failed);
        tv_operator = findViewById(R.id.tv_operator);
        rv_last = findViewById(R.id.rv_last);
        rl_line = findViewById(R.id.rl_line);
        addRepeatPurchase = findViewById(R.id.tv_add_repeat_purchase);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        _token = sharedPreferences.getString("Token", null);
        _userID = sharedPreferences.getString("UserID", null);
        try {
            _encryptedToken = Base64.encode((RSA.encrypt(_token, publicKey)));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {

                boolean flag = true;
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    takeScreenshot();
                } else {
                    finish();
                }

            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void backClicke(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    private class InsertRepeatPurchase extends AsyncTask<Void, Void, ResponseStatus> {

        ResponseStatus results = null;

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

            info = new JSONObject();
            try {
                String encryptedPhone = Base64.encode((RSA.encrypt(_mobile, publicKey)));
                String encryptedAmount = Base64.encode((RSA.encrypt(Numbers.ToEnglishNumbers(_amount).replace(",", ""), publicKey)));

                info.put("ChargeType", _chargeType);
                info.put("DataPlanType", 0);
                info.put("UserID", _userID);
                info.put("Code", typeToInsert + operatorToInsert);
                info.put("Type", typeToInsert);
                info.put("AmountString", encryptedAmount);
                info.put("Operator", operatorToInsert);
                info.put("Mobile", encryptedPhone);

            } catch (JSONException |
                    BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException
                    e) {
                e.printStackTrace();
            }

        }

        @Override
        protected ResponseStatus doInBackground(Void... params) {
            results = new Caller().insertRepeatPurchase(_userID, _encryptedToken, info.toString());

            return results;
        }

        @Override
        protected void onPostExecute(ResponseStatus login) {
            super.onPostExecute(login);
            //TODO we should add other items here too

            if (login != null && login.get_status().equals("SUCCESS")) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.repeat_success, Toast.LENGTH_SHORT);
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

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            File imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            shareImage(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void shareImage(File file) {
        Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    private static final int PERMISSION_REQUEST_CODE = 200;

    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ;
    }

    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission necessary");
                alertBuilder.setMessage("External storage" + " permission is necessary");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(BuyResult.this, new String[]{WRITE_EXTERNAL_STORAGE
                                , READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(BuyResult.this, new String[]{WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        } else {
            takeScreenshot();
        }
    }


}
