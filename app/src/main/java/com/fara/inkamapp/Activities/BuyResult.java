package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fara.inkamapp.BuildConfig;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BuyResult extends AppCompatActivity {
    private TextView tv_title, tv_failed, tv_amount_title, tv_amount, tv_amount_rial,
            tv_consistency_number_title, tv_consistency_number, tv_date,
            tv_destination_card_title, tv_destination_card, tv_own_card_title, tv_own_card, tv_operator, tv_package_title;
    private ImageView iv_failed;
    private RelativeLayout rv_last, rv_package, rl_line;
    private LinearLayout ll_factor;
    private ImageButton btn_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_buy_result);
        try {


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
            tv_own_card_title = findViewById(R.id.tv_own_card_title);
            tv_own_card = findViewById(R.id.tv_own_card);
            iv_failed = findViewById(R.id.iv_failed);
            tv_operator = findViewById(R.id.tv_operator);
            rv_last = findViewById(R.id.rv_last);
            rl_line = findViewById(R.id.rl_line);


            String type = getIntent().getStringExtra("type");
            String operator = getIntent().getStringExtra("operator");
            boolean success = getIntent().getBooleanExtra("success", false);
            NumberFormat formatter = new DecimalFormat("#,###");
            String formattedNumber = formatter.format(Double.valueOf(getIntent().getStringExtra("amount")));
            tv_amount.setText(Numbers.ToPersianNumbers(formattedNumber));
            String describe = getIntent().getStringExtra("describe");
            tv_date.setText(Numbers.ToPersianNumbers(getIntent().getStringExtra("date").substring(0, 16).replace("T", " ").replace("-", "/")));
            String mobile = getIntent().getStringExtra("mobile");
            String refNumber = String.valueOf(getIntent().getLongExtra("refrenceNumber", 0));

            if (success) {
                tv_failed.setText(R.string.success_payment);
                tv_failed.setTextColor(getResources().getColor(R.color.colorGreen));
                iv_failed.setImageResource(R.drawable.ic_circular_check);
            }
            switch (type) {
                case "charge":
                    tv_title.setText(R.string.buy_charge);
                    tv_amount_title.setText(R.string.charge);
                    switch (operator) {
                        case "0":
                            operator = "ایرانسل";
                            break;
                        case "1":
                            operator = "همراه اول";
                            break;
                        case "2":
                            operator = "رایتل";
                            break;
                    }
                    tv_operator.setText(operator);
                    tv_consistency_number_title.setText(R.string.mobile);
                    tv_consistency_number.setText(Numbers.ToPersianNumbers(mobile));
                    tv_destination_card_title.setText(R.string.reference_number);
                    tv_destination_card.setText(Numbers.ToPersianNumbers(refNumber));
                    rv_last.setVisibility(View.INVISIBLE);


                case "internet":
                    tv_title.setText(R.string.buy_packages);
                    rv_package.setVisibility(View.VISIBLE);
                    tv_package_title.setText(Numbers.ToPersianNumbers(describe));

                    tv_amount_title.setText(R.string.internet_package);

                    tv_operator.setVisibility(View.INVISIBLE);

                    tv_consistency_number.setText(Numbers.ToPersianNumbers(refNumber));
                    tv_destination_card_title.setVisibility(View.INVISIBLE);
                    tv_destination_card.setVisibility(View.INVISIBLE);
                    rv_last.setVisibility(View.INVISIBLE);

                    break;

                case "bill":
                    tv_title.setText(R.string.pay_bill);

                    tv_amount_title.setText(R.string.bill);

                    tv_operator.setVisibility(View.INVISIBLE);
                    rl_line.setVisibility(View.GONE);
                    tv_consistency_number.setText(Numbers.ToPersianNumbers(refNumber));
                    tv_destination_card_title.setVisibility(View.INVISIBLE);
                    tv_destination_card.setVisibility(View.INVISIBLE);
                    rv_last.setVisibility(View.INVISIBLE);

                    break;
                case "phone":

                    tv_title.setText(R.string.pay_bill);
                    tv_amount_title.setText(R.string.phone_bill);

                    tv_operator.setVisibility(View.INVISIBLE);
                    rl_line.setVisibility(View.GONE);
                    tv_consistency_number.setText(Numbers.ToPersianNumbers(refNumber));
                    tv_destination_card_title.setVisibility(View.INVISIBLE);
                    tv_destination_card.setVisibility(View.INVISIBLE);
                    rv_last.setVisibility(View.INVISIBLE);

                    break;
            }
            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takeScreenShotAndShare(ll_factor, false, "");
                }
            });
        } catch (Exception ex) {

        }
    }

    public void takeScreenShotAndShare(View view, boolean incText, String text) {
        try {

            File mPath = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "screenshot.png");
            //File imageDirectory = new File(mPath, "screenshot.png");

            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            FileOutputStream fOut = new FileOutputStream(mPath);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, fOut);
            fOut.flush();
            fOut.close();

            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
            Uri pictureUri = Uri.fromFile(mPath);
            shareIntent.setType("image/*");
            if (incText) {
                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            }
            shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",
                    new File("screenshot.png")));
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(shareIntent, "Share image using"));
        } catch (Throwable tr) {
            Log.d("TAG", "Couldn't save screenshot", tr);
        }
    }

    public void backClicke(View v) {
        finish();
    }
}
