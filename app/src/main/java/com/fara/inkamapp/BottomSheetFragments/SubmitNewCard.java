package com.fara.inkamapp.BottomSheetFragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fara.inkamapp.Dialogs.SubmitShebaNumber;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SubmitNewCard extends BottomSheetDialogFragment {

    String string;

    private static final String LETTER_SPACING = " ";

    private EditText firstEditText, secondEditText, thirdEditText, fourthEditText;
    private Button submit;
    private String myPreviousText;

    public static SubmitNewCard newInstance(String string) {
        SubmitNewCard submitNewCard = new SubmitNewCard();
        Bundle args = new Bundle();
        args.putString("string", string);
        submitNewCard.setArguments(args);
        return submitNewCard;
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

        View view = inflater.inflate(R.layout.bottom_sheet_submit_card, container,
                false);

        // get the views and attach the listener

        firstEditText = view.findViewById(R.id.et_first_num);
        secondEditText = view.findViewById(R.id.et_second_num);
        thirdEditText = view.findViewById(R.id.et_third_num);
        fourthEditText = view.findViewById(R.id.et_fourth_num);
        submit = view.findViewById(R.id.btn_submit_card);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitShebaNumber submitShebaNumber = new SubmitShebaNumber(getActivity());
                submitShebaNumber.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                submitShebaNumber.show();

                dismiss();
            }
        });


        firstEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();

                // Only update the EditText when the user modify it -> Otherwise it will be triggered when adding spaces
                if (!text.equals(myPreviousText)) {
                    // Remove spaces
                    text = text.replace(" ", "");

                    // Add space between each character
                    StringBuilder newText = new StringBuilder();
                    for (int i = 0; i < text.length(); i++) {
                        if (i == text.length() - 1) {
                            // Do not add a space after the last character -> Allow user to delete last character
                            newText.append(Character.toUpperCase(text.charAt(text.length() - 1)));
                        } else {
                            newText.append(Character.toUpperCase(text.charAt(i)) + LETTER_SPACING);
                        }
                    }

                    myPreviousText = newText.toString();

                    // Update the text with spaces and place the cursor at the end
                    firstEditText.setText(newText);
                    firstEditText.setSelection(newText.length());
                }
            }
        });

        secondEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();

                // Only update the EditText when the user modify it -> Otherwise it will be triggered when adding spaces
                if (!text.equals(myPreviousText)) {
                    // Remove spaces
                    text = text.replace(" ", "");

                    // Add space between each character
                    StringBuilder newText = new StringBuilder();
                    for (int i = 0; i < text.length(); i++) {
                        if (i == text.length() - 1) {
                            // Do not add a space after the last character -> Allow user to delete last character
                            newText.append(Character.toUpperCase(text.charAt(text.length() - 1)));
                        } else {
                            newText.append(Character.toUpperCase(text.charAt(i)) + LETTER_SPACING);
                        }
                    }

                    myPreviousText = newText.toString();

                    // Update the text with spaces and place the cursor at the end
                    secondEditText.setText(newText);
                    secondEditText.setSelection(newText.length());
                }
            }
        });

        thirdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();

                // Only update the EditText when the user modify it -> Otherwise it will be triggered when adding spaces
                if (!text.equals(myPreviousText)) {
                    // Remove spaces
                    text = text.replace(" ", "");

                    // Add space between each character
                    StringBuilder newText = new StringBuilder();
                    for (int i = 0; i < text.length(); i++) {
                        if (i == text.length() - 1) {
                            // Do not add a space after the last character -> Allow user to delete last character
                            newText.append(Character.toUpperCase(text.charAt(text.length() - 1)));
                        } else {
                            newText.append(Character.toUpperCase(text.charAt(i)) + LETTER_SPACING);
                        }
                    }

                    myPreviousText = newText.toString();

                    // Update the text with spaces and place the cursor at the end
                    thirdEditText.setText(newText);
                    thirdEditText.setSelection(newText.length());
                }
            }
        });

        fourthEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();

                // Only update the EditText when the user modify it -> Otherwise it will be triggered when adding spaces
                if (!text.equals(myPreviousText)) {
                    // Remove spaces
                    text = text.replace(" ", "");

                    // Add space between each character
                    StringBuilder newText = new StringBuilder();
                    for (int i = 0; i < text.length(); i++) {
                        if (i == text.length() - 1) {
                            // Do not add a space after the last character -> Allow user to delete last character
                            newText.append(Character.toUpperCase(text.charAt(text.length() - 1)));
                        } else {
                            newText.append(Character.toUpperCase(text.charAt(i)) + LETTER_SPACING);
                        }
                    }

                    myPreviousText = newText.toString();

                    // Update the text with spaces and place the cursor at the end
                    fourthEditText.setText(newText);
                    fourthEditText.setSelection(newText.length());
                }
            }
        });

        return view;

    }

}

