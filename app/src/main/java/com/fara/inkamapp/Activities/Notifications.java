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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Adapters.ContactsAdapter;
import com.fara.inkamapp.Adapters.NotificationAdapter;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Models.ContactList;
import com.fara.inkamapp.Models.NotificationList;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import java.util.ArrayList;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class Notifications extends AppCompatActivity implements View.OnClickListener {

    private TextView messages, notify, toastText;
    private RecyclerView notifLists, msgList;
    private ImageButton back;
    private NotificationAdapter notificationAdapter;

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

        setContentView(R.layout.activity_notifications);

        notifLists = findViewById(R.id.rv_notifications);
        msgList = findViewById(R.id.rv_messages);
        back = findViewById(R.id.ib_back);


        messages = findViewById(R.id.tv_messages);
        messages.setOnClickListener(this);
        notify = findViewById(R.id.tv_notify);
        notify.setOnClickListener(this);
        back.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_notify:
                notify.setBackgroundResource(R.drawable.green_rounded_background);
                notify.setTextColor(getResources().getColor(R.color.colorWhite));

                messages.setBackgroundResource(R.drawable.light_rounded_background);
                messages.setTextColor(getResources().getColor(R.color.colorBrown));

                new getAllNotifications().execute();

                break;
            case R.id.tv_messages:
                messages.setBackgroundResource(R.drawable.green_rounded_background);
                messages.setTextColor(getResources().getColor(R.color.colorWhite));

                notify.setBackgroundResource(R.drawable.light_rounded_background);
                notify.setTextColor(getResources().getColor(R.color.colorBrown));

                new getAllMessages().execute();

                break;

            case R.id.ib_back:
                onBackPressed();

                break;
        }
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    private class getAllNotifications extends AsyncTask<Void, Void, ArrayList<NotificationList>> {

        ArrayList<NotificationList> results = null;

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
        protected ArrayList<NotificationList> doInBackground(Void... params) {
            results = new Caller().getAllNotifications("2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00");

            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<NotificationList> notifications) {
            super.onPostExecute(notifications);
            //TODO we should add other items here too

            if (notifications != null) {

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                notifLists.setLayoutManager(layoutManager);
                notificationAdapter = new NotificationAdapter(getApplicationContext(), notifications);
                notifLists.setAdapter(notificationAdapter);

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

    private class getAllMessages extends AsyncTask<Void, Void, ArrayList<NotificationList>> {

        ArrayList<NotificationList> results = null;

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
        protected ArrayList<NotificationList> doInBackground(Void... params) {
            results = new Caller().getMessages("2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00");

            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<NotificationList> messages) {
            super.onPostExecute(messages);
            //TODO we should add other items here too

            if (messages != null) {

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                msgList.setLayoutManager(layoutManager);
                notificationAdapter = new NotificationAdapter(getApplicationContext(), messages);
                msgList.setAdapter(notificationAdapter);

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

