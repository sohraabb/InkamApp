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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.Activities.MainActivity;
import com.fara.inkamapp.Adapters.ChancesAdapter;
import com.fara.inkamapp.Adapters.CrimeAdapter;
import com.fara.inkamapp.Adapters.PresentorUserDetail;
import com.fara.inkamapp.Models.JModel;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.R;
import com.fara.inkamapp.WebServices.Caller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IncreaseChances extends Dialog {

    private Activity activity;
    private RecyclerView rvChances;
    private ImageButton cancel;
    private TextView toastText;
    private ChancesAdapter chancesAdapter;

    public IncreaseChances(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_increase_chances);
        rvChances = findViewById(R.id.rv_increase_chances);
        cancel = findViewById(R.id.ib_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        new GetChances().execute();

    }

    private class GetChances extends AsyncTask<Void, Void, String> {

        String results = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            results = new Caller().getChance(MainActivity._userId, MainActivity._token);

            return results;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //TODO we should add other items here too

            try {
                ArrayList<JModel> modelArrayList=new ArrayList<>();


                if (result != null) {

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray names = jsonObject.names();


                    for (int i = 0; i < names.length(); i++) {
                        JModel model = new JModel();
                        String temp = names.getString(i);
                        model.name = temp;
                        model.value = jsonObject.optString(temp);
                        modelArrayList.add(model);
                    }


                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvChances.setLayoutManager(layoutManager);
                    chancesAdapter = new ChancesAdapter(getContext(), modelArrayList);
                    rvChances.setAdapter(chancesAdapter);

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

