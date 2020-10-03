
package com.fara.inkamapp.BottomSheetFragments;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
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
import com.fara.inkamapp.Helpers.DateConverter;
import com.fara.inkamapp.Models.AirplainModels.AirTravelers;
import com.fara.inkamapp.Models.AirplainModels.Document;
import com.fara.inkamapp.Models.AirplainModels.PersonName;
import com.fara.inkamapp.Models.AirplainModels.Telephone;
import com.fara.inkamapp.Models.AirplainModels.TravelerInfo;
import com.fara.inkamapp.Models.Passengers;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.ArrayList;

public class AddNewAirplanePassenger extends BottomSheetDialogFragment implements
        com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    private NewPassengerListener mListener;
    String string;
    private TextView toastText;
    private RadioButton btn_male, btn_female, radio_adult, radio_child, radio_infant;
    private EditText et_first_name_farsi, et_last_name_farsi, et_first_name_english, et_last_name_english, et_phone, et_identity_code;
    private String gender, passengerType;
    private RadioGroup radioGroup_type;
    private EditText selected;

    public interface NewPassengerListener {
        void newPassenger(TravelerInfo passenger);
    }

    public void setOnNewPassengerListener(NewPassengerListener listener) {
        mListener = listener;
    }

    public static AddNewAirplanePassenger newInstance(String string) {
        AddNewAirplanePassenger addNewPassenger = new AddNewAirplanePassenger();
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
        radioGroup_type.setVisibility(View.VISIBLE);
        TextView tv_type = view.findViewById(R.id.tv_type);
        tv_type.setVisibility(View.VISIBLE);
        et_first_name_farsi = view.findViewById(R.id.et_first_name_farsi);
        et_last_name_farsi = view.findViewById(R.id.et_last_name_farsi);
        et_first_name_english = view.findViewById(R.id.et_first_name_english);
        et_last_name_english = view.findViewById(R.id.et_last_name_english);

        et_phone = view.findViewById(R.id.et_phone_number);
        final EditText et_email = view.findViewById(R.id.et_email);
        et_email.setVisibility(View.VISIBLE);
        TextView et_birthdate = view.findViewById(R.id.et_birthdate);
        et_birthdate.setVisibility(View.VISIBLE);
        et_birthdate.setInputType(InputType.TYPE_NULL);


        et_birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selected = (EditText) v;
                showCalendar();

            }
        });

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
                        TravelerInfo info = new TravelerInfo();
                        AirTravelers pass = new AirTravelers();
                        info.Telephone = new Telephone();
                        pass.PersonName = new PersonName();
                        pass.PersianPersonName = new PersonName();
                        pass.Document = new Document();
                        pass.PassengerType = passengerType;
                        pass.Document.DocId = et_identity_code.getText().toString();
                        pass.Document.BirthCountry = "IR";
                        pass.Document.DocIssuedCountry = "IR";
                        pass.Gender = gender;
                        pass.PersianPersonName.GivenName = et_first_name_farsi.getText().toString();
                        pass.PersianPersonName.Surname = et_last_name_farsi.getText().toString();
                        pass.PersonName.GivenName = et_first_name_english.getText().toString();
                        pass.PersonName.Surname = et_last_name_english.getText().toString();
                        pass.BirthDate = DateConverter.Convert_Shamsi_To_Miladi_Date(selected.getText().toString(), 0, 0);
                        info.Email = et_email.getText().toString();
                        info.Telephone.PhoneNumber = et_phone.getText().toString();
                        info.AirTravelers = new ArrayList<>();
                        info.AirTravelers.add(pass);
                        mListener.newPassenger(info);
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

            radio_adult.setButtonTintList(colorStateList);//set the color tint list
            radio_child.setButtonTintList(colorStateList);//set the color tint list
            radio_infant.setButtonTintList(colorStateList);//set the color tint list
            radio_child.invalidate(); //could not be necessary
            radio_infant.invalidate(); //could not be necessary
            radio_adult.invalidate();
            radio_adult.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        passengerType = "1";
                    }
                }
            });
            radio_child.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        passengerType = "2";
                    }
                }
            });
            radio_infant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        passengerType = "3";
                    }
                }
            });
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

    public void showCalendar() {
        //  Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf");

        PersianCalendar persianCalendar = new PersianCalendar();


        //  persianCalendar.setPersianDate(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));


        com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog datePickerDialog = com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog.newInstance(
                this,
                persianCalendar.getPersianYear(),
                persianCalendar.getPersianMonth(),
                persianCalendar.getPersianDay()
        );
        datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");

    }

    @Override
    public void onDateSet(com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;

        selected.setText(date);
    }

}