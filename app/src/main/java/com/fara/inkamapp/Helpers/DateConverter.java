package com.fara.inkamapp.Helpers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateConverter implements Serializable {
    private static final long serialVersionUID = 0;

    public static String Format_Miladi_by_ResetTime() {

        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String date = year + "-" + String.format("%02d", month) + "-"
                + String.format("%02d", day) + " 00:00:00";

        return Numbers.ToEnglishNumbers(date);
    }

    public static String Format_Miladi_CurrentDate() {

        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        String date = year + "-" + String.format("%02d", month) + "-"
                + String.format("%02d", day) + " "
                + String.format("%02d", hour) + ":"
                + String.format("%02d", minute) + ":"
                + String.format("%02d", second);

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // String str = sdf.format(cal.getTime());

        return Numbers.ToEnglishNumbers(date);
    }

    public static String Format_Miladi_CurrentDateAsAName() {

        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        String date = String.format("%02d", hour) + ""
                + String.format("%02d", minute) + ""
                + String.format("%02d", second) + " _ "
                + String.format("%02d", day) + "-"
                + String.format("%02d", month) + "-"
                + String.format("%02d", year);

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // String str = sdf.format(cal.getTime());

        return Numbers.ToPersianNumbers(date);
    }

    public static String Convert_Miladi_To_Shamsi_Date_ByTime(
            String GeorgianDate) {

        String[] str = GeorgianDate.split("-");
        int year = Integer.valueOf(str[0].trim());
        int month = Integer.valueOf(str[1].trim());
        String[] str2 = str[2].split(" ");
        int day = Integer.valueOf(str2[0].trim());

        JavaSource_Calendar c1 = new JavaSource_Calendar();
        c1.setGregorianDate(year, month, day);

        String PersianDate = c1.getIranianDate() + "  " + (str2.length > 1 ? str2[1] : "");

        return Numbers.ToEnglishNumbers(PersianDate);
    }

    public static String Convert_Miladi_To_Shamsi_Date_ByTime_Sohrab(
            String GeorgianDate) {

        String[] str = GeorgianDate.split(" ");
        int year = Integer.valueOf(str[0].trim());
        int month = Integer.valueOf(str[1].trim());
        String[] str2 = str[2].split(" ");
        int day = Integer.valueOf(str2[0].trim());

        JavaSource_Calendar c1 = new JavaSource_Calendar();
        c1.setGregorianDate(year, month, day);

        String PersianDate = c1.getIranianDate() + "  " + (str2.length > 1 ? str2[1] : "");

        return Numbers.ToEnglishNumbers(PersianDate);
    }

    public static String Convert_Miladi_To_Shamsi_Date(String GeorgianDate) {

        if (GeorgianDate.contains("."))
            GeorgianDate.replace(".", "/");
        if (GeorgianDate.contains("-"))
            GeorgianDate.replace("-", "/");

        String[] str = GeorgianDate.split(" ");
        int day = Integer.valueOf(str[0].trim());
        int month = Integer.valueOf(str[1].trim());
//        String[] str2 = str[2].split(" ");
        int year = Integer.valueOf(str[2].trim());

        JavaSource_Calendar c1 = new JavaSource_Calendar();
        c1.setGregorianDate(year, month, day);

        String PersianDate = c1.getIranianDate();

        return Numbers.ToEnglishNumbers(PersianDate);
    }

    public static String Convert_Shamsi_To_Miladi_Date(String persianDate,
                                                       int hour, int minute) {

        if (persianDate.contains("."))
            persianDate.replace(".", "/");
        if (persianDate.contains("-"))
            persianDate.replace("-", "/");

        Date miladi_Date = new JalaliCalendar().getGregorianDate(persianDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(miladi_Date);

        String dt = String.format("%04d", cal.get(Calendar.YEAR)) + "-"
                + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-"
                + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)) + " "
                + String.format("%02d", hour) + ":"
                + String.format("%02d", minute) + ":" + "00";

        return Numbers.ToEnglishNumbers(dt);
    }

    public static String Convert_Shamsi_To_Miladi_DateTime(String persianDate,
                                                           int hour, int minute, int second) {

        if (persianDate.contains("."))
            persianDate.replace(".", "/");
        if (persianDate.contains("-"))
            persianDate.replace("-", "/");

        Date miladi_Date = new JalaliCalendar().getGregorianDate(persianDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(miladi_Date);

        String dt = String.format("%04d", cal.get(Calendar.YEAR)) + "-"
                + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-"
                + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)) + " "
                + String.format("%02d", hour) + ":"
                + String.format("%02d", minute) + ":"
                + String.format("%02d", second);

        return Numbers.ToEnglishNumbers(dt);
    }

    public static String Format_mm_dd_yyyy(String GeorgianDate) {

        String[] str = GeorgianDate.split("-");
        int year = Integer.valueOf(str[0].trim());
        int month = Integer.valueOf(str[1].trim());
        String[] str2 = str[2].split(" ");
        int day = Integer.valueOf(str2[0].trim());

        String[] strTime = str2[1].split(":");
        int hour = Integer.valueOf(strTime[0]);
        int minute = Integer.valueOf(strTime[1]);
        int second = Integer.valueOf(strTime[2]);

        String dateString = String.format("%02d", month) + "/"
                + String.format("%02d", day) + "/"
                + String.format("%02d", year) + " "
                + String.format("%02d", hour) + ":"
                + String.format("%02d", minute) + ":"
                + String.format("%02d", second);

        return Numbers.ToEnglishNumbers(dateString);
    }

    public static String Format_Date(Date MiladiDate) {

        MiladiDate.setSeconds(0);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(MiladiDate);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(c1.getTime());

        return Numbers.ToEnglishNumbers(str);
    }

    public static String Format_Date_by_ResetTime(Date MiladiDate) {

        MiladiDate.setSeconds(0);
        MiladiDate.setMinutes(0);
        MiladiDate.setHours(0);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(MiladiDate);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(c1.getTime());

        return Numbers.ToEnglishNumbers(str);
    }
}
