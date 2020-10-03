package com.fara.inkamapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.multidex.MultiDex;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Adapters.NotificationAdapter;
import com.fara.inkamapp.BottomSheetFragments.AddExtraCredit;
import com.fara.inkamapp.BottomSheetFragments.NewID;
import com.fara.inkamapp.BottomSheetFragments.SubmitNewCard;
import com.fara.inkamapp.BottomSheetFragments.TransferCredit;
import com.fara.inkamapp.Fragments.Dashboard;
import com.fara.inkamapp.Fragments.PaymentServices;
import com.fara.inkamapp.Fragments.Users;
import com.fara.inkamapp.Fragments.Wallet;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.HideKeyboard;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.NotificationList;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.LoginInkam.publicKey;


public class MainActivity extends HideKeyboard implements AddExtraCredit.ItemClickListener, TransferCredit.TransferItemClickListener, SubmitNewCard.RefershCardListener, NewID.RefershIDListener {

    private Boolean isUserStartingFragment;

    final Fragment dashboardFragment = new Dashboard();
    final Fragment walletFragment = new Wallet();
    final Fragment usersFragment = new Users();
    final Fragment paymentServiceFragment = new PaymentServices();
//    final Fragment crispWebView = new CrispWebView();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment fragmentActive = dashboardFragment;

    private BottomNavigationView navView;
    private Menu menu;
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjL8NCgYgt7Y0Lg9OZUaziHSPusQoVpuHIkICjy7YI8yUlRBETmtNr9wdu61Wskz0PAQbj/TnCSXOhnhbWDormPk0GWyTjV/4Drrlx+hZtxPDgrYSwqscqoG2HWmWVlaqbAuVz4r/XMDbcy8zPy/ROGVey4uyGKj0hsA4p3O6YMwIDAQAB";

    private WalletClickListener mClickListener;
//    private UsersClickListener usersClickListener;

    private String AesKey;
    public static String _token, _userId, _userName;
    private TextView toastText;
    private int userFrag = 0;

    public void setClickListener(WalletClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

//    public void setClickListener(UsersClickListener itemClickListener) {
//        this.usersClickListener = itemClickListener;
//    }

    @Override
    public void onTransferItemClick() {
        ((Wallet) walletFragment).RefreshWallet();
    }

    @Override
    public void RefreshCard() {
        ((Wallet) walletFragment).RefreshCard();
    }

    @Override
    public void RefreshID() {
        ((Users) usersFragment).RefreshID();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
        MultiDex.install(this);

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


        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        fm.beginTransaction().add(R.id.main_container, paymentServiceFragment, "4").hide(paymentServiceFragment).commit();
        fm.beginTransaction().add(R.id.main_container, usersFragment, "3").hide(usersFragment).commit();
        fm.beginTransaction().add(R.id.main_container, walletFragment, "2").hide(walletFragment).commit();
        fm.beginTransaction().add(R.id.main_container, dashboardFragment, "1").commit();


        menu = navView.getMenu();
        menu.findItem(R.id.navigation_dashboard).setChecked(true);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove("ProfilePicURL").apply();
        _userId = sharedPreferences.getString("UserID", null);
        _userName = sharedPreferences.getString("UserName", null);
        AesKey = sharedPreferences.getString("key", null);
        try {
            if (publicKey == null) {
                publicKey = sharedPreferences.getString("publicKey", null);
            }
            _token = Base64.encode((RSA.encrypt(sharedPreferences.getString("Token", null), publicKey)));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        isUserStartingFragment = sharedPreferences.getBoolean("isUserStarting", false);

        userFrag = getIntent().getIntExtra("isUserAccepted", 0);
        if (userFrag == 1) {
            fm.beginTransaction().hide(fragmentActive).show(usersFragment).commit();
            fragmentActive = usersFragment;
            menu.findItem(R.id.navigation_users).setChecked(true);
        }


    }

    private void makeToast(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
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


    @Override
    public void onItemClick() {
        ((Wallet) walletFragment).RefreshWallet();
        ((Dashboard) dashboardFragment).refreshWallet();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.container));
    }


    // parent activity will implement this method to respond to click events
    public interface WalletClickListener {
        void onItemClick();
    }


//    public interface UsersClickListener {
//        void onItemClick();
//    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    fm.beginTransaction().hide(fragmentActive).show(dashboardFragment).commit();
                    fragmentActive = dashboardFragment;

                    return true;
                case R.id.navigation_wallet:

                    fm.beginTransaction().hide(fragmentActive).show(walletFragment).commit();
                    fragmentActive = walletFragment;

                    return true;
                case R.id.navigation_users:
                    if (sharedPreferences.getBoolean("isUserStarting", isUserStartingFragment)) {
                        fm.beginTransaction().hide(fragmentActive).show(usersFragment).commit();
                        fragmentActive = usersFragment;
                        menu.findItem(R.id.navigation_users).setChecked(true);

                    } else {
                        Intent intent = new Intent(getApplicationContext(), IntroToUserFragment.class);
                        startActivity(intent);
                    }

                    return true;
                case R.id.navigation_payment_service:

                    menu.findItem(R.id.navigation_payment_service).setChecked(false).setCheckable(false);
                    makeToast(getResources().getString(R.string.coming_soon));

//                    fm.beginTransaction().hide(fragmentActive).show(paymentServiceFragment).commit();
//                    fragmentActive = paymentServiceFragment;

                    return true;
            }
            return false;
        }
    };


}