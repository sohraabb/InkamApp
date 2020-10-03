package com.fara.inkamapp.Helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankRegix {

    public static boolean isMelli(String input) {
        Pattern p = Pattern.compile("[6][0][3][7][9][9]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isSepah(String input) {
        Pattern p = Pattern.compile("[5][8][9][2][1][0]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isToseSaderat(String input) {
        Pattern p = Pattern.compile("[6][2][7][6][4][8]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isSanatVaMadan(String input) {
        Pattern p = Pattern.compile("[6][2][7][9][6][1]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isKeshavarzi(String input) {
        Pattern p = Pattern.compile("[6][0][3][7][7][0]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isMaskan(String input) {
        Pattern p = Pattern.compile("[6][2][8][0][2][3]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isPostBank(String input) {
        Pattern p = Pattern.compile("[6][2][7][7][6][0]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isToseTaavon(String input) {
        Pattern p = Pattern.compile("[5][0][2][9][0][8]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isEghtesadNovin(String input) {
        Pattern p = Pattern.compile("[6][2][7][4][1][2]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isParsian(String input) {
        Pattern p = Pattern.compile("[6][2][2][1][0][6]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isPasargad(String input) {
        Pattern p = Pattern.compile("[5][0][2][2][2][9]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isKarafarin(String input) {
        Pattern p = Pattern.compile("[6][2][7][4][8][8]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isSaman(String input) {
        Pattern p = Pattern.compile("[6][2][1][9][8][6]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isSina(String input) {
        Pattern p = Pattern.compile("[6][3][9][3][4][6]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isSarmaye(String input) {
        Pattern p = Pattern.compile("[6][3][9][6][0][7]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

//    public static boolean isTaat(String input) {
//        Pattern p = Pattern.compile("[6][3][6][2][1][4]");
//        Matcher m = p.matcher(input);
//        return m.matches();
//    }

    public static boolean isShahr(String input) {
        Pattern p = Pattern.compile("[5][0][2][8][0][6]");
        Pattern p2 = Pattern.compile("[5][0][4][7][0][6]");

        Matcher m = p.matcher(input);
        Matcher m2 = p2.matcher(input);

        return m.matches() || m2.matches();
    }

    public static boolean isDey(String input) {
        Pattern p = Pattern.compile("[5][0][2][9][3][8]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isSaderat(String input) {
        Pattern p = Pattern.compile("[6][0][3][7][6][9]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isMellat(String input) {
        Pattern p = Pattern.compile("[6][1][0][4][3][3]");
        Matcher m = p.matcher(input);
        return m.matches();

    }

    public static boolean isTejarat(String input) {
        Pattern p = Pattern.compile("[6][2][7][3][5][3]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isRefah(String input) {
        Pattern p = Pattern.compile("[5][8][9][4][6][3]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isAnsar(String input) {
        Pattern p = Pattern.compile("[6][2][7][3][8][1]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isMehrEghtesad(String input) {
        Pattern p = Pattern.compile("[6][3][9][3][7][0]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isIranZamin(String input) {
        Pattern p = Pattern.compile("[5][0][5][7][8][5]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isAyande(String input) {
        Pattern p = Pattern.compile("[6][3][6][2][1][4]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isGhavamin(String input) {
        Pattern p = Pattern.compile("[6][3][9][2][1][7]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isGardeshgari(String input) {
        Pattern p = Pattern.compile("[5][0][5][4][1][6]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isHekmatIranian(String input) {
        Pattern p = Pattern.compile("[6][3][6][9][4][9]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isGharzolHasaneResalat(String input) {
        Pattern p = Pattern.compile("[5][0][4][1][7][2]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

    public static boolean isGharzolHasaneMehrIran(String input) {
        Pattern p = Pattern.compile("[6][0][6][3][7][3]");
        Matcher m = p.matcher(input);
        return m.matches();
    }

}
