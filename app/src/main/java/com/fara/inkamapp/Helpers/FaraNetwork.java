package com.fara.inkamapp.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class FaraNetwork {
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            boolean isConnected = false;
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                isConnected = (activeNetworkInfo != null) && (activeNetworkInfo.isConnected());
            }
            return isConnected;
        } catch (Exception e) {
            return false;
        }
    }
}
