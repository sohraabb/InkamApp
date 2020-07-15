package com.fara.inkamapp.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.fara.inkamapp.BottomSheetFragments.SubmitNewCard;
import com.fara.inkamapp.Helpers.AESEncyption;
import com.fara.inkamapp.Models.UserCard;
import com.fara.inkamapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static com.fara.inkamapp.Activities.MainActivity.MyPREFERENCES;

public class AllUserCardsAdapter extends RecyclerView.Adapter<AllUserCardsAdapter.ViewHolder> {

    private ArrayList<UserCard> userCards;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private SharedPreferences sharedPreferences;
    private String cardNo, expDate;
    private BottomSheetDialogFragment bottomSheetDialogFragment;


    // data is passed into the constructor
    public AllUserCardsAdapter(Context _context, ArrayList<UserCard> _userCards) {
        this.mInflater = LayoutInflater.from(_context);
        this.userCards = _userCards;
        this.context = _context;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == R.layout.adapter_all_user_cards) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_all_user_cards, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_add_cards, parent, false);
        }
        context = parent.getContext();

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == userCards.size()) {
            holder.add_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialogFragment = SubmitNewCard.newInstance("Bottom Sheet Dialog");
                    bottomSheetDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager()
                            , bottomSheetDialogFragment.getTag());
                }
            });
        } else {

            final UserCard _cards = userCards.get(position);
            sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            try {
                if (_cards.get_cardNumber() != null && !_cards.get_cardNumber().equals("anyType{}")) {
                    cardNo = AESEncyption.decryptMsg(_cards.get_cardNumber(), sharedPreferences.getString("key", null));
                    StringBuilder s;
                    s = new StringBuilder(cardNo);

                    for (int i = 4; i < s.length(); i += 5) {
                        s.insert(i, " ");
                    }
                    holder.cardNum.setText(s.toString());
                }
                if (_cards.get_expDate() != null && !_cards.get_expDate().equals("anyType{}")) {
                    expDate = AESEncyption.decryptMsg(_cards.get_expDate(), sharedPreferences.getString("key", null));
                    String year = expDate.substring(0, expDate.length() / 2);
                    String month = expDate.substring(expDate.length() / 2);
                    holder.expirationDate.setText(year + "/" + month);
                }

            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidParameterSpecException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (_cards.is_isDefault())
                holder.defaultStar.setVisibility(View.VISIBLE);
            else
                holder.defaultStar.setVisibility(View.INVISIBLE);


            if (_cards.get_bankName() != null) {
                switch (_cards.get_bankName()) {
                    case "بانک سامان":
                        holder.background.setBackgroundResource(R.drawable.saman_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_saman_bank_logo);
                        break;
                    case "بانک ملی":
                        holder.background.setBackgroundResource(R.drawable.melli_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_melli_bank_logo);
                        break;
                    case "بانک سپه":
                        holder.background.setBackgroundResource(R.drawable.sepah_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_sepah_bank_logo);
                        break;
                    case "بانک توسعه صادرات":
                        holder.background.setBackgroundResource(R.drawable.tose_saderat_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_tose_saderat_bank_logo);
                        break;
                    case "بانک صنعت و معدن":
                        holder.background.setBackgroundResource(R.drawable.sanat_madan_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_sanat_madan_bank_logo);
                        break;
                    case "بانک کشاورزی":
                        holder.background.setBackgroundResource(R.drawable.keshavarzi_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_keshavarzi_bank_logo);
                        break;
                    case "بانک مسکن":
                        holder.background.setBackgroundResource(R.drawable.maskan_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_maskan_bank_logo);
                        break;
                    case "بانک پارسیان":
                        holder.background.setBackgroundResource(R.drawable.parsian_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_parsian_bank_logo);
                        break;
                    case "بانک پاسارگاد":
                        holder.background.setBackgroundResource(R.drawable.pasargad_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.bank_pasargad_pec);
                        break;
                    case "بانک سینا":
                        holder.background.setBackgroundResource(R.drawable.sina_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_sina_bank_logo);
                        break;
                    case "بانک شهر":
                        holder.background.setBackgroundResource(R.drawable.shahr_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_shahr_bank_logo);
                        break;
                }
            }
        }


    }

    // binds the data to the view and textview in each row
// total number of rows
    @Override
    public int getItemCount() {
        return userCards.size() + 1;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView cardNum, shebaNum, name, expirationDate, add_item;
        private ImageButton edit;
        private ImageView cardIcon, defaultStar;
        private RelativeLayout background;


        ViewHolder(View itemView) {
            super(itemView);
            shebaNum = itemView.findViewById(R.id.tv_card_sheba);
            cardNum = itemView.findViewById(R.id.tv_card_num);
            expirationDate = itemView.findViewById(R.id.tv_card_expiration);
            name = itemView.findViewById(R.id.tv_card_name);
            edit = itemView.findViewById(R.id.ib_edit_card);
            cardIcon = itemView.findViewById(R.id.iv_bank);
            background = itemView.findViewById(R.id.rl_card);
            defaultStar = itemView.findViewById(R.id.iv_default_card);
            add_item = itemView.findViewById(R.id.tv_add_card);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public UserCard getItem(int id) {
        return userCards.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == userCards.size()) ? R.layout.adapter_add_cards : R.layout.adapter_all_user_cards;
    }
}

