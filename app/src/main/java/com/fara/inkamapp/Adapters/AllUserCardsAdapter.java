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
import com.fara.inkamapp.Helpers.BankRegix;
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
    private String cardNo, expDate, bankMatcher;
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
                    bankMatcher = firstSix(cardNo);


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

            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidParameterSpecException | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (_cards.is_isDefault())
                holder.defaultStar.setVisibility(View.VISIBLE);
            else
                holder.defaultStar.setVisibility(View.INVISIBLE);


//            if (BankRegix.isTaat(bankMatcher)) {
//                holder.background.setBackgroundResource(R.drawable.gharzol_hasane_mehr_iran_bank_back);
//                holder.cardIcon.setImageResource(R.drawable.bank_tat_pec);
//            }
            if (BankRegix.isGharzolHasaneMehrIran(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.gharzol_hasane_mehr_iran_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_gharzol_hasane_mehr_bank_logo);
            } else if (BankRegix.isGharzolHasaneResalat(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.gharzol_hasane_resalat_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_gharzol_hasane_resalat_bank_logo);
            } else if (BankRegix.isHekmatIranian(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.hekmat_iranian_bank);
                holder.cardIcon.setImageResource(R.drawable.ic_hekmat_bank_logo);
            } else if (BankRegix.isGardeshgari(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.gardeshgari_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_gardeshgari_bank_logo);
            } else if (BankRegix.isGhavamin(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.ghavamin_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_ghavamin_bank_logo);
            } else if (BankRegix.isAyande(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.ayande_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_ayande_bank_logo);
            } else if (BankRegix.isIranZamin(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.ayande_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_ayande_bank_logo);
            } else if (BankRegix.isMehrEghtesad(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.iran_zamin_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_iran_zamin_bank_logo);
            } else if (BankRegix.isToseTaavon(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.tose_taavon_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_tose_taavon_bank_logo);
            } else if (BankRegix.isToseSaderat(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.tose_saderat_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_tose_saderat_bank_logo);
            } else if (BankRegix.isTejarat(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.tejarat_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_tejarat_bank_logo);
            } else if (BankRegix.isSina(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.sina_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_sina_bank_logo);
            } else if (BankRegix.isShahr(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.shahr_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_shahr_bank_logo);
            } else if (BankRegix.isSepah(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.sepah_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_sepah_bank_logo);
            } else if (BankRegix.isAnsar(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.ansar_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_ansar_bank_logo);
            } else if (BankRegix.isDey(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.dey_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_dey_bank_logo);
            } else if (BankRegix.isEghtesadNovin(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.eghtesad_novin_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_eghtesad_novin_bank_logo);
            } else if (BankRegix.isKarafarin(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.karafarin_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_karafarin_bank_logo);
            } else if (BankRegix.isKeshavarzi(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.keshavarzi_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_keshavarzi_bank_logo);
            } else if (BankRegix.isMaskan(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.maskan_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_maskan_bank_logo);
            } else if (BankRegix.isMellat(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.mellat_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_mellat_bank_logo);
            } else if (BankRegix.isMelli(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.melli_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_melli_bank_logo);
            } else if (BankRegix.isParsian(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.parsian_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_parsian_bank_logo);
            } else if (BankRegix.isPasargad(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.pasargad_bank_back);
                holder.cardIcon.setImageResource(R.drawable.bank_pasargad_pec);
            } else if (BankRegix.isPostBank(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.post_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_post_bank_logo);
            } else if (BankRegix.isRefah(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.refah_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_refah_kargaran_bank_logo);
            } else if (BankRegix.isSaderat(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.saderat_iran_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_saderat_bank_logo);
            } else if (BankRegix.isSaman(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.saman_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_saman_bank_logo);
            } else if (BankRegix.isSanatVaMadan(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.sanat_madan_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_sanat_madan_bank_logo);
            } else if (BankRegix.isSarmaye(bankMatcher)) {
                holder.background.setBackgroundResource(R.drawable.sarmaye_bank_back);
                holder.cardIcon.setImageResource(R.drawable.ic_sarmaye_bank_logo);

            } else {
                holder.background.setBackgroundResource(R.drawable.gray_rounded_background);
                holder.cardIcon.setImageResource(R.drawable.empty_oval);
            }


            /*if (_cards.get_bankName() != null) {
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
                    case "بانک تات":
                        holder.background.setBackgroundResource(R.drawable.gharzol_hasane_mehr_iran_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.bank_tat_pec);
                        break;

                    case "بانک ملت":
                        holder.background.setBackgroundResource(R.drawable.mellat_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_mellat_bank_logo);
                        break;

                    case "بانک انصار":
                        holder.background.setBackgroundResource(R.drawable.ansar_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_ansar_bank_logo);
                        break;

                    case "بانک دی":
                        holder.background.setBackgroundResource(R.drawable.dey_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_dey_bank_logo);
                        break;

                    case "بانک اقتصاد نوین":
                        holder.background.setBackgroundResource(R.drawable.eghtesad_novin_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_eghtesad_novin_bank_logo);
                        break;

                    case "بانک کارافرین":
                        holder.background.setBackgroundResource(R.drawable.karafarin_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_karafarin_bank_logo);
                        break;

                    case "پست بانک":
                        holder.background.setBackgroundResource(R.drawable.post_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_post_bank_logo);
                        break;

                    case "پست رفاه":
                        holder.background.setBackgroundResource(R.drawable.refah_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_refah_kargaran_bank_logo);
                        break;

                    case "بانک صادرات":
                        holder.background.setBackgroundResource(R.drawable.saderat_iran_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_saderat_bank_logo);
                        break;

                    case "بانک سرمایه":
                        holder.background.setBackgroundResource(R.drawable.sarmaye_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_sarmaye_bank_logo);
                        break;

                    case "بانک تجارت":
                        holder.background.setBackgroundResource(R.drawable.tejarat_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_tejarat_bank_logo);
                        break;

                    case "بانک توسعه تعاون":
                        holder.background.setBackgroundResource(R.drawable.tose_taavon_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_tose_taavon_bank_logo);
                        break;

                    case "بانک ایران زمین":
                        holder.background.setBackgroundResource(R.drawable.iran_zamin_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_iran_zamin_bank_logo);
                        break;

                    case "بانک آینده":
                        holder.background.setBackgroundResource(R.drawable.ayande_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_ayande_bank_logo);
                        break;

                    case "بانک قوامین":
                        holder.background.setBackgroundResource(R.drawable.ghavamin_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_ghavamin_bank_logo);
                        break;

                    case "بانک گردشگری":
                        holder.background.setBackgroundResource(R.drawable.gardeshgari_bank_back);
                        holder.cardIcon.setImageResource(R.drawable.ic_gardeshgari_bank_logo);
                        break;

                    case "بانک حکمت ایرانیان":
                        holder.background.setBackgroundResource(R.drawable.hekmat_iranian_bank);
                        holder.cardIcon.setImageResource(R.drawable.ic_hekmat_bank_logo);
                        break;
                }
            }
             */
        }
    }

    public String firstSix(String input) {

        if (input.length() < 6) {
            return input;
        } else {
            return input.substring(0, 6);
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

