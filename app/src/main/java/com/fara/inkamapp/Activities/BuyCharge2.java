package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.HideKeyboard;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.Models.UserWallet;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.top.lib.mpl.view.PaymentInitiator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class BuyCharge2 extends HideKeyboard {

    private String orderId;
    private Button payWallet, pay;
    private TextView toastText;
    private JSONObject postData;
    private String amount, operator, type, phone, token, userID, encryptedToken, AesKey,dataToConfirm;
    private SharedPreferences sharedpreferences;
    private ImageButton back;

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

        setContentView(R.layout.activity_buy_charge2);
        TextView tv_phone_number = findViewById(R.id.tv_phone_number);
        amount = getIntent().getStringExtra("amount");
        tv_phone_number.setText(amount);
        operator = getIntent().getStringExtra("operator");
        type = getIntent().getStringExtra("type");
        phone = getIntent().getStringExtra("phone");
        pay = findViewById(R.id.btn_pay);
        payWallet = findViewById(R.id.btn_pay_wallet);

        back = findViewById(R.id.ib_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("Token", null);
        userID = sharedpreferences.getString("UserID", null);
        AesKey = sharedpreferences.getString("key", null);

        amount = Numbers.ToEnglishNumbers(amount.replace(",", ""));
        try {
            encryptedToken = Base64.encode((RSA.encrypt(token, publicKey)));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        new GetUserWallet().execute();
        payWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postData = new JSONObject();
                try {
                    postData.put("Amount", amount);
                    postData.put("CellNumbers", Base64.encode(RSA.encrypt(phone, publicKey)));
                    postData.put("CellNumber", phone);
                    postData.put("ChargeType", type);
                    postData.put("BankId", "08");
                    postData.put("DeviceType", "59");
                    postData.put("Operator", operator);

                } catch (JSONException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                    e.printStackTrace();
                }


                new TopUpRequestForWallet().execute();
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(BuyCharge2.this, PaymentInitiator.class);
//                intent.putExtra("Type", "1");
//                intent.putExtra("Token", token);
//                intent.putExtra("OrderID", 12345);
                postData = new JSONObject();
                try {
                    postData.put("Amount", amount);
                    postData.put("CellNumbers", Base64.encode(RSA.encrypt(phone, publicKey)));
                    postData.put("CellNumber", phone);
                    postData.put("ChargeType", type);
                    postData.put("BankId", "08");
                    postData.put("DeviceType", "59");
                    postData.put("Operator", operator);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }
                new TopUpRequest().execute();
            }
        });

        Bundle intent = getIntent().getExtras();
        orderId = intent.getString("orderID");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /* dataToConfirm ="{\"PayInfo\":\"e51sG3Rg1s4vftpps9lO0WIP8EEnBZy82HNMVO66Lyq8K4ApX01DHxZGE0yW1SUkjDGSJbyg+vcqNTI2Gd3IpwoAcoIO7xUuJFh8MDIqhCEaVt2E0lMDqNqz7hPXcgPoz3bNb5RH8c63Bd+Nuj4fvHR2MWg5zf9dueSFi+D0+SQ=\",\"PayData\":\"dXySBeJSHxeNucG0HHu5nkuTC+Q\\/nYDvZ8Lam4LAtgs0ZM54onSPd1JqrNlb8qwjZHvsQLgKAQKyj1C0B\\/OxZjaStQW4VxTQj68OjhJ\\/Gmk3LX7o8FhxJgXRf6SdI0QVG7B\\/umquZ4wCye5Sj0v+jUQGimgxiFQEJzM5F3uqIao=\",\"DataSign\":\"D3jRnsTH3wpQJmj8ONcYcQOu9ByJ09sItU3BNryi\\/BPiW8V6d6fKarl3+ImzJ3JpTFN4tVjA2+ZRRs4X4rFSh3Yq4Wh8LQkdImZ3HvvZcTGDz32SBvoUOmQBA1Jf6YBGhl99tXRFr42L6NvxfJ3fj\\/9DXh4wPUfAcRKlM5UpueE=\",\"AutoConfirm\":false}";// data.getStringExtra("enData");
        if(!dataToConfirm.equals(null))
            dataToConfirm=    dataToConfirm.replace("\\", "");
        String one = data.getStringExtra("message");
        String two = String.valueOf(data.getIntExtra("status", 0));
        new approveTopUpRequest().execute();
*/
        // if (requestCode == LAUNCH_SECOND_ACTIVITY) {
        if (resultCode == 1) {
            dataToConfirm = data.getStringExtra("enData");
            if(!dataToConfirm.equals(null))
                dataToConfirm=    dataToConfirm.replace("\\", "");
            String one = data.getStringExtra("message");
            String two = String.valueOf(data.getIntExtra("status", 0));
            new approveTopUpRequest().execute();
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.parent));
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    private class approveTopUpRequest extends AsyncTask<Void, Void, ReserveTopupRequest> {

        ReserveTopupRequest results = null;

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
        protected ReserveTopupRequest doInBackground(Void... params) {
            try {
                results = new Caller().approveTopupServiceReserve(userID, encryptedToken, dataToConfirm, Base64.encode((RSA.encrypt(orderId, publicKey))), Base64.encode((RSA.encrypt(amount, publicKey))), 0);
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
        protected void onPostExecute(ReserveTopupRequest reserveTopUpRequest) {
            super.onPostExecute(reserveTopUpRequest);
            //TODO we should add other items here too
            Intent intent = new Intent(getApplicationContext(), BuyResult.class);
            intent.putExtra("orderID", reserveTopUpRequest.get_reserveNumber());
            intent.putExtra("type", "charge");
            intent.putExtra("operator", operator);
            intent.putExtra("amount", amount);
            intent.putExtra("date", reserveTopUpRequest.get_date());
            intent.putExtra("mobile", phone);
            intent.putExtra("refrenceNumber", reserveTopUpRequest.get_referenceNumber());


            if (reserveTopUpRequest.get_reserveNumber() > 0) {

                intent.putExtra("success", true);


            } else {
                intent.putExtra("success", false);


               /* Toast toast = Toast.makeText(getApplicationContext(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();*/
            }
            startActivity(intent);

        }
    }

    private class TopUpRequest extends AsyncTask<Void, Void, ReserveTopupRequest> {

        ReserveTopupRequest results = null;

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
        protected ReserveTopupRequest doInBackground(Void... params) {
            results = new Caller().topupServiceReserve(userID, encryptedToken, postData.toString(), 0);

            return results;
        }

        @Override
        protected void onPostExecute(ReserveTopupRequest reserveTopUpRequest) {
            super.onPostExecute(reserveTopUpRequest);
            //TODO we should add other items here too


            if (reserveTopUpRequest.get_token() != null) {
                try {
                    orderId = AESEncyption.decryptMsg(reserveTopUpRequest.get_reserveNumberString(), AesKey);
                    Intent intent = new Intent(getApplicationContext(), PaymentInitiator.class);
                    intent.putExtra("Type", "1");
                    intent.putExtra("Token", AESEncyption.decryptMsg(reserveTopUpRequest.get_token(), AesKey));

                    intent.putExtra("OrderID", orderId);

                    intent.putExtra("TSPEnabled", 1);

                    startActivityForResult(intent, 1);
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

    private class TopUpRequestForWallet extends AsyncTask<Void, Void, ReserveTopupRequest> {

        ReserveTopupRequest results = null;

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
        protected ReserveTopupRequest doInBackground(Void... params) {
             results = new Caller().topupServiceReserveFromWallet(userID, encryptedToken, postData.toString(), 0);

            return results;
        }

        @Override
        protected void onPostExecute(ReserveTopupRequest reserveTopUpRequest) {
            super.onPostExecute(reserveTopUpRequest);
            //TODO we should add other items here too


            Intent intent = new Intent(getApplicationContext(), BuyResult.class);
            intent.putExtra("orderID", reserveTopUpRequest.get_reserveNumber());
            intent.putExtra("type", "charge");
            intent.putExtra("operator", operator);
            intent.putExtra("amount", amount);
            intent.putExtra("date", reserveTopUpRequest.get_date().toString());
            intent.putExtra("mobile", phone);
            intent.putExtra("refrenceNumber", reserveTopUpRequest.get_referenceNumber());


            if (reserveTopUpRequest.get_reserveNumber() > 0) {

                intent.putExtra("success", true);


            } else {
                intent.putExtra("success", false);


               /* Toast toast = Toast.makeText(getApplicationContext(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();*/
            }
            startActivity(intent);

        }
    }

    private class GetUserWallet extends AsyncTask<Void, Void, UserWallet> {

        UserWallet results = null;

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
        protected UserWallet doInBackground(Void... params) {
            results = new Caller().getUserWallet(userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(UserWallet userWallet) {
            super.onPostExecute(userWallet);
            //TODO we should add other items here too


            if (userWallet != null) {

                if (userWallet.get_balance() < Double.valueOf(amount)) {
                    payWallet.setEnabled(false);
                } else {
                    payWallet.setEnabled(true);
                    payWallet.setText(R.string.pay);
                    payWallet.setBackgroundResource(R.drawable.btn_yellow_background);
                    payWallet.setTextColor(Color.WHITE);
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
