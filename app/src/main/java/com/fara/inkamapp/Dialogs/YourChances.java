package com.fara.inkamapp.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Activities.MainActivity;
import com.fara.inkamapp.Adapters.ChancesAdapter;
import com.fara.inkamapp.Adapters.PresentorUserDetail;
import com.fara.inkamapp.Adapters.UserChancesAdapter;
import com.fara.inkamapp.BottomSheetFragments.AddExtraCredit;
import com.fara.inkamapp.Models.Chances;
import com.fara.inkamapp.Models.JModel;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class YourChances extends Dialog {

    private Activity activity;
    private RecyclerView rvChances;
    private ImageButton cancel;
    private TextView toastText;
    private UserChancesAdapter userChancesAdapter;

    public YourChances(Activity _activity) {
        super(_activity);
        // TODO Auto-generated constructor stub
        this.activity = _activity;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_your_chances);

        rvChances = findViewById(R.id.rv_your_chances);
        cancel = findViewById(R.id.ib_cancel);

        new GetUserChances().execute();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    private class GetUserChances extends AsyncTask<Void, Void, ArrayList<Chances>> {

        ArrayList<Chances> results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<Chances> doInBackground(Void... params) {
            results = new Caller().getUserChances(MainActivity._userId, MainActivity._token);

            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<Chances> chances) {
            super.onPostExecute(chances);
            //TODO we should add other items here too

            try {

                if (chances != null) {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvChances.setLayoutManager(layoutManager);
                    userChancesAdapter = new UserChancesAdapter(getContext(), chances);
                    rvChances.setAdapter(userChancesAdapter);

                } else {
                    Toast toast = Toast.makeText(getContext(), R.string.try_again, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toastText = toast.getView().findViewById(android.R.id.message);
                    toast.getView().setBackgroundResource(R.drawable.toast_background);

                    if (toastText != null) {
                        toastText.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/IRANSansMobile.ttf"));
                        toastText.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
                        toastText.setGravity(Gravity.CENTER);
                        toastText.setTextSize(14);
                    }
                    toast.show();
                }
            } catch (Exception e) {
                String s = e.toString();
            }
        }
    }
}
