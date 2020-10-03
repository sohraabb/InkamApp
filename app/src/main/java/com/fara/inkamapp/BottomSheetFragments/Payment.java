package com.fara.inkamapp.BottomSheetFragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fara.inkamapp.Activities.BuyResult;
import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.JalaliCalendar;
import com.fara.inkamapp.Helpers.JavaSource_Calendar;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.AirplainModels.AirReservation;
import com.fara.inkamapp.Models.AirplainModels.AirReservationRequest;
import com.fara.inkamapp.Models.ApproveInternetPackage;
import com.fara.inkamapp.Models.BookTicket;
import com.fara.inkamapp.Models.BusContact;
import com.fara.inkamapp.Models.Passengers;
import com.fara.inkamapp.Models.PayInfo;
import com.fara.inkamapp.Models.PaymentResult;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.Models.TicketToBook;
import com.fara.inkamapp.Models.UserWallet;
import com.fara.inkamapp.Models.WalletCredit;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianDateParser;
import com.top.lib.mpl.view.PaymentInitiator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;
import static com.fara.inkamapp.Activities.LoginInkam.publicKey;

public class Payment extends BottomSheetDialogFragment {

    String string;
    private Button walletPay, pay;
    private String _amountWithTax, _amountWithoutTax, _operator, _type, _phone, _token, _userID, _aesKey, encryptedToken, _orderID, _dataToConfirm, amount,
            _billID, _paymentID, _term, _plateNumber, _username, _describe, _city, _location, _datetime, _busID, _destinationID, _departureDate, _sourceID, dataToConfirm, _dataPlanType;
    private Float _busSummary;
    private ArrayList<Passengers> _passengers;
    private Double _amount;
    private BusContact _contact;
    private ImageView logo;
    private TextView price, phoneNumber, toastText;
    private SharedPreferences sharedPreferences;
    private JSONObject walletData, payData;
    private int serviceType, approveType, chargeKind;
    private long orderID;
    private long mLastClickTime = 0;

    public static Payment newInstance(String string) {
        Payment paymentDialog = new Payment();
        Bundle args = new Bundle();
        args.putString("string", string);
        paymentDialog.setArguments(args);
        return paymentDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        string = getArguments().getString("string");
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_payment, container,
                false);

        price = view.findViewById(R.id.tv_price);
//        priceWithTax = view.findViewById(R.id.tv_price_tax_value);
        logo = view.findViewById(R.id.iv_logo);
        phoneNumber = view.findViewById(R.id.tv_phone_number);
        walletPay = view.findViewById(R.id.btn_pay_wallet);
        pay = view.findViewById(R.id.btn_pay_online);

        initProduct();

