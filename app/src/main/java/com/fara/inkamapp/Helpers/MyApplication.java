package com.fara.inkamapp.Helpers;

import android.app.Application;
import im.crisp.client.Crisp;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Crisp.configure("98eaf367-67d2-4844-8685-f5e613094620");

    }
}