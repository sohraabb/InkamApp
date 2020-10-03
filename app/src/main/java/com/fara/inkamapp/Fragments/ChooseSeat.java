package com.fara.inkamapp.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fara.inkamapp.Activities.BusTicketSelectionProcess;
import com.fara.inkamapp.Activities.SearchTickets;
import com.fara.inkamapp.BottomSheetFragments.AddNewPassenger;
import com.fara.inkamapp.Models.BusSeate;
import com.fara.inkamapp.R;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChooseSeat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseSeat extends Fragment {

    private static List<BusSeate> seate;
    private static List<BusSeate> selectedSeate;
    private AddSeatListener mListener;

    public ChooseSeat() {
        // Required empty public constructor
    }

    public interface AddSeatListener {
        void addSeat(List<BusSeate> seat);
    }

    public void setOnAddSeatListener(AddSeatListener listener) {
        this.mListener = listener;
    }

    // TODO: Rename and change types and number of parameters
    public ChooseSeat newInstance(Context context, List<BusSeate> seate) {
        ChooseSeat repeatTransaction = new ChooseSeat();
        Bundle args = new Bundle();
        args.putString("string", "");
        selectedSeate = new ArrayList<>();
        this.seate = seate;

        repeatTransaction.setArguments(args);
        return repeatTransaction;
        // return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (AddSeatListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_choose_seat, container, false);
        v.setRotationY(180);
        LinearLayout mainLayout = (LinearLayout) v.findViewById(R.id.ll_seats);
        int count = seate.size();
        View view;
        List<Button> buttons = new ArrayList<>();
        LayoutInflater layoutInflater = getLayoutInflater();
        Dictionary<String, List<BusSeate>> dic = new Hashtable<>();
        for (int i = 1; i <= count; i++) {
            List<BusSeate> newList = new ArrayList<>();
            for (int j = 0; j < count; j++) {
                if (seate.get(j).getRow().equals(String.valueOf(i))) {
                    newList.add(seate.get(j));
                }

            }
            if (newList.size() > 0) {
                dic.put(String.valueOf(i), newList);
            }
        }

        for (int i = 1; i <= dic.size(); i++) {
            view = layoutInflater.inflate(R.layout.seat_button_layout, mainLayout, false);

            Button btn1 = (Button) view.findViewById(R.id.btn_seat1);
            Button btn2 = (Button) view.findViewById(R.id.btn_seat2);
            Button btn3 = (Button) view.findViewById(R.id.btn_seat3);
            Button btn4 = (Button) view.findViewById(R.id.btn_seat4);

            // btn1.setOnClickListener(onClickListener);
            //  btn2.setOnClickListener(onClickListener);
            //  btn3.setOnClickListener(onClickListener);
            //  btn4.setOnClickListener(onClickListener);
            btn1.setVisibility(View.INVISIBLE);
            btn2.setVisibility(View.INVISIBLE);
            btn3.setVisibility(View.INVISIBLE);
            btn4.setVisibility(View.INVISIBLE);
            for (int j = 0; j < dic.get(String.valueOf(i)).size(); j++) {
                BusSeate s = dic.get(String.valueOf(i)).get(j);
                Button currentBtn;
                switch (s.getColumn()) {
                    case "5":
                        btn4.setText(s.getNumber());
                        currentBtn = btn4;
                        break;
                    case "4":
                        btn3.setText(s.getNumber());
                        currentBtn = btn3;
                        break;
                    case "2":
                        btn2.setText(s.getNumber());

                        currentBtn = btn2;
                        break;
                    case "1":
                        btn1.setText(s.getNumber());

                        currentBtn = btn1;
                        break;
                    default:
                        currentBtn = new Button(getContext());
                }
                currentBtn.setVisibility(View.VISIBLE);
                currentBtn.setText(s.getNumber());
                currentBtn.setTag(s);
                if (s.getStatus().equals("BookedForMale")) {
                    currentBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_male_selected));

                } else if (s.getStatus().equals("BookedForFemale")) {
                    currentBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_female_selected));
                } else if (s.getStatus().equals("Available")) {
                    currentBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_corner));
                    currentBtn.setOnClickListener(onClickListener);
                } else if (s.getStatus().equals("1")) {
                    currentBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_male_select));
                    currentBtn.setOnClickListener(onClickListener);
                } else if (s.getStatus().equals("0")) {
                    currentBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_female_select));
                    currentBtn.setOnClickListener(onClickListener);
                } else {
                    currentBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_selected_seat));
                    currentBtn.setEnabled(false);
                }
            }

            mainLayout.addView(view);
        }
        return v;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Button btn = (Button) v;
            BusSeate seat = (BusSeate) btn.getTag();

            Drawable.ConstantState button_male_selected = ContextCompat.getDrawable(getContext(), R.drawable.button_male_select).getConstantState();
            Drawable.ConstantState button_female_selected = ContextCompat.getDrawable(getContext(), R.drawable.button_female_select).getConstantState();
            Drawable.ConstantState rounded_corner = ContextCompat.getDrawable(getContext(), R.drawable.rounded_corner).getConstantState();

            //0=woman,1=man,2=unselected
            if (seat.getStatus().equals("0")) {//if selected as woman change to unselect
                selectedSeate.remove(seat);
                seat.setStatus("2");
                btn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.rounded_corner));

            } else if (seat.getStatus().equals("1"))//if selected as man change to woman
            {

                selectedSeate.remove(seat);

                btn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_female_select));
                seat.setStatus("0");
                selectedSeate.add(seat);

            } else {//if unselected change to man
                seat.setStatus("1");
                btn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_male_select));
                if (!selectedSeate.contains(seat)) {
                    selectedSeate.add(seat);
                }
            }
            btn.setTag(seat);
            mListener.addSeat(selectedSeate);

        }
    };
}