        new GetUserWallet().execute();


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (serviceType) {
                    case 0:
                        payData = new JSONObject();
                        try {
                            payData.put("Amount", _amountWithTax);
                            payData.put("CellNumbers", Base64.encode(RSA.encrypt(_phone, publicKey)));
                            payData.put("CellNumber", _username);
                            payData.put("ChargeType", _type);
                            payData.put("BankId", "08");
                            payData.put("DeviceType", "59");
                            payData.put("Operator", _operator);
                            payData.put("Merchant", "اینکام");
                            approveType = 0;


                        } catch (JSONException | NoSuchPaddingException | NoSuchAlgorithmException |
                                InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {

                            e.printStackTrace();
                        }
                        new TopUpRequest().execute();

                        break;
                    case 1:
                    case 2:
                    case 3:
                        payData = new JSONObject();
                        try {
                            payData.put("Amount", _amountWithTax);
                            payData.put("BillID", _billID);
                            payData.put("City", _city);
                            payData.put("DateTime", _datetime);
                            payData.put("Location", _location);
                            payData.put("PaymentID", _paymentID);
                            payData.put("Type", _type);
                            payData.put("Merchant", "اینکام");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new GetBillToken().execute();
                        break;
                    case 4:
                        payData = new JSONObject();
                        try {
                            payData.put("Amount", _amountWithTax);
                            payData.put("CellNumbers", Base64.encode(RSA.encrypt(_phone, publicKey)));
                            payData.put("CellNumber", _username);
                            payData.put("ChargeType", _type);
                            payData.put("BankId", "08");
                            payData.put("DeviceType", "59");
                            payData.put("Operator", _operator);
                            payData.put("Merchant", "اینکام");
                            approveType = 1;

                        } catch (JSONException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                            e.printStackTrace();
                        }

                        new InternetPackageReserve().execute();

                        break;
                    case 5:

                        TicketToBook book = new TicketToBook(
                                _busID, _busSummary,
                                _passengers, _contact, _destinationID, _sourceID, _departureDate,
                                Double.parseDouble(_amountWithTax), _phone
                        );

                        Gson gson = new Gson();
                        String json = gson.toJson(book);
                        new BookBusTicket().execute(json);

                        break;
                    case 6:
                        //airplane
                        new AirplaneTicketRequestForApp().execute(getArguments().getString("airplane"));
                        break;
                    case 7:
                        break;

                    default:
                        break;
                }

            }
        });

        walletPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walletPay.setEnabled(false);

                switch (serviceType) {
                    case 0:
                        walletData = new JSONObject();
                        try {
                            walletData.put("Amount", _amountWithTax);
                            walletData.put("CellNumbers", Base64.encode(RSA.encrypt(_phone, publicKey)));
                            walletData.put("CellNumber", _username);
                            walletData.put("ChargeType", _type);
                            walletData.put("BankId", "08");
                            walletData.put("DeviceType", "59");
                            walletData.put("Operator", _operator);
                            walletData.put("Merchant", "اینکام");

                        } catch (JSONException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                            e.printStackTrace();
                        }

                        new TopUpRequestForWallet(chargeKind).execute();

                        break;
                    case 1:
                    case 3:
                        walletData = new JSONObject();
                        try {
                            walletData.put("Amount", _amountWithTax);
                            walletData.put("BillID", _billID);
                            walletData.put("City", _city);
                            walletData.put("DateTime", _datetime);
                            walletData.put("Location", _location);
                            walletData.put("PaymentID", _paymentID);
                            walletData.put("Type", _type);
                            walletData.put("Merchant", "اینکام");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new BillPaymentFromWallet().execute();
                        break;
                    case 2:
                        walletData = new JSONObject();
                        try {
                            walletData.put("Amount", _amountWithTax);
                            walletData.put("BillID", _billID);
                            walletData.put("City", _city);
                            walletData.put("DateTime", _datetime);
                            walletData.put("Location", _location);
                            walletData.put("PaymentID", _paymentID);
                            walletData.put("Type", _type);
                            walletData.put("Merchant", "اینکام");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new TrafficBillPaymentFromWallet().execute();
                        break;

                    case 4:
                        walletData = new JSONObject();
                        try {
                            walletData.put("Amount", _amountWithTax);
                            walletData.put("CellNumbers", Base64.encode(RSA.encrypt(_phone, publicKey)));
                            walletData.put("CellNumber", _username);
                            walletData.put("ChargeType", _type);
                            walletData.put("BankId", "08");
                            walletData.put("DeviceType", "59");
                            walletData.put("Operator", _operator);
                            walletData.put("Merchant", "اینکام");

                        } catch (JSONException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                            e.printStackTrace();
                        }

                        new TopUpRequestForWallet(1).execute();

                        break;
                    case 5:

                        TicketToBook book = new TicketToBook(
                                _busID, _busSummary,
                                _passengers, _contact, _destinationID, _sourceID, _departureDate,
                                _amount, _phone
                        );

                        Gson gson = new Gson();
                        String json = gson.toJson(book);
                        new BookTicketFromWallet().execute(json);

                        break;
                    case 6:

                        //airplane

                        new BookAirplaneTicketFromWallet().execute(getArguments().getString("airplane"));
                        break;
                    case 7:
                        break;

                    default:
                        break;
                }

            }
        });

        return view;
    }

    private void initProduct() {
        NumberFormat formatter = new DecimalFormat("#,###");
        walletPay.setEnabled(true);
        sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        _token = sharedPreferences.getString("Token", null);
        _userID = sharedPreferences.getString("UserID", null);
        _aesKey = sharedPreferences.getString("key", null);
        _username = sharedPreferences.getString("UserName", null);
        amount = getArguments().getString("amount");
        _amountWithTax = amount;
        if (getArguments() != null) {
            serviceType = getArguments().getInt("serviceType");
            switch (serviceType) {
                case 0:
                    _amountWithTax = getArguments().getString("amount");
                    _operator = getArguments().getString("operator");
                    _type = getArguments().getString("type");
                    _phone = getArguments().getString("phone");
                    chargeKind = getArguments().getInt("chargeKind");

                    if (chargeKind == 2)
                        _type = "1";

                    switch (_operator) {
                        case "0":
                            logo.setImageResource(R.drawable.irancell_logo_green);
                            break;
                        case "1":
                            logo.setImageResource(R.drawable.hamrah_aval_logo_green);
                            break;
                        case "2":
                            logo.setImageResource(R.drawable.rightel_logo);
                            break;
                    }

                    phoneNumber.setText(Numbers.ToPersianNumbers(_phone));
                    price.setText(Numbers.ToPersianNumbers(formatter.format(Double.parseDouble(_amountWithTax))));
//                    priceWithTax.setText(Numbers.ToPersianNumbers(_amountWithoutTax));
                    logo.setVisibility(View.INVISIBLE);

                    break;
                case 1:
                    _amountWithTax = getArguments().getString("billAmount");
                    _billID = getArguments().getString("billID");
                    _paymentID = getArguments().getString("paymentID");
                    _phone = getArguments().getString("phoneNumber");
                    _term = getArguments().getString("termTitle");

                    phoneNumber.setText(Numbers.ToPersianNumbers(_phone));
                    price.setText(Numbers.ToPersianNumbers(formatter.format(Double.parseDouble(_amountWithTax))));
//                    priceWithTax.setText(Numbers.ToPersianNumbers(_amountWithoutTax));
                    logo.setVisibility(View.INVISIBLE);

                    break;
                case 2:
                    _amountWithTax = getArguments().getString("amount");
                    _billID = getArguments().getString("billID");
                    _paymentID = getArguments().getString("paymentID");
                    _plateNumber = getArguments().getString("plateNumber");
                    _city = getArguments().getString("city");
                    _location = getArguments().getString("location");
                    _datetime = getArguments().getString("datetime");
                    _type = getArguments().getString("type");


                    price.setText(Numbers.ToPersianNumbers(formatter.format(Double.parseDouble(_amountWithTax))));
//                    priceWithTax.setText(Numbers.ToPersianNumbers(_amountWithoutTax));
                    logo.setVisibility(View.INVISIBLE);

                    break;
                case 3:
                    _amountWithTax = getArguments().getString("amount");
                    _billID = getArguments().getString("BillID");
                    _city = getArguments().getString("City");
                    _datetime = getArguments().getString("DateTime");
                    _location = getArguments().getString("Location");
                    _paymentID = getArguments().getString("PaymentID");
                    _type = getArguments().getString("Type");

                    price.setText(Numbers.ToPersianNumbers(formatter.format(Double.parseDouble(_amountWithTax))));
//                    priceWithTax.setText(Numbers.ToPersianNumbers(_amountWithTax));
                    logo.setVisibility(View.INVISIBLE);
                    break;
                case 4:

                    _amountWithoutTax = getArguments().getString("amountWithoutTax");
                    _amountWithTax = getArguments().getString("amountWithTax");
                    _operator = getArguments().getString("operator");
                    _type = getArguments().getString("type");
                    _phone = getArguments().getString("phone");
                    _describe = getArguments().getString("describe");
                    _dataPlanType = getArguments().getString("dataPlanType");

                    switch (_operator) {
                        case "0":
                            logo.setImageResource(R.drawable.irancell_logo_green);
                            break;
                        case "1":
                            logo.setImageResource(R.drawable.hamrah_aval_logo_green);
                            break;
                        case "2":
                            logo.setImageResource(R.drawable.rightel_logo);
                            break;
                    }

                    phoneNumber.setText(Numbers.ToPersianNumbers(_phone));
                    price.setText(Numbers.ToPersianNumbers(formatter.format(Double.parseDouble(_amountWithTax))));
//                    priceWithTax.setText(Numbers.ToPersianNumbers(_amountWithTax));

                    break;
                case 5:
                    _amountWithTax = String.valueOf(getArguments().getDouble("amount"));
                    _busID = getArguments().getString("busID");
                    _busSummary = getArguments().getFloat("busSummary");
                    _passengers = getArguments().getParcelableArrayList("passengers");
                    _contact = getArguments().getParcelable("contact");
                    _destinationID = getArguments().getString("destinationID");
                    _sourceID = getArguments().getString("sourceID");
                    _departureDate = getArguments().getString("departureDate");
                    _phone = getArguments().getString("mobile");

                    price.setText(Numbers.ToPersianNumbers(formatter.format(Double.valueOf(_amountWithTax))));
//                    priceWithTax.setText(Numbers.ToPersianNumbers(_amountWithTax));
                    phoneNumber.setText(Numbers.ToPersianNumbers(_phone));
                    logo.setVisibility(View.INVISIBLE);
                    break;
                case 6:
                    break;
                case 7:
                    break;

            }

            _amountWithTax = _amountWithTax != null ? Numbers.ToEnglishNumbers(_amountWithTax.replace(",", "")) : "";
            try {
                encryptedToken = Base64.encode((RSA.encrypt(_token, publicKey)));
            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getActivity());
    }

    private class TrafficBillPaymentFromWallet extends AsyncTask<Void, Void, String> {

        String results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected String doInBackground(Void... params) {

            results = new Caller().trafficBillPaymentFromWallet(walletData.toString(), _plateNumber, _userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            //TODO we should add other items here too


            if (response != null) {


            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
                walletPay.setEnabled(true);
            }

        }
    }

    private class GetUserWallet extends AsyncTask<Void, Void, UserWallet> {

        UserWallet results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected UserWallet doInBackground(Void... params) {
            results = new Caller().getUserWallet(_userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(UserWallet userWallet) {
            super.onPostExecute(userWallet);
            //TODO we should add other items here too


            if (userWallet != null) {

                if (userWallet.get_balance() >= Double.parseDouble(_amountWithTax)) {
                    walletPay.setBackgroundResource(R.drawable.btn_yellow_background);
                    walletPay.setEnabled(true);
                } else {
                    walletPay.setBackgroundResource(R.drawable.button_background_disabled);
                    walletPay.setEnabled(false);
                }

            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }
    }

    private class TopUpChargeRequestForWallet extends AsyncTask<Void, Void, ReserveTopupRequest> {

        ReserveTopupRequest results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected ReserveTopupRequest doInBackground(Void... params) {
            results = new Caller().topupServiceReserveFromWallet(_userID, encryptedToken, walletData.toString(), 0);

            return results;
        }

        @Override
        protected void onPostExecute(ReserveTopupRequest reserveTopUpRequest) {
            super.onPostExecute(reserveTopUpRequest);
            //TODO we should add other items here too


            Intent intent = new Intent(getActivity(), BuyResult.class);
            intent.putExtra("orderID", reserveTopUpRequest.get_reserveNumber());
            intent.putExtra("type", "charge");
            intent.putExtra("operator", _operator);
            intent.putExtra("amount", _amountWithTax);
            intent.putExtra("date", reserveTopUpRequest.get_date());
            intent.putExtra("mobile", _phone);
            intent.putExtra("refrenceNumber", String.valueOf(reserveTopUpRequest.get_referenceNumber()));


            if (reserveTopUpRequest.get_reserveNumber() > 0) {

                intent.putExtra("success", true);

            } else {
                intent.putExtra("success", false);

            }
            startActivity(intent);

        }
    }

    private class TopUpRequest extends AsyncTask<Void, Void, ReserveTopupRequest> {

        ReserveTopupRequest results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected ReserveTopupRequest doInBackground(Void... params) {
            results = new Caller().topupServiceReserve(_userID, encryptedToken, payData.toString(), Integer.parseInt(_type));

            return results;
        }

        @Override
        protected void onPostExecute(ReserveTopupRequest reserveTopUpRequest) {
            super.onPostExecute(reserveTopUpRequest);
            //TODO we should add other items here too


            if (reserveTopUpRequest.get_token() != null) {
                try {
                    _orderID = AESEncyption.decryptMsg(reserveTopUpRequest.get_reserveNumberString(), _aesKey);
                    Intent intent = new Intent(getActivity(), PaymentInitiator.class);
                    intent.putExtra("Type", "1");
                    intent.putExtra("Token", AESEncyption.decryptMsg(reserveTopUpRequest.get_token(), _aesKey));
                    intent.putExtra("OrderID", _orderID);

                    startActivityForResult(intent, 1);
                } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidParameterSpecException |
                        InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                        IllegalBlockSizeException | UnsupportedEncodingException e) {

                    e.printStackTrace();
                }


            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }
    }

    private class approveTopUpRequest extends AsyncTask<Void, Void, ReserveTopupRequest> {

        ReserveTopupRequest results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected ReserveTopupRequest doInBackground(Void... params) {
            try {
                results = new Caller().approveTopupServiceReserve(_userID, encryptedToken, _dataToConfirm, Base64.encode((RSA.encrypt(_orderID, publicKey))), Base64.encode((RSA.encrypt(_amountWithTax, publicKey))), Integer.parseInt(_type));
            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(ReserveTopupRequest reserveTopUpRequest) {
            super.onPostExecute(reserveTopUpRequest);
            //TODO we should add other items here too
            Intent intent = new Intent(getActivity(), BuyResult.class);
            intent.putExtra("orderID", reserveTopUpRequest.get_reserveNumber());
            intent.putExtra("type", "charge");
            intent.putExtra("operator", _operator);
            intent.putExtra("amount", _amountWithTax);
            intent.putExtra("date", reserveTopUpRequest.get_date());
            intent.putExtra("mobile", _phone);
            intent.putExtra("refrenceNumber", String.valueOf(reserveTopUpRequest.get_referenceNumber()));
            intent.putExtra("chargeType", _type);


            if (reserveTopUpRequest.get_reserveNumber() > 0)
                intent.putExtra("success", true);
            else
                intent.putExtra("success", false);

            startActivity(intent);

        }
    }

    private class approveInternetTopUpRequest extends AsyncTask<Void, Void, ApproveInternetPackage> {

        ApproveInternetPackage results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected ApproveInternetPackage doInBackground(Void... params) {
            try {
                results = new Caller().InternetPackageApprove(_userID, encryptedToken, _dataToConfirm, Base64.encode((RSA.encrypt(_orderID, publicKey))), Base64.encode((RSA.encrypt(_amountWithTax, publicKey))));
            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException |
                    NoSuchPaddingException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(ApproveInternetPackage reserveTopUpRequest) {
            super.onPostExecute(reserveTopUpRequest);
            //TODO we should add other items here too
            try {
                JavaSource_Calendar javaSource_calendar = new JavaSource_Calendar();

                Intent intent = new Intent(getActivity(), BuyResult.class);
                intent.putExtra("orderID", reserveTopUpRequest.get_orderId());
                intent.putExtra("type", "internet");
                intent.putExtra("operator", _operator);
                intent.putExtra("amount", _amountWithTax);
                intent.putExtra("date", javaSource_calendar.getIranianDate());
                intent.putExtra("describe", _describe);
                intent.putExtra("dataplanType", _dataPlanType);
                intent.putExtra("mobile", _phone);
                intent.putExtra("refrenceNumber", String.valueOf(reserveTopUpRequest.get_refrenceNumber()));
                intent.putExtra("chargeType", "internet");


                if (reserveTopUpRequest.get_Message().equals("تراکنش موفق"))
                    intent.putExtra("success", true);
                else
                    intent.putExtra("success", false);

                startActivity(intent);
            } catch (Exception ignored) {
                String s = ignored.toString();
            }
        }
    }

    private class GetBillToken extends AsyncTask<Void, Void, PaymentResult> {

        PaymentResult results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected PaymentResult doInBackground(Void... params) {


            String res = new Caller().GetBillToken(_userID, encryptedToken, Long.parseLong(_username.substring(1)), Long.parseLong(_paymentID), Long.parseLong(_billID));
            try {
                Gson gson = new Gson();
                results = gson.fromJson(AESEncyption.decryptMsg(res, _aesKey), PaymentResult.class);


            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidParameterSpecException |
                    InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                    IllegalBlockSizeException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(PaymentResult serviceBillInfo) {
            super.onPostExecute(serviceBillInfo);
            //TODO we should add other items here too


            if (serviceBillInfo != null) {


                orderID = serviceBillInfo.getOrderID();
                Intent intent = new Intent(getActivity(), PaymentInitiator.class);
                intent.putExtra("Type", "2");
                intent.putExtra("Token", serviceBillInfo.getData().getToken());

                startActivityForResult(intent, 1);


            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }
        }
    }

    private class BillPaymentFromWallet extends AsyncTask<Void, Void, String> {

        String results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected String doInBackground(Void... params) {

            JSONObject postData = new JSONObject();
            try {
                postData.put("Amount", _amountWithTax);
                postData.put("BillID", _billID);
                postData.put("PayId", _paymentID);
                postData.put("OrderId", "0");

            } catch (Exception e) {
                e.toString();
            }
            results = new Caller().BillPaymentFromWallet(postData.toString(), _userID, encryptedToken);

            return results;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            //TODO we should add other items here too


            if (response != null) {
                JavaSource_Calendar javaSource_calendar = new JavaSource_Calendar();
                Intent intent = new Intent(getActivity(), BuyResult.class);

                intent.putExtra("type", "bill");
                intent.putExtra("amount", _amountWithTax);
                intent.putExtra("billID", _billID);
                if (_datetime != null)
                    intent.putExtra("date", _datetime);
                else
                    intent.putExtra("date", javaSource_calendar.getIranianDate());

                if (_phone != null && _phone.equals(""))
                    intent.putExtra("phoneNum", _phone);

                if (response.equals("تراکنش موفق"))
                    intent.putExtra("success", true);
                else
                    intent.putExtra("success", false);

                startActivity(intent);

            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
                walletPay.setEnabled(true);
            }

        }
    }

    private class InternetPackageReserve extends AsyncTask<Void, Void, ReserveTopupRequest> {

        ReserveTopupRequest results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }


        @Override
        protected ReserveTopupRequest doInBackground(Void... params) {
            results = new Caller().internetPackageReserve(_userID, encryptedToken, payData.toString());

            return results;
        }

        @Override
        protected void onPostExecute(ReserveTopupRequest reserveTopUpRequest) {
            super.onPostExecute(reserveTopUpRequest);
            //TODO we should add other items here too

            if (reserveTopUpRequest.get_token() != null) {
                _orderID = String.valueOf(reserveTopUpRequest.get_reserveNumber());
                Intent intent = new Intent(getActivity(), PaymentInitiator.class);
                intent.putExtra("Type", "1");
                try {
                    intent.putExtra("Token", AESEncyption.decryptMsg(reserveTopUpRequest.get_token(), _aesKey));
                } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidParameterSpecException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                intent.putExtra("OrderID", _orderID);
                intent.putExtra("TSPEnabled", 1);

                startActivityForResult(intent, 1);

            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }
    }

    private class TopUpRequestForWallet extends AsyncTask<Void, Void, ReserveTopupRequest> {

        ReserveTopupRequest results = null;
        private int typeCharge;

        public TopUpRequestForWallet(int type) {
            typeCharge = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected ReserveTopupRequest doInBackground(Void... params) {
            results = new Caller().topupServiceReserveFromWallet(_userID, encryptedToken, walletData.toString(), typeCharge);

            return results;
        }

        @Override
        protected void onPostExecute(ReserveTopupRequest reserveTopUpRequest) {
            super.onPostExecute(reserveTopUpRequest);
            //TODO we should add other items here too


            if (reserveTopUpRequest != null) {
                JavaSource_Calendar javaSource_calendar = new JavaSource_Calendar();
                Intent intent = new Intent(getActivity(), BuyResult.class);

                if (typeCharge == 1)
                    intent.putExtra("type", "internet");
                else
                    intent.putExtra("type", "charge");

                intent.putExtra("operator", _operator);
                intent.putExtra("amount", _amountWithTax);
                intent.putExtra("describe", _describe);
                intent.putExtra("mobile", _phone);
                intent.putExtra("date", javaSource_calendar.getIranianDate());
                intent.putExtra("refrenceNumber", String.valueOf(reserveTopUpRequest.get_referenceNumber()));
                intent.putExtra("dataPlanType", _dataPlanType);

                if (reserveTopUpRequest.get_referenceNumber() > 0 && reserveTopUpRequest.get_responseCode() == 0) {
                    intent.putExtra("success", true);

                } else {
                    intent.putExtra("success", false);
                }
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
                walletPay.setEnabled(true);
            }
        }
    }

    private class BillSuccess extends AsyncTask<Void, Void, PayInfo> {

        PayInfo results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected PayInfo doInBackground(Void... params) {
            try {
                String res = new Caller().BillSuccess(_userID, encryptedToken, _dataToConfirm, Base64.encode((RSA.encrypt(String.valueOf(orderID), publicKey))), Base64.encode((RSA.encrypt(_amountWithTax, publicKey))));
                String json = AESEncyption.decryptMsg(res, _aesKey);
                Gson g = new Gson();
                results = g.fromJson(json, PayInfo.class);


            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException |
                    NoSuchAlgorithmException | UnsupportedEncodingException | InvalidAlgorithmParameterException |
                    InvalidParameterSpecException e) {

                e.printStackTrace();
            }
            return results;
        }

        @Override
        protected void onPostExecute(PayInfo data) {
            super.onPostExecute(data);
            //TODO we should add other items here too
            Intent intent = new Intent(getActivity(), BuyResult.class);

            intent.putExtra("type", "phone");
            intent.putExtra("amount", _amountWithTax);
            intent.putExtra("date", data.getPayDTime());
            intent.putExtra("refrenceNumber", String.valueOf(data.getTrcNo()));
            intent.putExtra("phoneNum", _phone);

            if (data.getStatus() == 0)
                intent.putExtra("success", true);
            else
                intent.putExtra("success", false);

            startActivity(intent);

        }
    }

    private class BookTicketFromWallet extends AsyncTask<String, Void, BookTicket> {

        BookTicket results = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }


        @Override
        protected BookTicket doInBackground(String... params) {
            String res = new Caller().bookTicketFromWallet(_userID, encryptedToken, params[0]);
            try {
                String dec = AESEncyption.decryptMsg(res, _aesKey);
                Gson g = new Gson();
                results = g.fromJson(dec, BookTicket.class);
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidParameterSpecException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(BookTicket bookTicket) {
            super.onPostExecute(bookTicket);
            //TODO we should add other items here too


            if (bookTicket.getID() != null && bookTicket.getID().length() > 0) {


            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
                walletPay.setEnabled(true);
            }

        }
    }

    private class BookBusTicket extends AsyncTask<String, Void, BookTicket> {

        BookTicket results = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected BookTicket doInBackground(String... params) {
            String res = new Caller().bookBusTicket(_userID, encryptedToken, params[0]);
            try {
                String dec = AESEncyption.decryptMsg(res, _aesKey);
                Gson g = new Gson();
                results = g.fromJson(dec, BookTicket.class);
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidParameterSpecException |
                    InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(BookTicket bookTicket) {
            super.onPostExecute(bookTicket);
            //TODO we should add other items here too


            if (bookTicket.getToken() != null) {

                _orderID = String.valueOf(bookTicket.getOrderID());
                Intent intent = new Intent(getActivity(), PaymentInitiator.class);
                intent.putExtra("Type", "1");

                intent.putExtra("Token", bookTicket.getToken());
                intent.putExtra("OrderID", _orderID);


                startActivityForResult(intent, 1);


            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }
    }

    /*private class BookBusTicketApprove extends AsyncTask<String, Void, WalletCredit> {

        WalletCredit results = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getContext(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
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
                results = new Caller().busTicketSuccess(_userID, encryptedToken,dataToConfirm,Base64.encode((RSA.encrypt(token, orderId))), Base64.encode((RSA.encrypt(token,String.valueOf( amount)))));
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
    }*/
    private class BookAirplaneTicketFromWallet extends AsyncTask<String, Void, String> {

        String results = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            return new Caller().airplainTicketRequestFromWallet(_userID, encryptedToken, params[0], Long.parseLong(amount.replace(".0", "")));
           /* try {
            // return AESEncyption.decryptMsg(res, _aesKey);
               // Gson g = new Gson();
              //  results = g.fromJson(dec, BookTicket.class);
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
            }*/

            //return results;
        }

        @Override
        protected void onPostExecute(String bookTicket) {
            super.onPostExecute(bookTicket);
            //TODO we should add other items here too

            Toast toast = Toast.makeText(getActivity(), bookTicket, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toastText = toast.getView().findViewById(android.R.id.message);
            toast.getView().setBackgroundResource(R.drawable.toast_background);

            if (toastText != null) {
                toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                toastText.setGravity(Gravity.CENTER);
                toastText.setTextSize(14);
            }
            toast.show();


        }
    }

    private class AirplaneTicketRequestForApp extends AsyncTask<String, Void, AirReservation> {

        AirReservation results = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getActivity(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }

        @Override
        protected AirReservation doInBackground(String... params) {
            String res = new Caller().airplaneTicketRequestForApp(_userID, encryptedToken, params[0], Long.parseLong(amount.replace(".0", "")));
            try {
                String dec = AESEncyption.decryptMsg(res, _aesKey);
                Gson g = new Gson();
                results = g.fromJson(dec, AirReservation.class);
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidParameterSpecException |
                    InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException |
                    IllegalBlockSizeException | UnsupportedEncodingException e) {

                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(AirReservation bookTicket) {
            super.onPostExecute(bookTicket);
            //TODO we should add other items here too


            if (bookTicket != null && bookTicket.token != null) {

                _orderID = String.valueOf(bookTicket.orderID);
                Intent intent = new Intent(getActivity(), PaymentInitiator.class);
                intent.putExtra("Type", "1");
                intent.putExtra("Token", bookTicket.token);
                intent.putExtra("OrderID", _orderID);


                startActivityForResult(intent, 10);


            } else {
                Toast toast = Toast.makeText(getActivity(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }
    }

    private class BookAirplaneTicketApprove extends AsyncTask<String, Void, WalletCredit> {

        WalletCredit results = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (!isNetworkAvailable()) {
                Toast toast = Toast.makeText(getContext(), R.string.error_net, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);
                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
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
                results = new Caller().airplaneTicketSuccess(_userID, encryptedToken, dataToConfirm, Base64.encode((RSA.encrypt(_token, _orderID))),
                        Base64.encode((RSA.encrypt(_token, String.valueOf(amount)))));

            } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException |
                    NoSuchPaddingException | NoSuchAlgorithmException e) {

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
                Toast toast = Toast.makeText(getContext(), R.string.try_again, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {

            case 1:
                // for successful response in payment
//                Toast.makeText(getActivity() , "In Merchant Side " +  data.getStringExtra("enData") + "\n\n" +  data.getStringExtra("message") + "\n" +  String.valueOf(data.getIntExtra("status" , 0)), Toast.LENGTH_LONG  ).show();
                _dataToConfirm = data.getStringExtra("enData");
                if (_dataToConfirm != null)
                    _dataToConfirm = _dataToConfirm.replace("\\", "");
                String one = data.getStringExtra("message");
                String two = String.valueOf(data.getIntExtra("status", 0));
                if (approveType == 0)
                    new approveTopUpRequest().execute();
                else
                    new approveInternetTopUpRequest().execute();

                break;
            case 2:
            case 5:
                // for Library Internal Error in Payment
                // for Error response in payment
//                Toast.makeText(getActivity(), "In Merchant Side " + String.valueOf(data.getIntExtra("errorType", 0)) + "\n\n" + String.valueOf(data.getIntExtra("OrderID", 0)), Toast.LENGTH_LONG).show();
                break;
            case 3:
                // for successful response in Bill Payment
//                Toast.makeText(getActivity() , "In Merchant Side " +  data.getStringExtra("enData") + "\n\n" +  data.getStringExtra("message") + "\n" +   String.valueOf(data.getIntExtra("status" , 0)), Toast.LENGTH_LONG  ).show();
                _dataToConfirm = data.getStringExtra("enData");
                if (_dataToConfirm != null)
                    _dataToConfirm = _dataToConfirm.replace("\\", "");
                String msg = data.getStringExtra("message");
                String sts = String.valueOf(data.getIntExtra("status", 0));
                if (sts.equals("0")) {
                    new BillSuccess().execute();
                }
                break;
            case 4:
                // for Error response in Bill Payment
//                Toast.makeText(getActivity() , "In Merchant Side " +  String.valueOf( data.getIntExtra("errorType" , 0) ) + "\n\n" , Toast.LENGTH_LONG  ).show();
                Toast toast = Toast.makeText(getActivity(), "خطا" +
                        " " +
                        "در" +
                        " " +
                        "ثبت" +
                        " " +
                        "درخواست" +
                        " " +
                        "پرداخت" +
                        " " +
                        "قبض", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast.getView().findViewById(android.R.id.message);
                toast.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast.show();
                break;
            case 6:
                // for Library Internal Error in Bill Payment
//                Toast.makeText(getActivity() , "In Merchant Side " +  String.valueOf( data.getIntExtra("errorType" , 0) ) + "\n\n" , Toast.LENGTH_LONG  ).show();
                Toast toast6 = Toast.makeText(getActivity(), "خطای" +
                        " " +
                        "داخلی" +
                        " " +
                        "کتابخانه" +
                        " " +
                        "در" +
                        " " +
                        "ثبت" +
                        " " +
                        "پرداخت" +
                        " " +
                        "قبض", Toast.LENGTH_SHORT);
                toast6.setGravity(Gravity.CENTER, 0, 0);
                toastText = toast6.getView().findViewById(android.R.id.message);
                toast6.getView().setBackgroundResource(R.drawable.toast_background);

                if (toastText != null) {
                    toastText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSansMobile.ttf"));
                    toastText.setTextColor(getResources().getColor(R.color.colorBlack));
                    toastText.setGravity(Gravity.CENTER);
                    toastText.setTextSize(14);
                }
                toast6.show();

                break;
            case 7:
                // for Library Successfull topup
//                Toast.makeText(getActivity(), "In Merchant Side " + data.getStringExtra("enData") + "\n\n" + data.getStringExtra("message") + "\n" + data.getIntExtra("status", 0), Toast.LENGTH_LONG).show();
                break;
            case 8:
            case 9:
                // for Library Internal Error in Topup Payment
                // for  Error in Topup Payment
//                Toast.makeText(getActivity(), "In Merchant Side " + data.getIntExtra("errorType", 0) + "\n\n", Toast.LENGTH_LONG).show();
                break;
            case 10:
                // for airplane success payment
                _dataToConfirm = data.getStringExtra("enData");
                if (_dataToConfirm != null)
                    _dataToConfirm = _dataToConfirm.replace("\\", "");

                new BookAirplaneTicketApprove().execute();

                break;
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
        }
    }

}