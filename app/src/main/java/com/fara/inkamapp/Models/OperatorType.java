package com.fara.inkamapp.Models;

public enum OperatorType {
    Mtn(0), Mci(1), Rightel(2);


    private final int value;
    private OperatorType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

//public enum ChargeType {
//    mamooli("mamooli", 0),
//    shegeft("shegeft", 1);
//
//    private String stringValue;
//    private int intValue;
//
//    private ChargeType(String toString, int value) {
//        stringValue = toString;
//        intValue = value;
//    }
//
//    public enum IrancellBillChargeType {
//        افزایش_حد_اعتبار("افزایش_حد_اعتبار", 0),
//        افزایش_اعتبار("افزایش_اعتبار", 1);
//
//        private String stringValue;
//        private int intValue;
//
//        private IrancellBillChargeType(String toString, int value) {
//            stringValue = toString;
//            intValue = value;
//        }
//    }
//
//    public enum DataPlanType {
//        اعتباری("اعتباری", 0),
//        دایمی("دایمی", 1),
//        دیتا_رایتل("دیتا_رایتل", 2),
//        دائمی_ایرانسل_TD_LTE("دائمی_ایرانسل_TD_LTE", 3),
//        اعتباری_ایرانسل_TD_LTE("اعتباری_ایرانسل_TD_LTE", 4),
//        اینترنت_خطوط_اشتراکی_ایرانسل("اینترنت_خطوط_اشتراکی_ایرانسل", 5),
//        مکاممه_خطوط_اعتباری("مکاممه_خطوط_اعتباری", 6),
//        مکاممه_خطوط_دائمی("مکاممه_خطوط_دائمی", 7),
//        بسته_های_اینترنتی_شگفت_انگیز_ایرانسل("بسته_های_اینترنتی_شگفت_انگیز_ایرانسل", 8),
//        بسته_های_افزایش_حجم_اینترنت_رایتل("بسته_های_افزایش_حجم_اینترنت_رایتل", 9),
//        LTETDتنها_برای_بسته_های_اشتراکی_اعتباری_مکالمه_و_("LTETDتنها_برای_بسته_های_اشتراکی_اعتباری_مکالمه_و_", 10),
//        تنها_برای_بسته_های_اشتراکی_خ_دائمی_مکالمه_LTETD("تنها_برای_بسته_های_اشتراکی_خ_دائمی_مکالمه_LTETD", 11);
//
//
//        private String stringValue;
//        private int intValue;
//
//        private DataPlanType(String toString, int value) {
//            stringValue = toString;
//            intValue = value;
//        }
//    }
//
//    public enum InternetPeriod {
//        ساعتی("ساعتی", 0),
//        روزانه("روزانه", 1),
//        چند_روزه("چند_روزه", 2),
//        هفتگی("هفتگی", 3),
//        ماهانه("ماهانه", 4),
//        ماه6("ماه6", 5),
//        مکاممه_خطوط_اعتباری("مکاممه_خطوط_اعتباری", 6),
//        یکسامه("یکسامه", 7),
//        مناسبتی("مناسبتی", 8);
//
//
//        private String stringValue;
//        private int intValue;
//
//        private InternetPeriod(String toString, int value) {
//            stringValue = toString;
//            intValue = value;
//        }
//    }
//
//    public enum BankID {
//        بانک_مشترک_ایران_ونزول("بانک_مشترک_ایران_ونزول", 1),
//        بانک_توسعه_صادرات_ایران("بانک_توسعه_صادرات_ایران", 2),
//        بانک_صنعت_معدن("بانک_صنعت_معدن", 3),
//        بانک_کشاورزی("بانک_کشاورزی", 4),
//        مسکن("مسکن", 5),
//        توسعه_تعاون("توسعه_تعاون", 5),
//        اقتصاد_نوین("اقتصاد_نوین", 6),
//        پارسیان("پارسیان", 7),
//        پاسارگاد("پاسارگاد", 8),
//        کارآفرین("کارآفرین", 7),
//        سامان("سامان", 7),
//        سینا("سینا", 7),
//        سرمایه("سرمایه", 7),
//        شهر("شهر", 7),
//        دی("دی", 7),
//        صادرات("صادرات", 7),
//        ملت("ملت", 7),
//        تجارت("تجارت", 7),
//        رفاه_کارگران("رفاه_کارگران", 7),
//        حکمت_ایرانیان("حکمت_ایرانیان", 7),
//        گردشگری("گردشگری", 7),
//        ایران_زمین("ایران_زمین", 7),
//        قوامین("قوامین", 7),
//        انصار("انصار", 7),
//        خاورمیانه("خاورمیانه", 7),
//        مهراقتصاد("مهراقتصاد", 7),
//        آینده("آینده", 7),
//        ملی_ایران("ملی_ایران", 7),
//        سپه("سپه", 7),
//        پست_بانک_ایران("پست_بانک_ایران", 7),
//        قرض_الحسنه_مهر_ایران("قرض_الحسنه_مهر_ایران", 7),
//        قرض_الحسنه_رسالت("قرض_الحسنه_رسالت", 7),
//        موسسه_اعتباری_توسعه("موسسه_اعتباری_توسعه", 7),
//        موسسه_اعتباری_کوثر("موسسه_اعتباری_کوثر", 7),
//        موسسه_اعتباری_ملل_عسکریه("موسسه_اعتباری_ملل_عسکریه", 7),
//        موسسه_اعتباری_نور("موسسه_اعتباری_نور", 7),
//        موسسه_اعتباری_کاسپین("موسسه_اعتباری_کاسپین", 7);
//
//        private String stringValue;
//        private int intValue;
//
//        private BankID(String toString, int value) {
//            stringValue = toString;
//            intValue = value;
//        }
//    }
//
//    @Override
//    public String toString() {
//        return stringValue;
//
//    }
//}
