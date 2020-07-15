package com.fara.inkamapp.Activities;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.HideKeyboard;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class EditUserProfile extends HideKeyboard implements View.OnClickListener {
    private TextView toastText, submit, requestPassword;
    private ImageButton back;
    private EditText firstName, lastName, city;
    private CircleImageView profileImage;
    private String _userId, _token, _username, _encodeImage = "", _cityID, _cityName, AesKey;
    private SharedPreferences sharedPreferences;
    private JSONObject user;
    private static final int SELECT_PHOTO = 100;
    private User userInfo;


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

        setContentView(R.layout.activity_edit_user_profile);
        initVariables();

        new GetUserInfo().execute();


    }

    private void initVariables() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        AesKey = sharedPreferences.getString("key", null);
        try {
            _userId = sharedPreferences.getString("UserID", null);
            _token = Base64.encode((RSA.encrypt(sharedPreferences.getString("Token", null), publicKey)));
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
        back = findViewById(R.id.ib_back);
        submit = findViewById(R.id.tv_submit);
        firstName = findViewById(R.id.et_first_name);
        lastName = findViewById(R.id.et_last_name);
        city = findViewById(R.id.et_your_city);
        profileImage = findViewById(R.id.choose_profile_image);
        requestPassword = findViewById(R.id.tv_new_pass);

        submit.setOnClickListener(this);
        back.setOnClickListener(this);
        requestPassword.setOnClickListener(this);
        profileImage.setOnClickListener(this);
        city.setOnClickListener(this);
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                new UpdateUser().execute();

                break;
            case R.id.choose_profile_image:

                if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                }


                break;

            case R.id.tv_new_pass:

                break;

            case R.id.et_your_city:
                Intent i = new Intent(getApplicationContext(), SearchProvince.class);
                startActivityForResult(i, 1);

                break;
            case R.id.ib_back:
                onBackPressed();

                break;
        }
    }

    private class GetUserInfo extends AsyncTask<Void, Void, User> {

        User results = null;

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
        protected User doInBackground(Void... params) {
            results = new Caller().getUserById(_userId, _token);
            userInfo = results;
            return results;
        }

        @Override
        protected void onPostExecute(User userInfo) {
            super.onPostExecute(userInfo);
            //TODO we should add other items here too

            if (userInfo != null) {
                if (!userInfo.getFirstName().equals("anyType{}") && userInfo.getFirstName() != null) {
                    firstName.setText(userInfo.getFirstName());
                    firstName.setBackgroundResource(R.drawable.edit_text_green_storke);
                }
                if (!userInfo.getLastName().equals("anyType{}") && userInfo.getLastName() != null) {
                    lastName.setText(userInfo.getLastName());
                    lastName.setBackgroundResource(R.drawable.edit_text_green_storke);
                }
                if (!userInfo.getCityName().equals("anyType{}") && userInfo.getCityName() != null) {
                    city.setText(userInfo.getCityName());
                    city.setBackgroundResource(R.drawable.edit_text_green_storke);

                }

                if (!userInfo.getProfilePicURL().equals("anyType{}") && userInfo.getProfilePicURL() != null) {
                    Picasso.with(getApplicationContext())
                            .load("http://" + userInfo.getProfilePicURL()).resize(100, 100).centerCrop().into(profileImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.i("Sohrab P", "Success");

                        }

                        @Override
                        public void onError() {

                        }

                    });
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


    private class UpdateUser extends AsyncTask<Void, Void, ResponseStatus> {

        ResponseStatus results = null;

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


            user = new JSONObject();
            try {
                userInfo.setFirstName(firstName.getText().toString());
                userInfo.setLastName(lastName.getText().toString());
                userInfo.setProfilePicURL(_encodeImage);
                userInfo.setCityID(_cityID);

                if (userInfo.getCreditCard() != null && !userInfo.getCreditCard().equals("")) {
                    String card = AESEncyption.decryptMsg(userInfo.getCreditCard(), AesKey);
                    userInfo.setCreditCard(Base64.encode(RSA.encrypt(card, publicKey)));
                }
                if (userInfo.getSheba() != null && !userInfo.getSheba().equals("")) {
                    String sheba = AESEncyption.decryptMsg(userInfo.getSheba(), AesKey);
                    userInfo.setSheba(Base64.encode(RSA.encrypt(sheba, publicKey)));
                }
               /* user.put("ID", sharedPreferences.getString("UserID", null));
                user.put("FirstName", firstName.getText().toString());
                user.put("LastName", lastName.getText().toString());
                user.put("UserName", sharedPreferences.getString("UserName", null));
                user.put("ProfilePicURL", _encodeImage);
                user.put("CityID", _cityID);*/


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ResponseStatus doInBackground(Void... params) {
            Gson g = new Gson();
            String u = g.toJson(userInfo);
            results = new Caller().updateUser(_token, u);

            return results;
        }

        @Override
        protected void onPostExecute(ResponseStatus responseStatus) {
            super.onPostExecute(responseStatus);
            //TODO we should add other items here too

            if (responseStatus != null && responseStatus.get_status().equals("SUCCESS")) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.user_success, Toast.LENGTH_SHORT);
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

    @Override
    protected void onResume() {
        super.onResume();
        setupUI(findViewById(R.id.ConstraintLayout));

    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bImage = baos.toByteArray();
        String encImage = Base64.encode(bImage);

        return encImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {

                        Uri selectedImage = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap profilePic = BitmapFactory.decodeStream(imageStream);
                        _encodeImage = encodeImage(profilePic);

                        Picasso.with(getApplicationContext())
                                .load(selectedImage).resize(100, 100).centerCrop().into(profileImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.i("Sohrab P", "Success");

                            }

                            @Override
                            public void onError() {

                            }

                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case 1:
                if (resultCode == RESULT_OK) {
                    _cityID = data.getStringExtra("CityID");
                    _cityName = data.getStringExtra("CityName");
                    city.setBackgroundResource(R.drawable.edit_text_green_storke);
                    city.setText(_cityName);
                }
                break;
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                } else {
                    Toast.makeText(EditUserProfile.this, "Access Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

}
