package com.fara.inkamapp.Fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Activities.AirplaneTickets;
import com.fara.inkamapp.Activities.BusTickets;
import com.fara.inkamapp.Activities.BuyCharge;
import com.fara.inkamapp.Activities.CardToCardTransfer;
import com.fara.inkamapp.Activities.CrispWebView;
import com.fara.inkamapp.Activities.MainActivity;
import com.fara.inkamapp.Activities.Notifications;
import com.fara.inkamapp.Activities.PhoneDebt;
import com.fara.inkamapp.Activities.ServiceBillsAndCarFines;
import com.fara.inkamapp.Activities.TrainTickets;
import com.fara.inkamapp.Adapters.DashboardServiceAdapter;
import com.fara.inkamapp.BottomSheetFragments.InternetPackageBottomSheet;
import com.fara.inkamapp.BottomSheetFragments.RepeatTransaction;
import com.fara.inkamapp.BottomSheetFragments.UserProfile;
import com.fara.inkamapp.Helpers.Base64;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Helpers.RSA;
import com.fara.inkamapp.Models.ProductAndService;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.fara.inkamapp.Activities.LoginInkam.publicKey;


/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    private ImageButton notification, repeatTransaction;
    private CircleImageView profile;
    private BottomSheetDialogFragment bottomSheetDialogFragment;
    //    private RelativeLayout charge, cardToCard, netPackage, trainTicket, planeTicket, busTicket, phone, car, service;
    private RelativeLayout netPackage;
    private TextView toastText, tvIncom, tvUsers;
    private RecyclerView mRecyclerView;
    private DashboardServiceAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView walletBalance;
    private String encrytedToken;


    public Dashboard() {
        // Required empty public constructor
    }

    public void refreshWallet() {
        new getUserWallet().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        notification = view.findViewById(R.id.ib_notification);
        profile = view.findViewById(R.id.profile_image);
        repeatTransaction = view.findViewById(R.id.ib_history);
        walletBalance = view.findViewById(R.id.tv_wallet_balance);
        tvIncom = view.findViewById(R.id.tv_income);
        tvUsers = view.findViewById(R.id.tv_users);

        mRecyclerView = view.findViewById(R.id.rv_service_and_product);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogFragment = UserProfile.newInstance("Bottom Sheet Get Money Dialog");
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Notifications.class);
                startActivity(intent);
            }
        });

        repeatTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogFragment = RepeatTransaction.newInstance("Bottom Sheet Get Money Dialog");
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        try {
            encrytedToken = Base64.encode((RSA.encrypt(MainActivity._token, publicKey)));
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

        new loginVerification().execute();
        new getUserWallet().execute();


        return view;
    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getActivity());
    }

    private class loginVerification extends AsyncTask<Void, Void, ArrayList<ProductAndService>> {

        ArrayList<ProductAndService> results = null;

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
        protected ArrayList<ProductAndService> doInBackground(Void... params) {

            results = new Caller().getProductAndService(MainActivity._userId, MainActivity._token);

            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<ProductAndService> productAndServices) {
            super.onPostExecute(productAndServices);
            //TODO we should add other items here too


            if (productAndServices != null) {

                mAdapter = new DashboardServiceAdapter(getActivity(), productAndServices);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setClickListener(new DashboardServiceAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        switch (position) {
                            case 0:
                                Intent carBillsIntent = new Intent(getActivity(), ServiceBillsAndCarFines.class);
                                carBillsIntent.putExtra("BillCode", 0);
                                startActivity(carBillsIntent);
                                break;

                            case 1:
                                Intent phoneDebtIntent = new Intent(getActivity(), PhoneDebt.class);
                                startActivity(phoneDebtIntent);
                                break;

                            case 2:
                                Intent airplaneIntent = new Intent(getActivity(), AirplaneTickets.class);
                                startActivity(airplaneIntent);
                                break;

                            case 3:

                                Intent billIntent = new Intent(getActivity(), ServiceBillsAndCarFines.class);
                                billIntent.putExtra("BillCode", 1);
                                startActivity(billIntent);
                                break;

                            case 4:
                                Intent cardToCard = new Intent(getActivity(), CardToCardTransfer.class);
                                startActivity(cardToCard);
                                break;

                            case 5:

                                Intent chargeIntent = new Intent(getActivity(), BuyCharge.class);
                                startActivity(chargeIntent);

                                break;

                            case 6:

                                Intent ticketIntent = new Intent(getActivity(), BusTickets.class);
                                startActivity(ticketIntent);

                                break;

                            case 7:

                                Intent trainTicket = new Intent(getActivity(), TrainTickets.class);
                                startActivity(trainTicket);
                                break;

                            case 8:
                                bottomSheetDialogFragment = InternetPackageBottomSheet.newInstance("Bottom Sheet Get Money Dialog");
                                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                                break;

                            default:
                                break;
                        }

                    }
                });


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

    private class getUserWallet extends AsyncTask<Void, Void, User> {

        User results = null;

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
        protected User doInBackground(Void... params) {
            results = new Caller().getUserById(MainActivity._userId, MainActivity._token);

            return results;
        }

        @Override
        protected void onPostExecute(User userWallet) {
            super.onPostExecute(userWallet);
            //TODO we should add other items here too

            if (userWallet != null) {
                NumberFormat formatter = new DecimalFormat("#,###");
                String formattedNumber = formatter.format(Double.valueOf(userWallet.getCashOfWallet()));
                walletBalance.setText(Numbers.ToPersianNumbers(String.valueOf(formattedNumber)));
                tvUsers.setText(Numbers.ToPersianNumbers(String.valueOf(formatter.format(userWallet.getUserCount()))));
                tvIncom.setText(Numbers.ToPersianNumbers(String.valueOf(formatter.format(userWallet.getIncome()))));
                if (!userWallet.getProfilePicURL().equals("anyType{}") && userWallet.getProfilePicURL() != null) {


                    Picasso.with(getContext())
                            .load("http://" + userWallet.getProfilePicURL()).resize(25, 25).centerCrop().into(profile, new Callback() {
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

    private class GetProfileImageByUserName extends AsyncTask<Void, Void, String> {

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
            results = new Caller().GetProfileImageByUserName(MainActivity._userName);

            return results;
        }

        @Override
        protected void onPostExecute(String pic) {
            super.onPostExecute(pic);
            //TODO we should add other items here too

            if (pic != null && !pic.equals("")) {

                Picasso.with(getContext())
                        .load("http://" + pic).resize(44, 44).centerCrop().into(profile, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.i("Sohrab P", "Success");

                    }

                    @Override
                    public void onError() {

                    }

                });
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


}
