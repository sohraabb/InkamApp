package com.fara.inkamapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.fara.inkamapp.Activities.AirplaneTickets;
import com.fara.inkamapp.Activities.BusTickets;
import com.fara.inkamapp.Activities.BuyCharge;
import com.fara.inkamapp.Activities.CardToCardTransfer;
import com.fara.inkamapp.Activities.CardToCardTransfer2;
import com.fara.inkamapp.Activities.Notifications;
import com.fara.inkamapp.Activities.PhoneDebt;
import com.fara.inkamapp.Activities.ServiceBillsAndCarFines;
import com.fara.inkamapp.Activities.TrainTickets;
import com.fara.inkamapp.BottomSheetFragments.GetMoney;
import com.fara.inkamapp.BottomSheetFragments.InternetPackageBottomSheet;
import com.fara.inkamapp.BottomSheetFragments.RepeatTransaction;
import com.fara.inkamapp.BottomSheetFragments.UserProfile;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    private ImageButton notification, repeatTransaction;
    private CircleImageView profile;
    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private RelativeLayout charge, cardToCard, netPackage, trainTicket, planeTicket, busTicket, phone, car, service;


    public Dashboard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        notification = view.findViewById(R.id.ib_notification);
        profile = view.findViewById(R.id.profile_image);
        repeatTransaction = view.findViewById(R.id.ib_history);
        charge = view.findViewById(R.id.rl_charge);
        cardToCard = view.findViewById(R.id.rl_card_to_card);
        netPackage = view.findViewById(R.id.rl_internet_package);
        trainTicket = view.findViewById(R.id.rl_train_ticket);
        planeTicket = view.findViewById(R.id.rl_plane);
        busTicket = view.findViewById(R.id.rl_bus);
        phone = view.findViewById(R.id.rl_phone);
        car = view.findViewById(R.id.rl_car);
        service = view.findViewById(R.id.rl_service);

        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ServiceBillsAndCarFines.class);
                startActivity(intent);
            }
        });

        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ServiceBillsAndCarFines.class);
                startActivity(intent);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PhoneDebt.class);
                startActivity(intent);
            }
        });

        busTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BusTickets.class);
                startActivity(intent);
            }
        });

        planeTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AirplaneTickets.class);
                startActivity(intent);
            }
        });

        trainTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TrainTickets.class);
                startActivity(intent);
            }
        });

        charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BuyCharge.class);
                startActivity(intent);
            }
        });

        cardToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CardToCardTransfer.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogFragment = UserProfile.newInstance("Bottom Sheet Get Money Dialog");
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(),bottomSheetDialogFragment.getTag());
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Notifications.class);
                startActivity(intent);
            }
        });

        repeatTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogFragment = RepeatTransaction.newInstance("Bottom Sheet Get Money Dialog");
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(),bottomSheetDialogFragment.getTag());
            }
        });

        netPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogFragment = InternetPackageBottomSheet.newInstance("Bottom Sheet Get Money Dialog");
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(),bottomSheetDialogFragment.getTag());
            }
        });


        return view;
    }


}