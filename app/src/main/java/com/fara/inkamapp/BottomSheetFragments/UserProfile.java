package com.fara.inkamapp.BottomSheetFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fara.inkamapp.Activities.AboutInkam;
import com.fara.inkamapp.Activities.EditUserProfile;
import com.fara.inkamapp.Activities.LoginInkam;
import com.fara.inkamapp.Activities.MainActivity;
import com.fara.inkamapp.Activities.Setting;
import com.fara.inkamapp.Activities.Support;
import com.fara.inkamapp.Activities.TermsAndConditions;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.fara.inkamapp.Activities.CompleteProfile.MyPREFERENCES;

public class UserProfile extends BottomSheetDialogFragment {

    String string;
    private RelativeLayout editProfile, support, termsConditions, about, settings;
    private TextView logout, toastText, name, phoneNumber;
    private CircleImageView profilePic;


    public static UserProfile newInstance(String string) {
        UserProfile userProfile = new UserProfile();
        Bundle args = new Bundle();
        args.putString("string", string);
        userProfile.setArguments(args);
        return userProfile;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        string = getArguments().getString("string");
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_user_profile, container,
                false);

        editProfile = view.findViewById(R.id.rl_edit_profile);
        support = view.findViewById(R.id.rl_online_support);
        termsConditions = view.findViewById(R.id.rl_terms_conditions);
        about = view.findViewById(R.id.rl_about_inkam);
        settings = view.findViewById(R.id.rl_setting);
        logout = view.findViewById(R.id.tv_log_out);
        profilePic = view.findViewById(R.id.iv_user_profile);
        name = view.findViewById(R.id.tv_user_name);
        phoneNumber = view.findViewById(R.id.tv_user_phone_number);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditUserProfile.class);
                startActivity(intent);
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Support.class);
                startActivity(intent);
            }
        });

        termsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TermsAndConditions.class);
                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutInkam.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Setting.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences settings = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                settings.edit().remove("Token").apply();
                settings.edit().remove("UserID").apply();
                settings.edit().remove("UserName").apply();
                settings.edit().remove("key").apply();
                settings.edit().remove("expDate").apply();


                Intent intent = new Intent(getActivity(), LoginInkam.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        new GetUserById().execute();

        return view;
    }

    private class GetUserById extends AsyncTask<Void, Void, User> {

        User results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected User doInBackground(Void... params) {
            results = new Caller().getUserById(MainActivity._userId, MainActivity._token);

            return results;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            //TODO we should add other items here too

            if (user != null) {
                if (!user.getFirstName().equals("anyType{}") && user.getFirstName() != null)
                    name.setText(user.getFirstName());

                else if (!user.getLastName().equals("anyType{}") && user.getLastName() != null)
                    name.setText(user.getLastName());

                if (!user.getFirstName().equals("anyType{}") && user.getFirstName() != null && !user.getLastName().equals("anyType{}") && user.getLastName() != null) {
                    name.setText(user.getFirstName() + " " + user.getLastName());
                }

                if (!user.getUserName().equals("anyType{}") && user.getUserName() != null)
                    phoneNumber.setText(Numbers.ToPersianNumbers(user.getUserName()));

                if (user.getProfilePicURL() != null && !user.getProfilePicURL().equals("anyType{}")) {
                    Picasso.with(getContext())
                            .load("http://"+user.getProfilePicURL()).resize(100, 100).centerCrop().into(profilePic, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.i("Sohrab P", "Success");

                        }

                        @Override
                        public void onError() {

                        }

                    });
                }


            }
        }
    }
}

