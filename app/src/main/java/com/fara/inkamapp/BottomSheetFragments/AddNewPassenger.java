package com.fara.inkamapp.BottomSheetFragments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.DialogFragment;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewPassenger extends BottomSheetDialogFragment {

    String string;
    private RadioButton btn_male, btn_female;

    public static AddNewPassenger newInstance(String string) {
        AddNewPassenger addNewPassenger = new AddNewPassenger();
        Bundle args = new Bundle();
        args.putString("string", string);
        addNewPassenger.setArguments(args);
        return addNewPassenger;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        string = getArguments().getString("string");
        // bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
    }


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