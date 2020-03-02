package com.fara.inkamapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fara.inkamapp.Adapters.NetPackages;
import com.fara.inkamapp.R;

import java.util.ArrayList;

public class InternetPackageActivity extends AppCompatActivity implements NetPackages.ItemClickListener {

    private NetPackages netPackagesAdapter;
    private RelativeLayout packageInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_package);

        packageInfo = findViewById(R.id.rl_package_info);

        packageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BuyPackages.class);
                startActivity(intent);
            }
        });

        // data to populate the RecyclerView with

        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("ساعتی");
        animalNames.add("روزانه");
        animalNames.add("هفتگی");
        animalNames.add("ماهانه");
        animalNames.add("سالانه");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rv_packages);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        netPackagesAdapter = new NetPackages(getApplicationContext(), animalNames);
        netPackagesAdapter.setClickListener(this);
        recyclerView.setAdapter(netPackagesAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + netPackagesAdapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
    }

}
