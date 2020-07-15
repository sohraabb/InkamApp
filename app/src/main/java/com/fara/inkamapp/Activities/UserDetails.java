package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Adapters.PresentorUserDetail;
import com.fara.inkamapp.Adapters.UserDetailsAdapter;
import com.fara.inkamapp.Fragments.Users;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Models.PercentageCode;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import java.util.ArrayList;
import java.util.List;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class UserDetails extends AppCompatActivity {
    private TextView toastText, tv_details_phone, tv_details_my_friend, tv_users_number;
    private String code;
    private RecyclerView rc_users;

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

        setContentView(R.layout.activity_user_details);
        rc_users = findViewById(R.id.rc_users);
        tv_details_phone = findViewById(R.id.tv_details_phone);
        tv_details_my_friend = findViewById(R.id.tv_details_my_friend);
        tv_users_number = findViewById(R.id.tv_users_number);
        String userCount = getIntent().getExtras().getString("userCount");
        code = getIntent().getExtras().getString("code");
        String name = getIntent().getExtras().getString("name");

        tv_users_number.setText(userCount);
        tv_details_my_friend.setText(name);
        tv_details_phone.setText(code);
        new GetUserByPresentageCode().execute();
    }


    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }


    private class GetUserByPresentageCode extends AsyncTask<Void, Void, List<User>> {

        List<User> results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected List<User> doInBackground(Void... params) {
            results = new Caller().GetUserByPresentageCode(MainActivity._userId, MainActivity._token, code);

            return results;
        }

        @Override
        protected void onPostExecute(List<User> percentageCodes) {
            super.onPostExecute(percentageCodes);
            //TODO we should add other items here too

            try {


                if (percentageCodes != null) {

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    rc_users.setLayoutManager(layoutManager);
                    PresentorUserDetail mAdapter = new PresentorUserDetail(getApplicationContext(), percentageCodes);
                    rc_users.setAdapter(mAdapter);

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.try_again, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toastText = toast.getView().findViewById(android.R.id.message);
                    toast.getView().setBackgroundResource(R.drawable.toast_background);

                    if (toastText != null) {
                        toastText.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/IRANSansMobile.ttf"));
                        toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                        toastText.setGravity(Gravity.CENTER);
                        toastText.setTextSize(14);
                    }
                    toast.show();
                }
            } catch (Exception e) {
                String s=e.toString();
            }
        }
    }

    public void backClicked(View v) {
        finish();
    }
}
