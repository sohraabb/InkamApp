package com.fara.inkamapp.BottomSheetFragments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fara.inkamapp.Fragments.AddPassenger;
import com.fara.inkamapp.Models.Passengers;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewPassenger extends BottomSheetDialogFragment {
    private NewPassengerListener mListener;
    String string;
    private TextView toastText;
    private RadioButton btn_male, btn_female, radio_adult, radio_child, radio_infant;
    private EditText et_first_name_farsi, et_last_name_farsi, et_first_name_english, et_last_name_english, et_phone, et_identity_code;
    private String gender;
    private RadioGroup radioGroup_type;

    public interface NewPassengerListener {
        void newPassenger(Passengers passenger);
    }

    public void setOnNewPassengerListener(NewPassengerListener listener) {
        mListener = listener;
    }

    public static AddNewPassenger newInstance(String string) {
        AddNewPassenger addNewPassenger = new AddNewPassenger();
        Bundle args = new Bundle();
        args.putString("string", string);
        addNewPassenger.setArguments(args);
        return addNewPassenger;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (NewPassengerListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        string = getArguments().getString("string");
        // bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(BottomSheetDialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_add_new_passenger, container,
                false);

        // get the views and attach the listener
        btn_female = view.findViewById(R.id.radio_gender_female);
        btn_male = view.findViewById(R.id.radio_gender_male);
        radioGroup_type = view.findViewById(R.id.radioGroup_type);
        radio_adult = view.findViewById(R.id.radio_adult);
        radio_child = view.findViewById(R.id.radio_child);
        radio_infant = view.findViewById(R.id.radio_infant);


        et_first_name_farsi = view.findViewById(R.id.et_first_name_farsi);
        et_last_name_farsi = view.findViewById(R.id.et_last_name_farsi);
        et_phone = view.findViewById(R.id.et_phone_number);
        et_identity_code = view.findViewById(R.id.et_identity_code);
        Button btnAccept = view.findViewById(R.id.btn_accept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    if (et_first_name_farsi.getText().toString().length() == 0 ||
                            et_last_name_farsi.getText().toString().length() == 0 ||
                            et_identity_code.getText().toString().length() == 0 ||
                            et_phone.getText().toString().length() == 0

                    ) {
                        mListener.newPassenger(null);
                    } else {
                        Passengers pass = new Passengers();
                        pass.setFirstName(et_first_name_farsi.getText().toString());
                        pass.setLastName(et_last_name_farsi.getText().toString());
                        pass.setNationalCode(et_identity_code.getText().toString());
                        pass.setMobile(et_phone.getText().toString());
                        pass.setGender(gender);
                        mListener.newPassenger(pass);
                        dismiss();
                    }
                }
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {

            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{

                            new int[]{-android.R.attr.state_enabled}, //disabled
                            new int[]{android.R.attr.state_enabled} //enabled
                    },
                    new int[]{

                            R.color.colorWhite //disabled
                            , R.color.colorMainGreen //enabled

                    }
            );
            gender = "Male";
            btn_female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        gender = "Female";
                    } else {
                        gender = "Male";
                    }
                }
            });
            btn_male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        gender = "Male";
                    } else {
                        gender = "Female";
                    }
                }
            });
            btn_female.setButtonTintList(colorStateList);//set the color tint list
            btn_male.setButtonTintList(colorStateList);//set the color tint list

            btn_male.invalidate(); //could not be necessary
            btn_female.invalidate(); //could not be necessary

        } else {
            AppCompatRadioButton rb;
            rb = new AppCompatRadioButton(getActivity());

            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{

                            R.color.colorWhite
                            , R.color.colorMainGreen,
                    }
            );
            rb.setSupportButtonTintList(colorStateList);
        }

        return view;

    }
}