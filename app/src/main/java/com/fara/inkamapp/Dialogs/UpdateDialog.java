package com.fara.inkamapp.Dialogs;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.fara.inkamapp.Activities.MainActivity;
import com.fara.inkamapp.BuildConfig;
import com.fara.inkamapp.Helpers.NotificationHelper;
import com.fara.inkamapp.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class UpdateDialog extends AppCompatActivity {

    private Activity _activity;
    boolean back = false;
    private ProgressDialog progressDialog;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static String file_url = "http://mouse.ir/Income.apk";

    public void showDialog(Activity activity) {
        try {
            _activity = activity;
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_update_app);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


            Button update_button = dialog.findViewById(R.id.btn_update);
            Button cancel_button = dialog.findViewById(R.id.btn_cancel);

            update_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (back) {
                        Intent intent = new Intent(_activity, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        _activity.startActivity(intent);
                        finish();
                    } else {
//                        initViews();
//                        requestPermission();

//                        if (shouldAskPermissions())
//                            askPermissions();

                        if (ContextCompat.checkSelfPermission(_activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            // Do the file write
                            progressDialog = new ProgressDialog(_activity);
                            progressDialog.setMessage("در حال دانلود آپدیت برنامه");
                            progressDialog.setIndeterminate(true);
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            progressDialog.setCancelable(false);
                            new DownloadTask(_activity)
                                    .execute(file_url);
                        } else {
                            // Request permission from the user
                            ActivityCompat.requestPermissions(_activity,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                        }


//                        btnShowProgress.setText("درحال دریافت...");
                    }
                }
            });


            cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        } catch (Exception e) {
            e.toString();
        }
    }


    // usually, subclasses of AsyncTask are declared inside the activity class.
// that way, you can easily modify the UI thread from here
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/"
                        + "Income.apk");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            progressDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(context, R.string.download_completed, Toast.LENGTH_SHORT).show();
                back = true;
                new InstallLauncherAsync().execute();
            }
        }
    }


    // * Background Async Task to download file
    class DownloadFileFromURL extends AsyncTask<String, Integer, String> {

        private final NotificationHelper mNotificationHelper;

        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        int percentageComplete = 0;
        long fileLength;
        PendingIntent mContentIntent;
        NotificationManager mNotificationManager;
        private final int NOTIFICATION_ID = 1;
        long total;
        Notification mNotification;
        Long delay = Calendar.getInstance().getTimeInMillis();

        public DownloadFileFromURL(Context ctx) {
            mNotificationHelper = new NotificationHelper(ctx);


            progressDialog = new ProgressDialog(ctx);
            progressDialog.setMessage(ctx.getResources().getString(
                    R.string.downloading));
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, ctx
                            .getResources().getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            cancel(true);
                        }
                    });

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            mNotificationManager = mNotificationHelper.createNotification();

        }

        @Override
        protected void onCancelled() {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
                // running = false;
                connection.disconnect();
//                btnShowProgress.setText("بازگشت");
                back = true;
                progressDialog.dismiss();
                mNotificationHelper.completed();
            } catch (Exception ex) {

            }

        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            try {

                URL url = new URL(f_url[0]);
                connection = (HttpURLConnection) url.openConnection();
                // connection.setRequestMethod("GET");

                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP "
                            + connection.getResponseCode() + " "
                            + connection.getResponseMessage();
                }

                fileLength = connection.getContentLength();

                input = connection.getInputStream();

                File outputFile = new File(
                        Environment.getExternalStorageDirectory() + "/"
                                + "Income.apk");
                if (outputFile.exists()) {

                    outputFile.delete();
                }
                connection.setConnectTimeout(14000);
                connection.setReadTimeout(20000);

                input = new BufferedInputStream(connection.getInputStream());

                output = new FileOutputStream(
                        Environment.getExternalStorageDirectory() + "/"
                                + "Income.apk");

                byte data[] = new byte[4096];
                total = 0;
                int count = 0;

                while ((count = input.read(data, 0, 1024)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        output.close();
                        connection.disconnect();

                        return null;
                    }

                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) ((total * 100) / fileLength));
                    output.write(data, 0, count);

                }
                return "success...";

            } catch (Exception e) {
                if (connection != null)
                    connection.disconnect();
                return null;
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                    if (connection != null)
                        connection.disconnect();

                } catch (IOException ignored) {

                }

            }
        }

        /**
         * Updating progress bar
         **/
        @Override
        protected void onProgressUpdate(final Integer... progress) {
            final long[] startTime = new long[1];
            final long[] elapsedTime = {0L};

            Intent notificationIntent = new Intent();
            CharSequence contentText = progress[0] + "% complete";
//            PendingIntent.getActivity(UpdateActivity.this, 0,
//                    notificationIntent, 0);

            final Notification.Builder builder = new Notification.Builder(_activity)
                    .setSmallIcon(R.mipmap.logo_income)
                    .setContentTitle(_activity.getString(R.string.downloading))
                    .setContentText(contentText)
                    // .setContentIntent(mContentIntent)
                    ;
            mNotification = builder.build();
            if (Calendar.getInstance().getTimeInMillis() > delay) {

                builder.setProgress(100, (int) progress[0], false);
                mNotificationManager.notify(1, mNotification);
                progressDialog.setProgress(progress[0]);
                delay = Calendar.getInstance().getTimeInMillis() + 500;
            }

        }

        /**
         * After completing background task Dismiss the progress dialog
         ***/
        @Override
        protected void onPostExecute(String result) {
            // progressDialog.dismiss();
            progressDialog.setProgress(100);
            mNotificationHelper.completed();
//            btnShowProgress.setText("اتمام دریافت آپدیت جدید");
            back = true;
            if (result != null)
                new InstallLauncherAsync().execute();

        }

    }

    private class InstallLauncherAsync extends AsyncTask<Void, String, Boolean> {

        @Override
        protected Boolean doInBackground(Void... s) {
            try {

                File toInstall = new File(Environment.getExternalStorageDirectory() + "/"
                        + "Income.apk");
                Intent intent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri apkUri = FileProvider.getUriForFile(_activity, BuildConfig.APPLICATION_ID + ".provider", toInstall);
                    intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intent.setData(apkUri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    Uri apkUri = Uri.fromFile(toInstall);
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }

                _activity.startActivity(intent);


                return true;
            } catch (Exception e) {


                Log.e("harekat Exception", e.toString());
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean data) {

        }
    }

    @Override
    protected void onDestroy() {
        try {
//            btnShowProgress = null;

            progressDialog = null;
        } catch (Exception e) {


            Log.e("harekat Exception", e.toString());

        }
        super.onDestroy();
    }


    //Initialization Varibles

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(_activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(_activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                // Re-attempt file write
                progressDialog = new ProgressDialog(_activity);
                progressDialog.setMessage("در حال دانلود آپدیت برنامه");
                progressDialog.setIndeterminate(true);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                new DownloadTask(_activity)
                        .execute(file_url);


        }
    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

}