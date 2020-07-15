package com.fara.inkamapp.Models;

import java.util.List;

public class InternetPackages {
    private String ResponseCode;
    private List<String> ResponseMessage;
    private List<DataPlanLists> dataPlanLists;

    public void setDataPlanLists(List<DataPlanLists> dataPlanLists) {
        dataPlanLists = dataPlanLists;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public void setResponseMessage(List<String> responseMessage) {
        ResponseMessage = responseMessage;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public List<String> getResponseMessage() {
        return ResponseMessage;
    }

    public List<DataPlanLists> getDataPlanLists() {
        return dataPlanLists;
    }

    public class DataPlanLists {
        /// <summary>
        /// کد بسته
        /// </summary>
        public int Id;
        /// <summary>
        /// مبلغ بدون مالیات
        /// </summary>
        public double PriceWithoutTax;
        /// <summary>
        /// مبلغ با مالیات
        /// </summary>
        public double PriceWithTax;
        /// <summary>
        /// دوره بسته
        /// </summary>
        public int Period;
        /// <summary>
        /// عنوان بسته
        /// </summary>
        public String Title;
        /// <summary>
        ///  اپراتور سرویس دهنده
        /// </summary>
        public int Operator;
        /// <summary>
        /// قابلیت تمدید خودکار(y/n)
        /// </summary>
        public String AutoRenewal;
        /// <summary>
        ///  بسته نوع
        /// </summary>
        public int DataPlanType;

        public void setAutoRenewal(String autoRenewal) {
            AutoRenewal = autoRenewal;
        }

        public void setDataPlanType(int dataPlanType) {
            DataPlanType = dataPlanType;
        }

        public void setId(int id) {
            Id = id;
        }

        public void setOperator(int operator) {
            Operator = operator;
        }

        public void setPeriod(int period) {
            Period = period;
        }

        public void setPriceWithoutTax(double priceWithoutTax) {
            PriceWithoutTax = priceWithoutTax;
        }

        public void setPriceWithTax(double priceWithTax) {
            PriceWithTax = priceWithTax;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getAutoRenewal() {
            return AutoRenewal;
        }

        public String getTitle() {
            return Title;
        }

        public double getPriceWithoutTax() {
            return PriceWithoutTax;
        }

        public double getPriceWithTax() {
            return PriceWithTax;
        }

        public int getDataPlanType() {
            return DataPlanType;
        }

        public int getId() {
            return Id;
        }

        public int getOperator() {
            return Operator;
        }

        public int getPeriod() {
            return Period;
        }
    }
}
