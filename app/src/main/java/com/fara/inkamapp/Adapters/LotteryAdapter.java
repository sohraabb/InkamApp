package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.BottomSheetFragments.LotteryWinners;
import com.fara.inkamapp.Helpers.DateConverter;
import com.fara.inkamapp.Helpers.Numbers;
import com.fara.inkamapp.Models.Fund;
import com.fara.inkamapp.Models.ProductAndService;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class LotteryAdapter extends RecyclerView.Adapter<LotteryAdapter.ViewHolder> {
    private ArrayList<Fund> fundList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private BottomSheetDialogFragment bottomSheetDialogFragment;

    public LotteryAdapter(Context _context, ArrayList<Fund> _fund) {
        this.mInflater = LayoutInflater.from(_context);
        this.fundList = _fund;
        this.context = _context;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            final Fund fundObjects = fundList.get(position);

            if (fundObjects.get_persianDate() != null)
                holder.date.setText(fundObjects.get_persianDate());

            holder.amount.setText(Numbers.ToPersianNumbers(String.valueOf(fundObjects.get_balance())));
            holder.yourChances.setText(Numbers.ToPersianNumbers(String.valueOf(fundObjects.get_userChanceCount())));
            holder.chances.setText(Numbers.ToPersianNumbers(String.valueOf(fundObjects.get_totalChanceCount())));

            holder.winners.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("dateTime", DateConverter.Convert_Shamsi_To_Miladi_Date(fundObjects.get_persianDate(),0,0));

                    bottomSheetDialogFragment = LotteryWinners.newInstance("Bottom Sheet Get Money Dialog");
                    bottomSheetDialogFragment.setArguments(bundle);
                    bottomSheetDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                }
            });


        } catch (Exception e) {
            e.toString();
        }
    }


    @Override
    public int getItemCount() {
        return fundList.size();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_lottery, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView chances, yourChances, winners, amount, date, chancesTxt, yourChancesTxt, rial;


        ViewHolder(View itemView) {
            super(itemView);
            chances = itemView.findViewById(R.id.tv_chances_value);
            yourChances = itemView.findViewById(R.id.tv_your_chances_value);
            winners = itemView.findViewById(R.id.tv_winners);
            amount = itemView.findViewById(R.id.tv_lottery_amount);
            date = itemView.findViewById(R.id.tv_lottery_date);
            chancesTxt = itemView.findViewById(R.id.tv_chances);
            yourChancesTxt = itemView.findViewById(R.id.tv_your_chances);
            rial = itemView.findViewById(R.id.tv_rial);

            Typeface iranSansBold = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMobile_Bold.ttf");
            chances.setTypeface(iranSansBold);
            yourChances.setTypeface(iranSansBold);
            winners.setTypeface(iranSansBold);
            amount.setTypeface(iranSansBold);
            Typeface iranSansMedium = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMobile_Medium.ttf");
            date.setTypeface(iranSansMedium);
            Typeface iranSansReg = Typeface.createFromAsset(context.getAssets(), "fonts/IRANSansMobile.ttf");
            chancesTxt.setTypeface(iranSansReg);
            yourChancesTxt.setTypeface(iranSansReg);
            rial.setTypeface(iranSansReg);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

}