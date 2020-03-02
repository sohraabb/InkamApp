package com.fara.inkamapp.BottomSheetFragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fara.inkamapp.Activities.AboutInkam;
import com.fara.inkamapp.Activities.EditUserProfile;
import com.fara.inkamapp.Activities.Setting;
import com.fara.inkamapp.Activities.Support;
import com.fara.inkamapp.Activities.TermsAndConditions;
import com.fara.inkamapp.Dialogs.SuccessTransfer;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class UserProfile extends BottomSheetDialogFragment {

    String string;
    private RelativeLayout editProfile, support, termsConditions, about, settings;

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

        return view;

    }
}

