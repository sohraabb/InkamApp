package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Adapters.BusSeateSelectionAdapter;
import com.fara.inkamapp.BottomSheetFragments.AddNewPassenger;
import com.fara.inkamapp.BottomSheetFragments.Payment;
import com.fara.inkamapp.Fragments.AddPassenger;
import com.fara.inkamapp.Fragments.ChooseSeat;
import com.fara.inkamapp.Fragments.PassengerRecipte;
import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.BookTicket;
import com.fara.inkamapp.Models.Bus;
import com.fara.inkamapp.Models.BusContact;
import com.fara.inkamapp.Models.BusSeate;
import com.fara.inkamapp.Models.BusSummary;
import com.fara.inkamapp.Models.Passengers;
import com.fara.inkamapp.Models.TicketToBook;
import com.fara.inkamapp.Models.WalletCredit;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.top.lib.mpl.view.PaymentInitiator;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class BusTicketSelectionProcess extends AppCompatActivity implements
        AddNewPassenger.NewPassengerListener, ChooseSeat.AddSeatListener {
    private TextView toastText, title;
    private String busID,  orderId;
    private List<Fragment> fragments;
    private List<BusSeate> seats;
    private ArrayList<Passengers> passengers;
    private PassengerListNotifyListener mListener;
    private ViewPager pager;
    private BusSeateSelectionAdapter pageAdapter;
    private BusSummary busSummery;
    private Boolean isSwiapeable;
    private LinearLayout llOne, llTwo;
    private List<String> titles;
    private String toCity;
    JSONObject postData;
    private String destinationID, sourceID;
    private RelativeLayout rl_services_oval, rl_receipt_oval;
    private ImageView iv_services, iv_receipt;
    private String token, userID, encryptedToken, AesKey, dataToConfirm;
    private SharedPreferences sharedpreferences;
    double amount;
    private BottomSheetDialogFragment bottomSheetDialogFragment;

    @Override
    public void addSeat(List<BusSeate> seat) {
        this.seats = seat;
    }

    public interface PassengerListNotifyListener {
        void addToList(Passengers passenger);
    }

    public void setOnPassengerListNotifyListener(PassengerListNotifyListener listener) {
        mListener = listener;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titles = new ArrayList<>();
        titles.add(getString(R.string.choose_seat));
        titles.add(getString(R.string.passengers));
        titles.add(getString(R.string.travel_recipt));

        isSwiapeable = false;
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/IRANSansMobile.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
        setContentView(R.layout.activity_bus_ticket_selection_process);
        fragments = new ArrayList<>();
        seats = new ArrayList<>();
        busID = getIntent().getStringExtra("busID");
        toCity = getIntent().getStringExtra("toCity");
        sourceID = getIntent().getStringExtra("sourceID");
        destinationID = getIntent().getStringExtra("destinationID");
        passengers = new ArrayList<>();
        title = findViewById(R.id.tv_title2);
        Button btnContinue = findViewById(R.id.btn_continue);
        final LinearLayout llOne = findViewById(R.id.ll_line_1);
        final LinearLayout llTwo = findViewById(R.id.ll_line_2);
        rl_services_oval = findViewById(R.id.rl_services_oval);
        rl_receipt_oval = findViewById(R.id.rl_receipt_oval);
        iv_receipt = findViewById(R.id.iv_receipt);
        iv_services = findViewById(R.id.iv_services);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (pager.getCurrentItem()) {
                    case 0:
                        if (seats.size() == 0) {
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.choose_seat_please, Toast.LENGTH_SHORT);
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

                            ResizeWidthAnimation a = new ResizeWidthAnimation(llOne, ((View) llOne.getParent()).getWidth());
                            a.setDuration(500);
                            llOne.setVisibility(View.VISIBLE);
                            llOne.startAnimation(a);
                            a.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    rl_services_oval.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.green_oval_background));
                                    iv_services.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_service_white));

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                        }
                        break;
                    case 1:
                        if (passengers.size() == 0) {
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.complete_passenger_info, Toast.LENGTH_SHORT);
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
                            ResizeWidthAnimation a = new ResizeWidthAnimation(llTwo, ((View) llTwo.getParent()).getWidth());
                            a.setDuration(500);
                            llTwo.setVisibility(View.VISIBLE);
                            llTwo.startAnimation(a);
                            a.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    rl_receipt_oval.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.green_oval_background));
                                    iv_receipt.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_receipt_white));
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            // pager.setCurrentItem(pager.getCurrentItem() + 1, true);

                            fragments.add(new PassengerRecipte().newInstance(busSummery, passengers, toCity, seats.size()));
                            pageAdapter.notifyDataSetChanged();
                            pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                        }
                        break;
                    case 2:
                        isSwiapeable = true;
                        BusContact contact = new BusContact();
                        contact.setMobilePhone(passengers.get(0).getMobile());

                        contact.setName(passengers.get(0).getFirstName() + " " + passengers.get(0).getLastName());
                        while (passengers.size() < seats.size()) {
                            passengers.add(passengers.get(0));
                        }
                        for (int i = 0; i < passengers.size(); i++) {
                            passengers.get(i).setSeatNumber(Integer.parseInt(seats.get(i).getNumber()));
                        }
                        amount = busSummery.getFinancial().getPrice() - (busSummery.getFinancial().getPrice() * busSummery.getFinancial().getMaxApplicableDiscountPercentage() / 100);

                        amount = amount * seats.size();
                        TicketToBook book = new TicketToBook(
                                busID, busSummery.getFinancial().getMaxApplicableDiscountPercentage(),
                                passengers, contact, destinationID,sourceID , busSummery.getDepartureDate(),
                                amount, passengers.get(0).getMobile()
                        );


                        try {

                            Bundle bundle = new Bundle();
                            bundle.putString("busID", busID);
                            bundle.putFloat("busSummary", busSummery.getFinancial().getMaxApplicableDiscountPercentage());
                            bundle.putParcelableArrayList("passengers", passengers);
                            bundle.putParcelable("contact", contact);
                            bundle.putString("destinationID", destinationID);
                            bundle.putString("sourceID", sourceID);
                            bundle.putString("departureDate", busSummery.getDepartureDate());
                            bundle.putDouble("amount", amount);
                            bundle.putString("mobile", passengers.get(0).getMobile());
                            bundle.putInt("serviceType",5);


                            bottomSheetDialogFragment = Payment.newInstance("Bottom Sheet Payment Dialog");
                            bottomSheetDialogFragment.setArguments(bundle);
                            bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                        } catch (Exception e) {
                            e.toString();
                        }

                        break;
                }


            }
        });
        ImageButton ib_back = findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (pager.getCurrentItem()) {
                    case 0:
                        finish();
                        break;
                    case 1:
                        pager.setCurrentItem(0, true);
                        break;
                    case 2:
                        pager.setCurrentItem(1, true);
                        isSwiapeable = true;
                        break;
                }
            }
        });
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("Token", null);
        userID = sharedpreferences.getString("UserID", null);
        AesKey = sharedpreferences.getString("key", null);
        try {
            encryptedToken = Base64.encode((RSA.encrypt(token, publicKey)));
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
        new GetSpecificService().execute();


    }

    @Override
    public void newPassenger(Passengers passenger) {
        if (passenger == null) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.please_fil_info, Toast.LENGTH_SHORT);
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
            passengers.add(passenger);
            mListener.addToList(passenger);
        }
    }

    private class GetSpecificService extends AsyncTask<Void, Void, Bus> {

        Bus results = null;

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
        protected Bus doInBackground(Void... params) {


            results = new Caller().getSpecificService(userID, encryptedToken, busID);

            return results;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        protected void onPostExecute(final Bus bus) {
            super.onPostExecute(bus);
            //TODO we should add other items here too
            if (bus == null) {

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
            } else {
                try {
                    busSummery = bus;
                    fragments.add(new ChooseSeat().newInstance(getApplicationContext(), bus.getSeates()));
                    fragments.add(new AddPassenger().newInstance(passengers));

                    pageAdapter = new BusSeateSelectionAdapter(getSupportFragmentManager(), fragments);

                    pager = (ViewPager) findViewById(R.id.vp_progress);
                    pager.setRotationY(180);
                    pager.setAdapter(pageAdapter);
                    pager.setOnTouchListener(new View.OnTouchListener() {
                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {

                            return !isSwiapeable;
                        }
                    });

                    pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            title.setText(titles.get(position));
                            if (pager.getCurrentItem() < position) {
                                // handle swipe LEFT
                            } else if (pager.getCurrentItem() > position) {
                                // handle swipe RIGHT
                            }
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                } catch (Exception ex) {
                    String d = ex.toString();
                    Log.e("errorrrrrrrrr", ex.toString());
                }
            }
        }
    }

    private class BookBusTicket extends AsyncTask<String, Void, BookTicket> {

        BookTicket results = null;


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
        protected BookTicket doInBackground(String... params) {
            String res = new Caller().bookBusTicket(userID, encryptedToken, params[0]);
            try {
                String dec=   AESEncyption.decryptMsg(res, AesKey);
                Gson g= new Gson();
                results=  g.fromJson(dec,BookTicket.class);
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidParameterSpecException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(BookTicket bookTicket) {
            super.onPostExecute(bookTicket);
            //TODO we should add other items here too


            if (bookTicket.getToken() != null) {

                orderId = String.valueOf(bookTicket.getOrderID());
                Intent intent = new Intent(getApplicationContext(), PaymentInitiator.class);
                intent.putExtra("Type", "1");

                intent.putExtra("Token", bookTicket.getToken());
                intent.putExtra("OrderID", orderId);

                intent.putExtra("TSPEnabled", 1);

                startActivityForResult(intent, 1);



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

    private class BookBusTicketApprove extends AsyncTask<String, Void, WalletCredit> {

        WalletCredit results = null;


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
        protected WalletCredit doInBackground(String... params) {
            try {
                results = new Caller().busTicketSuccess(userID, encryptedToken,dataToConfirm,Base64.encode((RSA.encrypt(token, orderId))), Base64.encode((RSA.encrypt(token,String.valueOf( amount)))));
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

            return results;
        }

        @Override
        protected void onPostExecute(WalletCredit bookTicket) {
            super.onPostExecute(bookTicket);
            //TODO we should add other items here too


            if (bookTicket.get_status() != null && bookTicket.get_status().equals("0")) {



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

    private class BookTicketFromWallet extends AsyncTask<String, Void, BookTicket> {

        BookTicket results = null;


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
        protected BookTicket doInBackground(String... params) {
            String res = new Caller().bookTicketFromWallet(userID, encryptedToken, params[0]);
            try {
                String dec=   AESEncyption.decryptMsg(res, AesKey);
                Gson g= new Gson();
                results=  g.fromJson(dec,BookTicket.class);
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidParameterSpecException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(BookTicket bookTicket) {
            super.onPostExecute(bookTicket);
            //TODO we should add other items here too


            if (bookTicket.getID() != null && bookTicket.getID().length()>0) {



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

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        switch (pager.getCurrentItem()) {
            case 0:
                finish();
                break;
            case 1:
                pager.setCurrentItem(0, true);
                break;
            case 2:
                pager.setCurrentItem(1, true);
                isSwiapeable = true;
                break;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == 1) {
            dataToConfirm = data.getStringExtra("enData");
            if(!dataToConfirm.equals(null))
                dataToConfirm=    dataToConfirm.replace("\\", "");
            String one = data.getStringExtra("message");
            String sts = String.valueOf(data.getIntExtra("status", 0));
            if(sts.equals("0")) {
                new BookBusTicketApprove().execute();
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {

        }
        // }
    }//

    public static class ResizeWidthAnimation extends Animation {
        private int mWidth;
        private int mStartWidth;
        private View mView;

        public ResizeWidthAnimation(View view, int width) {
            mView = view;
            mWidth = width;
            mStartWidth = 1;// view.getWidth();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newWidth = mStartWidth + (int) ((mWidth - mStartWidth) * interpolatedTime);

            mView.getLayoutParams().width = newWidth;
            mView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
}
