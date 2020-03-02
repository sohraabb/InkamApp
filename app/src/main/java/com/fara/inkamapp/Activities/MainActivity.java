package com.fara.inkamapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fara.inkamapp.Fragments.Dashboard;
import com.fara.inkamapp.Fragments.PaymentServices;
import com.fara.inkamapp.Fragments.Users;
import com.fara.inkamapp.Fragments.Wallet;
import com.fara.inkamapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Boolean isUserStartingFragment;

    final Fragment dashboardFragment = new Dashboard();
    final Fragment walletFragment = new Wallet();
    final Fragment usersFragment = new Users();
    final Fragment paymentServiceFragment = new PaymentServices();

    final FragmentManager fm = getSupportFragmentManager();
    Fragment fragmentActive = dashboardFragment;

    private TextView mTextMessage;
    private BottomNavigationView navView;
    private Menu menu;


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

                    Intent intent = new Intent(getApplicationContext(), IntroToUserFragment.class);
                    startActivity(intent);

                    return true;
                case R.id.navigation_payment_service:

                    fm.beginTransaction().hide(fragmentActive).show(paymentServiceFragment).commit();
                    fragmentActive = paymentServiceFragment;

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.main_container, paymentServiceFragment, "3").hide(paymentServiceFragment).commit();
        fm.beginTransaction().add(R.id.main_container, usersFragment, "3").hide(usersFragment).commit();
        fm.beginTransaction().add(R.id.main_container, walletFragment, "2").hide(walletFragment).commit();
        fm.beginTransaction().add(R.id.main_container, dashboardFragment, "1").commit();

        menu = navView.getMenu();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            isUserStartingFragment = getIntent().getExtras().getBoolean("isUserStarting");

        if (isUserStartingFragment != null) {
            if (!isUserStartingFragment) {
                fm.beginTransaction().hide(fragmentActive).show(dashboardFragment).commit();
                fragmentActive = dashboardFragment;
            } else {
                fm.beginTransaction().hide(fragmentActive).show(usersFragment).commit();
                fragmentActive = usersFragment;
                menu.findItem(R.id.navigation_users).setChecked(true);

            }
        }
    }

}