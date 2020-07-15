package com.fara.inkamapp.Fragments;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;

import com.fara.inkamapp.Activities.BusTicketSelectionProcess;
import com.fara.inkamapp.Adapters.PassengersAdapter;
import com.fara.inkamapp.BottomSheetFragments.AddNewPassenger;
import com.fara.inkamapp.Models.Passengers;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPassenger#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPassenger extends Fragment   {
    List<Passengers> passengersList;
    private BottomSheetDialogFragment bottomSheetDialogFragment;

    public AddPassenger() {
        // Required empty public constructor
    }


    public AddPassenger newInstance(List<Passengers> passengers) {
        this.passengersList = passengers;
        AddPassenger fragment = new AddPassenger();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_passenger, container, false);
        v.setRotationY(180);
        Button btnAddPassenger = (Button) v.findViewById(R.id.btnAddPassenger);
        btnAddPassenger.setOnClickListener(onClickListener);
        ImageButton imgBtnAddPassenger = (ImageButton) v.findViewById(R.id.imgBtnAddPassenger);
        imgBtnAddPassenger.setOnClickListener(onClickListener);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rc_passenger);
        passengersList= new ArrayList<>();

        final PassengersAdapter adapter = new PassengersAdapter(getContext(), passengersList);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        adapter.setClickListener(new PassengersAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        ( (BusTicketSelectionProcess) getActivity()).setOnPassengerListNotifyListener(new BusTicketSelectionProcess.PassengerListNotifyListener() {
            @Override
            public void addToList(Passengers passenger) {
                if (passengersList == null){
                    passengersList=new ArrayList<>();
                }
                passengersList.add(passenger);
                try {
                   // adapter.setValue(passenger);

                   adapter.notifyItemInserted(passengersList.size()-1);
                }catch (Exception ex){
                    String s= ex.toString();
                }


            }
        });
        return v;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            bottomSheetDialogFragment = AddNewPassenger.newInstance("Bottom Sheet Get Money Dialog");
            bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());


        }
    };



}
