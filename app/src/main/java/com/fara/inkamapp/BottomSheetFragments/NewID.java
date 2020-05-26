package com.fara.inkamapp.BottomSheetFragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Activities.AirplaneTickets;
import com.fara.inkamapp.Activities.BusTickets;
import com.fara.inkamapp.Activities.BuyCharge;
import com.fara.inkamapp.Activities.CardToCardTransfer;
import com.fara.inkamapp.Activities.PhoneDebt;
import com.fara.inkamapp.Activities.ServiceBillsAndCarFines;
import com.fara.inkamapp.Activities.TrainTickets;
import com.fara.inkamapp.Activities.UserDetails;
import com.fara.inkamapp.Adapters.DashboardServiceAdapter;
import com.fara.inkamapp.Adapters.UserDetailsAdapter;
import com.fara.inkamapp.Helpers.FaraNetwork;
import com.fara.inkamapp.Models.PercentageCode;
import com.fara.inkamapp.Models.ProductAndService;
import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class NewID extends BottomSheetDialogFragment {

    String string;
    private TextView toastText;
    private EditText name, code;
    private Button submit;
    private String idName, idCode;


    public static NewID newInstance(String string) {
        NewID newIdBottomSheet = new NewID();
        Bundle args = new Bundle();
        args.putString("string", string);
        newIdBottomSheet.setArguments(args);
        return newIdBottomSheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        string = getArguments().getString("string");
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(DialogFragment.STYLE_NO_FRAME,0);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_new_id, container,
                false);

        name = view.findViewById(R.id.et_name_id);
        code = view.findViewById(R.id.et_code_id);
        submit = view.findViewById(R.id.btn_accept);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idName = name.getText().toString();
                idCode = code.getText().toString();
                new InsertPresentageCode().execute();
            }
        });


        // get the views and attach the listener

        return view;

    }

    private boolean isNetworkAvailable() {
        return FaraNetwork.isNetworkAvailable(getActivity());
    }


    private class InsertPresentageCode extends AsyncTask<Void, Void, ResponseStatus> {

        ResponseStatus results = null;

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
        protected ResponseStatus doInBackground(Void... params) {

            results = new Caller().insertPresentageCode("2A78AB62-53C9-48B3-9D20-D7EE33337E86", "9368FD3E-7650-4C43-8245-EF33F4743A00", idCode, idName, Boolean.parseBoolean("true"));

            return results;
        }

        @Override
        protected void onPostExecute(ResponseStatus response) {
            super.onPostExecute(response);
            //TODO we should add other items here too

            if (response != null) {

                Toast toast = Toast.makeText(getActivity(), response.get_message(), Toast.LENGTH_SHORT);
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
