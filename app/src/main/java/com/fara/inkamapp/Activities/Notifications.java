package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.fara.inkamapp.BottomSheetFragments.SubmitNewCard;
import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.ContactList;
import com.fara.inkamapp.Models.NotificationList;
import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class Notifications extends AppCompatActivity implements View.OnClickListener {

    private TextView messages, notify, toastText, unreadMessages, unreadNotifications, zero_size;
    private RecyclerView notifLists, msgList;
    private ImageButton back;
    private NotificationAdapter notificationAdapter, messagesAdapter;
    private SharedPreferences sharedPreferences;
    private String _userId, _token;


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
        initVariables();

        new getAllNotifications().execute();

    }

//    private void onBack(){
//        mListener.RefreshCard();
//    }

    private void initVariables() {
        notifLists = findViewById(R.id.rv_notifications);
        msgList = findViewById(R.id.rv_messages);
        back = findViewById(R.id.ib_back);
        unreadNotifications = findViewById(R.id.tv_has_notif);
        unreadMessages = findViewById(R.id.tv_has_messages);
        zero_size = findViewById(R.id.tv_no_msg);
        messages = findViewById(R.id.tv_messages);
        notify = findViewById(R.id.tv_notify);

        notify.setOnClickListener(this);
        back.setOnClickListener(this);
        messages.setOnClickListener(this);


        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        try {
            _userId = sharedPreferences.getString("UserID", null);
            _token = Base64.encode((RSA.encrypt(Objects.requireNonNull(sharedPreferences.getString("Token", null)), publicKey)));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_notify:
                notify.setBackgroundResource(R.drawable.green_rounded_background);
                notify.setTextColor(getResources().getColor(R.color.colorWhite));

                messages.setBackgroundResource(R.drawable.light_rounded_background);
                messages.setTextColor(getResources().getColor(R.color.colorBrown));

                notifLists.setVisibility(View.VISIBLE);
                msgList.setVisibility(View.GONE);

                new getAllNotifications().execute();

                break;
            case R.id.tv_messages:
                messages.setBackgroundResource(R.drawable.green_rounded_background);
                messages.setTextColor(getResources().getColor(R.color.colorWhite));

                notify.setBackgroundResource(R.drawable.light_rounded_background);
                notify.setTextColor(getResources().getColor(R.color.colorBrown));

                msgList.setVisibility(View.VISIBLE);
                notifLists.setVisibility(View.GONE);

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
            results = new Caller().getAllNotifications(_userId, _token);

            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<NotificationList> notifications) {
            super.onPostExecute(notifications);
            //TODO we should add other items here too

            if (notifications != null) {
                if (notifications.size() != 0) {

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    notifLists.setLayoutManager(layoutManager);
                    notificationAdapter = new NotificationAdapter(getApplicationContext(), notifications);
                    notifLists.setAdapter(notificationAdapter);

                    for (int i = 0; i < notifications.size(); i++) {
                        if (notifications.get(i).get_unRead() > 0) {
                            unreadNotifications.setVisibility(View.VISIBLE);
                            unreadNotifications.setText(Numbers.ToPersianNumbers(String.valueOf(notifications.get(i).get_unRead())));
                            new SeenAllNotifications().execute();
                        } else {
                            unreadNotifications.setVisibility(View.INVISIBLE);
                        }
                    }
                } else {
                    zero_size.setText(R.string.no_notif);
                    zero_size.setVisibility(View.VISIBLE);
                    notifLists.setVisibility(View.GONE);
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
            results = new Caller().getMessages(_userId, _token);

            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<NotificationList> messages) {
            super.onPostExecute(messages);
            //TODO we should add other items here too

            if (messages != null) {
                if (messages.size() != 0) {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    msgList.setLayoutManager(layoutManager);
                    messagesAdapter = new NotificationAdapter(getApplicationContext(), messages);
                    msgList.setAdapter(messagesAdapter);

                    for (int i = 0; i < messages.size(); i++) {
                        if (messages.get(i).get_unRead() > 0) {
                            unreadMessages.setVisibility(View.VISIBLE);
                            unreadMessages.setText(Numbers.ToPersianNumbers(String.valueOf(messages.get(i).get_unRead())));
                            new SeenAllMessages().execute();
                        } else {
                            unreadMessages.setVisibility(View.INVISIBLE);
                        }
                    }
                } else {
                    zero_size.setText(R.string.no_msg);
                    zero_size.setVisibility(View.VISIBLE);
                    msgList.setVisibility(View.GONE);
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

    private class SeenAllNotifications extends AsyncTask<Void, Void, ResponseStatus> {

        ResponseStatus results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ResponseStatus doInBackground(Void... params) {
            results = new Caller().seenAllNotifications(_userId, _token);


            return results;
        }

        @Override
        protected void onPostExecute(ResponseStatus responseStatus) {
            super.onPostExecute(responseStatus);
            //TODO we should add other items here too

            if (responseStatus != null && responseStatus.get_status().equals("SUCCESS")) {

                unreadNotifications.setVisibility(View.INVISIBLE);

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

    private class SeenAllMessages extends AsyncTask<Void, Void, ResponseStatus> {

        ResponseStatus results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ResponseStatus doInBackground(Void... params) {
            results = new Caller().seenAllMessages(_userId, _token);


            return results;
        }

        @Override
        protected void onPostExecute(ResponseStatus responseStatus) {
            super.onPostExecute(responseStatus);
            //TODO we should add other items here too

            if (responseStatus != null && responseStatus.get_status().equals("SUCCESS")) {

                unreadMessages.setVisibility(View.INVISIBLE);

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

