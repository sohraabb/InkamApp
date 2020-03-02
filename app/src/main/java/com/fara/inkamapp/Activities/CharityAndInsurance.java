package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fara.inkamapp.BottomSheetFragments.CharityPriceInfo;
import com.fara.inkamapp.BottomSheetFragments.RecentTransactions;
import com.fara.inkamapp.BottomSheetFragments.UserProfile;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CharityAndInsurance extends AppCompatActivity {

    private RelativeLayout goToCharity;
    private BottomSheetDialogFragment bottomSheetDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_and_insurance);

        goToCharity = findViewById(R.id.rl_charity_info);

        goToCharity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogFragment = RecentTransactions.newInstance("Bottom Sheet Get Money Dialog");
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
    }
}
