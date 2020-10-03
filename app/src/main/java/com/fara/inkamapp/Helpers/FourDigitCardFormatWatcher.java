package com.fara.inkamapp.Helpers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fara.inkamapp.R;

public class FourDigitCardFormatWatcher implements TextWatcher {

    // Change this to what you want... ' ', '-' etc..
    private final String space = "  ";
    private EditText et_filed;
    private RelativeLayout card_background;
    private ImageView bank_logo;


    public FourDigitCardFormatWatcher(EditText et_filed, RelativeLayout rl_background, ImageView logo) {
        this.et_filed = et_filed;
        this.card_background = rl_background;
        this.bank_logo = logo;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", "").length() < 6) {
            card_background.setBackgroundResource(R.drawable.gray_rounded_background);
            bank_logo.setImageResource(R.drawable.empty_oval);
        }

        if (et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", "").length() == 6) {
            if (BankRegix.isAnsar(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.ansar_bank_back);
                bank_logo.setImageResource(R.drawable.ic_ansar_bank_logo);
            } else if (BankRegix.isDey(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.dey_bank_back);
                bank_logo.setImageResource(R.drawable.ic_dey_bank_logo);
            } else if (BankRegix.isEghtesadNovin(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.eghtesad_novin_bank_back);
                bank_logo.setImageResource(R.drawable.ic_eghtesad_novin_bank_logo);
            } else if (BankRegix.isKarafarin(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.karafarin_bank_back);
                bank_logo.setImageResource(R.drawable.ic_karafarin_bank_logo);
            } else if (BankRegix.isKeshavarzi(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.keshavarzi_bank_back);
                bank_logo.setImageResource(R.drawable.ic_keshavarzi_bank_logo);
            } else if (BankRegix.isMaskan(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.maskan_bank_back);
                bank_logo.setImageResource(R.drawable.ic_maskan_bank_logo);
            } else if (BankRegix.isMellat(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.mellat_bank_back);
                bank_logo.setImageResource(R.drawable.ic_mellat_bank_logo);
            } else if (BankRegix.isMelli(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.melli_bank_back);
                bank_logo.setImageResource(R.drawable.ic_melli_bank_logo);
            } else if (BankRegix.isParsian(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.parsian_bank_back);
                bank_logo.setImageResource(R.drawable.ic_parsian_bank_logo);
            } else if (BankRegix.isPasargad(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.pasargad_bank_back);
                bank_logo.setImageResource(R.drawable.bank_pasargad_pec);
            } else if (BankRegix.isPostBank(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.post_bank_back);
                bank_logo.setImageResource(R.drawable.ic_post_bank_logo);
            } else if (BankRegix.isRefah(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.refah_bank_back);
                bank_logo.setImageResource(R.drawable.ic_refah_kargaran_bank_logo);
            } else if (BankRegix.isSaderat(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.saderat_iran_bank_back);
                bank_logo.setImageResource(R.drawable.ic_saderat_bank_logo);
            } else if (BankRegix.isSaman(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.saman_bank_back);
                bank_logo.setImageResource(R.drawable.ic_saman_bank_logo);
            } else if (BankRegix.isSanatVaMadan(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.sanat_madan_bank_back);
                bank_logo.setImageResource(R.drawable.ic_sanat_madan_bank_logo);
            } else if (BankRegix.isSarmaye(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.sarmaye_bank_back);
                bank_logo.setImageResource(R.drawable.ic_sarmaye_bank_logo);
            } else if (BankRegix.isSepah(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.sepah_bank_back);
                bank_logo.setImageResource(R.drawable.ic_sepah_bank_logo);
            } else if (BankRegix.isShahr(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.shahr_bank_back);
                bank_logo.setImageResource(R.drawable.ic_shahr_bank_logo);
            } else if (BankRegix.isSina(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.sina_bank_back);
                bank_logo.setImageResource(R.drawable.ic_sina_bank_logo);
            } else if (BankRegix.isTejarat(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.tejarat_bank_back);
                bank_logo.setImageResource(R.drawable.ic_tejarat_bank_logo);
            } else if (BankRegix.isToseSaderat(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.tose_saderat_bank_back);
                bank_logo.setImageResource(R.drawable.ic_tose_saderat_bank_logo);
            } else if (BankRegix.isToseTaavon(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.tose_taavon_bank_back);
                bank_logo.setImageResource(R.drawable.ic_tose_taavon_bank_logo);
            } else if (BankRegix.isIranZamin(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.iran_zamin_bank_back);
                bank_logo.setImageResource(R.drawable.ic_iran_zamin_bank_logo);
            } else if (BankRegix.isAyande(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.ayande_bank_back);
                bank_logo.setImageResource(R.drawable.ic_ayande_bank_logo);
            } else if (BankRegix.isGhavamin(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.ghavamin_bank_back);
                bank_logo.setImageResource(R.drawable.ic_ghavamin_bank_logo);
            } else if (BankRegix.isGardeshgari(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.gardeshgari_bank_back);
                bank_logo.setImageResource(R.drawable.ic_gardeshgari_bank_logo);
            } else if (BankRegix.isHekmatIranian(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.hekmat_iranian_bank);
                bank_logo.setImageResource(R.drawable.ic_hekmat_bank_logo);
            } else if (BankRegix.isGharzolHasaneResalat(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.gharzol_hasane_resalat_bank_back);
                bank_logo.setImageResource(R.drawable.ic_gharzol_hasane_resalat_bank_logo);
            } else if (BankRegix.isGharzolHasaneMehrIran(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
                card_background.setBackgroundResource(R.drawable.gharzol_hasane_mehr_iran_bank_back);
                bank_logo.setImageResource(R.drawable.ic_gharzol_hasane_mehr_bank_logo);

//            else if (BankRegix.isTaat(et_filed.getText().toString().replaceAll("(?<=\\d) +(?=\\d)", ""))) {
//                card_background.setBackgroundResource(R.drawable.gharzol_hasane_mehr_iran_bank_back);
//                bank_logo.setImageResource(R.drawable.bank_tat_pec);
            } else {
                card_background.setBackgroundResource(R.drawable.gray_rounded_background);
                bank_logo.setImageResource(R.drawable.empty_oval);
            }
        }


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String initial = s.toString();
        // remove all non-digits characters
        String processed = initial.replaceAll("\\D", "");

        if (processed.length() > 4) {
            processed = processed.replaceAll("(\\d{4})(?=\\d)", "$1 ");
        }

        //Remove the listener
        et_filed.removeTextChangedListener(this);

        //Assign processed text
        et_filed.setText(processed);

        try {
            et_filed.setSelection(processed.length());
        } catch (Exception e) {
            // TODO: handle exception
        }

        //Give back the listener
        et_filed.addTextChangedListener(this);
    }
}
