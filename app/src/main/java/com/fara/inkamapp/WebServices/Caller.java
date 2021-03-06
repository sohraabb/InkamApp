package com.fara.inkamapp.WebServices;

import android.content.Context;
import android.util.Log;

import com.fara.inkamapp.Models.AirplainModels.AirReservation;
import com.fara.inkamapp.Models.AirplainModels.Airline;
import com.fara.inkamapp.Models.AirplainModels.GetAirplaneTicketList;
import com.fara.inkamapp.Models.ApproveInternetPackage;
import com.fara.inkamapp.Models.BookTicket;
import com.fara.inkamapp.Models.Bus;
import com.fara.inkamapp.Models.BusCities;
import com.fara.inkamapp.Models.BusSummary;
import com.fara.inkamapp.Models.Chances;
import com.fara.inkamapp.Models.City;
import com.fara.inkamapp.Models.ContactList;
import com.fara.inkamapp.Models.Data;
import com.fara.inkamapp.Models.Fund;
import com.fara.inkamapp.Models.NotificationList;
import com.fara.inkamapp.Models.PackagesDataPlan;
import com.fara.inkamapp.Models.PercentageCode;
import com.fara.inkamapp.Models.PhoneBillInfo;
import com.fara.inkamapp.Models.Profit;
import com.fara.inkamapp.Models.Province;
import com.fara.inkamapp.Models.RepeatPurchase;
import com.fara.inkamapp.Models.Report;
import com.fara.inkamapp.Models.Routes;
import com.fara.inkamapp.Models.ServiceBillInfo;
import com.fara.inkamapp.Models.CheckUsername;
import com.fara.inkamapp.Models.ProductAndService;
import com.fara.inkamapp.Models.ReserveTopupRequest;
import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.Models.SpecificTicketInfo;
import com.fara.inkamapp.Models.TokenResponse;
import com.fara.inkamapp.Models.TraceTicketStatus;
import com.fara.inkamapp.Models.TrafficFines;
import com.fara.inkamapp.Models.User;
import com.fara.inkamapp.Models.UserCard;
import com.fara.inkamapp.Models.UserList;
import com.fara.inkamapp.Models.UserWallet;
import com.fara.inkamapp.Models.WalletCredit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Caller extends Thread {
    private static final Object _balancee = null;
    public Soap cs;
    //AppSQLiteHelper db;
    private Context mContext;
    // public ResultID resultId;
    // public MatchTree resultMatchTree;
    private Context _soapContext;
    private int lang;

    public Caller() {

//         db = new AppSQLiteHelper(activity.getApplicationContext());
//         String url = "http://mylavan.com/tabletglobalservice.asmx";
//         cs = new SOAP("http://tempuri.org/", url);

        String url = "https://income-app.ir/webservice/webservice.asmx";
        String url2 = "http://172.16.1.53/InkamMvc/WebService/WebService.asmx";
//        String url3 = "http://172.16.1.32/WebService/WebService.asmx";

        cs = new Soap("http://tempuri.org/", url);
        Log.i("SORI CAller", "url o khoond");

    }

    public CheckUsername checkUsername(String phone) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        CheckUsername result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("phone");
        pi1.setValue(phone);
        pi1.setType(String.class);
        list.add(pi1);

        try {

            SoapPrimitive resp = cs.CallPrim("ChekUserName", list);

            if (resp != null) {

                result = new CheckUsername(resp);

            }

        } catch (Exception ex) {
            Log.e("Dambel_Caller", ex.toString());

        }
        return result;
    }

    public ResponseStatus insertPhone(String phone) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("phone");
        pi1.setValue(phone);
        pi1.setType(String.class);
        list.add(pi1);

        try {

            SoapObject resp = cs.Call("InsertPhone", list);

            if (resp != null) {

                result = new ResponseStatus(resp);

            }

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public boolean checkVerificationCode(String phone, String code) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("phone");
        pi1.setValue(phone);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("code");
        pi2.setValue(code);
        pi2.setType(String.class);
        list.add(pi2);

        try {

            SoapPrimitive resp = cs.CallPrim("CheckVerificationCode", list);
//            SoapPrimitive object = (SoapPrimitive) resp.getAttribute("CheckVerificationCodeResponse");

            return resp != null && resp.toString().equals("true");


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return false;
        }

    }

    public User agentLogin(String phone, String password, String key) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        User result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("phone");
        pi1.setValue(phone);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("password");
        pi2.setValue(password);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("key");
        pi3.setValue(key);
        pi3.setType(String.class);
        list.add(pi3);


        try {

            SoapObject resp = cs.Call("AgentLogin", list);
            SoapObject object = (SoapObject) resp.getProperty("UserList");
            SoapObject mainObject = (SoapObject) object.getProperty("User");

            result = new User(mainObject);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ResponseStatus insertPassword(String id, String Password, String code, String phone) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("id");
        pi1.setValue(id);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("Password");
        pi2.setValue(Password);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("code");
        pi3.setValue(code);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("phone");
        pi4.setValue(phone);
        pi4.setType(String.class);
        list.add(pi4);

        try {

            SoapObject resp = cs.Call("InsertPassword", list);

            if (resp != null) {

                result = new ResponseStatus(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ResponseStatus updatePassword(String id, String token, String lastPass, String newPassword) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("id");
        pi1.setValue(id);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("lastPass");
        pi3.setValue(lastPass);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("newPassword");
        pi4.setValue(newPassword);
        pi4.setType(String.class);
        list.add(pi4);

        try {

            SoapObject resp = cs.Call("ChangePassword", list);

            if (resp != null) {

                result = new ResponseStatus(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ResponseStatus updateUser(String token, String user) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("token");
        pi1.setValue(token);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("user");
        pi2.setValue(user);
        pi2.setType(String.class);
        list.add(pi2);

        try {

            SoapObject resp = cs.Call("UpdateUser", list);

            if (resp != null) {

                result = new ResponseStatus(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public UserList insertUser(String user) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        UserList result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("user");
        pi1.setValue(user);
        pi1.setType(String.class);
        list.add(pi1);

        try {

            SoapObject resp = cs.Call("InsertUser", list);

            if (resp != null) {

                result = new UserList(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ArrayList<ProductAndService> getProductAndService(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ProductAndService result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);


        try {
            ArrayList<ProductAndService> list1 = new ArrayList<ProductAndService>();

            SoapObject resp = cs.Call("GetProductAndService", list);
            SoapObject object = (SoapObject) resp.getProperty("ProductAndServiceList");

            for (int i = 0; i < object.getPropertyCount(); i++) {
                result = new ProductAndService((SoapObject) object.getProperty(i));
                list1.add(result);
            }
            return list1;

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;
        }

    }

    public User getUserById(String id, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        User result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("id");
        pi1.setValue(id);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);


        try {

            SoapObject resp = cs.Call("GetUserByID", list);
            SoapObject object = (SoapObject) resp.getProperty("UserList");
            SoapObject mainObject = (SoapObject) object.getProperty("User");

            result = new User(mainObject);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public UserWallet getUserWallet(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        UserWallet result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);


        try {

            SoapObject resp = cs.Call("GetUserWallet", list);

            result = new UserWallet(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public Profit GetUserProfitReport(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        Profit result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);


        try {

            SoapObject resp = cs.Call("GetUserProfitReport", list);

            result = new Profit(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ReserveTopupRequest topupServiceReserve(String userID, String token, String topupRequest, int type) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ReserveTopupRequest result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("topupRequest");
        pi3.setValue(topupRequest);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("type");
        pi4.setValue(type);
        pi4.setType(String.class);
        list.add(pi4);


        try {

            SoapObject resp = cs.Call("TopupServiceReserve", list);

            result = new ReserveTopupRequest(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ReserveTopupRequest approveTopupServiceReserve(String userID, String token, String data, String orderID, String amount, int type) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ReserveTopupRequest result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("data");
        pi3.setValue(data);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("orderID");
        pi4.setValue(orderID);
        pi4.setType(String.class);
        list.add(pi4);

        PropertyInfo pi5 = new PropertyInfo();
        pi5.setName("amount");
        pi5.setValue(amount);
        pi5.setType(String.class);
        list.add(pi5);

        PropertyInfo pi6 = new PropertyInfo();
        pi6.setName("type");
        pi6.setValue(type);
        pi6.setType(Integer.class);
        list.add(pi6);

        try {

            SoapObject resp = cs.Call("ApproveTopupServiceReserve", list);

            result = new ReserveTopupRequest(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ReserveTopupRequest topupServiceReserveFromWallet(String userID, String token, String topupRequest, int type) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ReserveTopupRequest result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("topupRequest");
        pi3.setValue(topupRequest);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("type");
        pi4.setValue(type);
        pi4.setType(String.class);
        list.add(pi4);


        try {

            SoapObject resp = cs.Call("TopupServiceReserveFromWallet", list);

            result = new ReserveTopupRequest(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ResponseStatus insertBillId(String userID, String token, String billID) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("billID");
        pi3.setValue(billID);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("InsertBillID", list);

            if (resp != null) {

                result = new ResponseStatus(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ResponseStatus insertBillTransaction(String userID, String token, String actionID, double amount, String targetPhone, String description, String billID, boolean period, String transactionNumber, String payID, boolean isFromWallet) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("actionID");
        pi3.setValue(actionID);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("amount");
        pi4.setValue(amount);
        pi4.setType(String.class);
        list.add(pi4);

        PropertyInfo pi5 = new PropertyInfo();
        pi5.setName("targetPhone");
        pi5.setValue(targetPhone);
        pi5.setType(String.class);
        list.add(pi5);

        PropertyInfo pi6 = new PropertyInfo();
        pi6.setName("description");
        pi6.setValue(description);
        pi6.setType(String.class);
        list.add(pi6);

        PropertyInfo pi7 = new PropertyInfo();
        pi7.setName("billID");
        pi7.setValue(billID);
        pi7.setType(String.class);
        list.add(pi7);

        PropertyInfo pi8 = new PropertyInfo();
        pi8.setName("period");
        pi8.setValue(period);
        pi8.setType(String.class);
        list.add(pi8);

        PropertyInfo pi9 = new PropertyInfo();
        pi9.setName("transactionNumber");
        pi9.setValue(transactionNumber);
        pi9.setType(String.class);
        list.add(pi9);

        PropertyInfo pi10 = new PropertyInfo();
        pi10.setName("payID");
        pi10.setValue(payID);
        pi10.setType(String.class);
        list.add(pi10);

        PropertyInfo pi11 = new PropertyInfo();
        pi11.setName("isFromWallet");
        pi11.setValue(isFromWallet);
        pi11.setType(String.class);
        list.add(pi11);

        try {

            SoapObject resp = cs.Call("InsertBillTransaction", list);

            if (resp != null) {

                result = new ResponseStatus(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ServiceBillInfo getElectricityBillInfoData(String billID, String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ServiceBillInfo result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("billID");
        pi1.setValue(billID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("userID");
        pi2.setValue(userID);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("token");
        pi3.setValue(token);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("GetElectricityBillInfoData", list);
            result = new ServiceBillInfo(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ServiceBillInfo getGasBillInfo(String billID, String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ServiceBillInfo result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("billID");
        pi1.setValue(billID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("userID");
        pi2.setValue(userID);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("token");
        pi3.setValue(token);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("GetGasBillInfo", list);
            result = new ServiceBillInfo(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ServiceBillInfo getWaterBillInfo(String billID, String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ServiceBillInfo result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("billID");
        pi1.setValue(billID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("userID");
        pi2.setValue(userID);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("token");
        pi3.setValue(token);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("GetWaterBillInfo", list);
            result = new ServiceBillInfo(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public TrafficFines getTrafficFinesInfo(String billID, String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        TrafficFines result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("billID");
        pi1.setValue(billID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("userID");
        pi2.setValue(userID);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("token");
        pi3.setValue(token);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("GetTrafficFinesInfo", list);
            result = new TrafficFines(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public PhoneBillInfo getHamrahAvalBillInfo(String MobileNo, String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        PhoneBillInfo result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("MobileNo");
        pi1.setValue(MobileNo);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("userID");
        pi2.setValue(userID);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("token");
        pi3.setValue(token);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("GetHamrahAvalBillInfo", list);
            result = new PhoneBillInfo(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public PhoneBillInfo getIrancelBillInfo(String MobileNo, String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        PhoneBillInfo result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("MobileNo");
        pi1.setValue(MobileNo);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("userID");
        pi2.setValue(userID);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("token");
        pi3.setValue(token);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("GetIrancelBillInfo", list);
            result = new PhoneBillInfo(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public PhoneBillInfo getRightelBillInfo(String MobileNo, String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        PhoneBillInfo result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("MobileNo");
        pi1.setValue(MobileNo);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("userID");
        pi2.setValue(userID);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("token");
        pi3.setValue(token);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("GetRightelBillInfo", list);
            result = new PhoneBillInfo(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public PhoneBillInfo getTelecomBillInfo(String MobileNo, String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        PhoneBillInfo result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("MobileNo");
        pi1.setValue(MobileNo);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("userID");
        pi2.setValue(userID);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("token");
        pi3.setValue(token);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("GetTelecomBillInfo", list);
            result = new PhoneBillInfo(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public String trafficBillPaymentFromWallet(String param, String pelak, String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        String _response = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("param");
        pi1.setValue(param);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("pelak");
        pi2.setValue(pelak);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("userID");
        pi3.setValue(userID);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("token");
        pi4.setValue(token);
        pi4.setType(String.class);
        list.add(pi4);

        try {

            SoapPrimitive resp = cs.CallPrim("TrafficBillPaymentFromWallet", list);
//            SoapPrimitive object = (SoapPrimitive) resp.getAttribute("CheckVerificationCodeResponse");
            if (resp != null) {
                _response = String.valueOf(resp);

            }

            return _response;


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return "";
        }
    }

    public String TrafficBillSuccess(String param, String pelak, String userID, String token, String orderID, String data) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        String _response = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("param");
        pi1.setValue(param);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("pelak");
        pi2.setValue(pelak);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("userID");
        pi3.setValue(userID);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("token");
        pi4.setValue(token);
        pi4.setType(String.class);
        list.add(pi4);
        PropertyInfo pi5 = new PropertyInfo();
        pi5.setName("orderID");
        pi5.setValue(orderID);
        pi5.setType(String.class);
        list.add(pi5);
        PropertyInfo pi6 = new PropertyInfo();
        pi6.setName("data");
        pi6.setValue(data);
        pi6.setType(String.class);
        list.add(pi6);

        try {

            SoapPrimitive resp = cs.CallPrim("TrafficBillSuccess", list);
//            SoapPrimitive object = (SoapPrimitive) resp.getAttribute("CheckVerificationCodeResponse");
            if (resp != null) {
                _response = String.valueOf(resp);

            }

            return _response;


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return "";
        }
    }

    public String BillPaymentFromWallet(String param, String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        String _response = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("param");
        pi1.setValue(param);
        pi1.setType(String.class);
        list.add(pi1);
        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("userID");
        pi3.setValue(userID);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("token");
        pi4.setValue(token);
        pi4.setType(String.class);
        list.add(pi4);

        try {

            SoapPrimitive resp = cs.CallPrim("BillPaymentFromWallet", list);
            SoapObject respp = cs.Call("BillPaymentFromWallet", list);
//            SoapPrimitive object = (SoapPrimitive) resp.getAttribute("CheckVerificationCodeResponse");
            if (resp != null) {
                _response = String.valueOf(resp);

            }

            return _response;


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return "";
        }
    }

    public ResponseStatus updateUserCard(String userID, String token, String card) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("card");
        pi3.setValue(card);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("UpdateUserCard", list);

            if (resp != null) {

                result = new ResponseStatus(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ResponseStatus deleteUserCard(String userID, String token, String cardID) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("cardID");
        pi3.setValue(cardID);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("DeleteUserCard", list);

            if (resp != null) {

                result = new ResponseStatus(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public UserCard getUserCardByID(String userID, String token, String cardID) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        UserCard result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("cardID");
        pi3.setValue(cardID);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("GetUserCardByID", list);

            if (resp != null) {

                result = new UserCard(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ArrayList<UserCard> getAllUserCard(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        UserCard result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        try {
            ArrayList<UserCard> cardList = new ArrayList<UserCard>();
            SoapObject resp = cs.Call("GetAllUserCard", list);
            SoapObject object = (SoapObject) resp.getProperty("UserCardList");


            for (int i = 0; i < object.getPropertyCount(); i++) {
                result = new UserCard((SoapObject) object.getProperty(i));
                cardList.add(result);
            }
            return cardList;

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;
        }
    }

    public ArrayList<City> getCityByProvince(String id) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        City result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("id");
        pi1.setValue(id);
        pi1.setType(String.class);
        list.add(pi1);


        try {
            ArrayList<City> cities = new ArrayList<City>();
            SoapObject resp = cs.Call("GetCityByProvince", list);
            SoapObject object = (SoapObject) resp.getProperty("CityList");

            for (int i = 0; i < object.getPropertyCount(); i++) {
                result = new City((SoapObject) object.getProperty(i));
                cities.add(result);
            }
            return cities;


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;
        }
    }

    public double getUserWalletBalance(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        double _response = 0;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        try {

            SoapObject resp = cs.Call("GetUserWalletBalance", list);

            if (resp != null) {

                _response = Double.parseDouble(resp.getPropertySafelyAsString("GetUserWalletBalanceResult"));
            }

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return _response;
    }

    public ArrayList<Report> getWalletTransactionReport(String userID, String token, String fromDate, String toDate) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        Report result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("fromDate");
        pi3.setValue(fromDate);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("toDate");
        pi4.setValue(toDate);
        pi4.setType(String.class);
        list.add(pi4);


        try {
            ArrayList<Report> reportList = new ArrayList<Report>();
            SoapObject resp = cs.Call("GetWalletTransactionReport", list);
            SoapObject object = (SoapObject) resp.getProperty("ReportList");

            for (int i = 0; i < object.getPropertyCount(); i++) {
                result = new Report((SoapObject) object.getProperty(i));
                reportList.add(result);
            }
            return reportList;

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;
        }
    }

    public ArrayList<Report> getAllTransactionReport(String userID, String token, String fromDate, String toDate, int type) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        Report result;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("fromDate");
        pi3.setValue(fromDate);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("toDate");
        pi4.setValue(toDate);
        pi4.setType(String.class);
        list.add(pi4);

        PropertyInfo pi5 = new PropertyInfo();
        pi5.setName("type");
        pi5.setValue(type);
        pi5.setType(Integer.class);
        list.add(pi5);


        try {
            ArrayList<Report> reportList = new ArrayList<Report>();
            SoapObject resp = cs.Call("GetTransactionReport", list);
            SoapObject object = (SoapObject) resp.getProperty("ReportList");


            for (int i = 0; i < object.getPropertyCount(); i++) {
                result = new Report((SoapObject) object.getProperty(i));
                reportList.add(result);
            }
            return reportList;

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;
        }
    }

    public ResponseStatus insertTransactionConfirm(String userID, String token, String transactionId, String status, long rrn, String cardnumber, String paymentToken, String orderID, String message) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("transactionId");
        pi3.setValue(transactionId);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("status");
        pi4.setValue(status);
        pi4.setType(String.class);
        list.add(pi4);

        PropertyInfo pi5 = new PropertyInfo();
        pi5.setName("rrn");
        pi5.setValue(rrn);
        pi5.setType(String.class);
        list.add(pi5);

        PropertyInfo pi6 = new PropertyInfo();
        pi6.setName("cardnumber");
        pi6.setValue(cardnumber);
        pi6.setType(String.class);
        list.add(pi6);

        PropertyInfo pi7 = new PropertyInfo();
        pi7.setName("paymentToken");
        pi7.setValue(paymentToken);
        pi7.setType(String.class);
        list.add(pi7);

        PropertyInfo pi8 = new PropertyInfo();
        pi8.setName("orderID");
        pi8.setValue(orderID);
        pi8.setType(String.class);
        list.add(pi8);

        PropertyInfo pi9 = new PropertyInfo();
        pi9.setName("message");
        pi9.setValue(message);
        pi9.setType(String.class);
        list.add(pi9);

        try {

            SoapObject resp = cs.Call("InsertTransactionConfirm", list);

            if (resp != null) {

                result = new ResponseStatus(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ResponseStatus insertChargeTransaction(String userID, String token, String actionID, String chargTypeCode, double amount, String targetPhone, String description, String transactionNumber, boolean isFromWallet) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("actionID");
        pi3.setValue(actionID);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("chargTypeCode");
        pi4.setValue(chargTypeCode);
        pi4.setType(String.class);
        list.add(pi4);

        PropertyInfo pi5 = new PropertyInfo();
        pi5.setName("amount");
        pi5.setValue(amount);
        pi5.setType(String.class);
        list.add(pi5);

        PropertyInfo pi6 = new PropertyInfo();
        pi6.setName("targetPhone");
        pi6.setValue(targetPhone);
        pi6.setType(String.class);
        list.add(pi6);

        PropertyInfo pi7 = new PropertyInfo();
        pi7.setName("description");
        pi7.setValue(description);
        pi7.setType(String.class);
        list.add(pi7);

        PropertyInfo pi8 = new PropertyInfo();
        pi8.setName("transactionNumber");
        pi8.setValue(transactionNumber);
        pi8.setType(String.class);
        list.add(pi8);

        PropertyInfo pi9 = new PropertyInfo();
        pi9.setName("isFromWallet");
        pi9.setValue(isFromWallet);
        pi9.setType(String.class);
        list.add(pi9);

        try {

            SoapObject resp = cs.Call("InsertChargeTransaction", list);

            if (resp != null) {
                result = new ResponseStatus(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ResponseStatus insertInternetTransaction(String userID, String token, String actionID, double amount, String targetPhone, String serviceProviderID, String packageDescription, String description, String transactionNumber, boolean isFromWallet) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("actionID");
        pi3.setValue(actionID);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("amount");
        pi4.setValue(amount);
        pi4.setType(String.class);
        list.add(pi4);

        PropertyInfo pi5 = new PropertyInfo();
        pi5.setName("targetPhone");
        pi5.setValue(targetPhone);
        pi5.setType(String.class);
        list.add(pi5);

        PropertyInfo pi6 = new PropertyInfo();
        pi6.setName("serviceProviderID");
        pi6.setValue(serviceProviderID);
        pi6.setType(String.class);
        list.add(pi6);

        PropertyInfo pi7 = new PropertyInfo();
        pi7.setName("packageDescription");
        pi7.setValue(packageDescription);
        pi7.setType(String.class);
        list.add(pi7);

        PropertyInfo pi8 = new PropertyInfo();
        pi8.setName("description");
        pi8.setValue(description);
        pi8.setType(String.class);
        list.add(pi8);

        PropertyInfo pi9 = new PropertyInfo();
        pi9.setName("transactionNumber");
        pi9.setValue(transactionNumber);
        pi9.setType(String.class);
        list.add(pi9);

        PropertyInfo pi10 = new PropertyInfo();
        pi10.setName("isFromWallet");
        pi10.setValue(isFromWallet);
        pi10.setType(String.class);
        list.add(pi10);

        try {

            SoapObject resp = cs.Call("InsertInternetTransaction", list);

            if (resp != null) {
                result = new ResponseStatus(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ReserveTopupRequest internetPackageReserve(String userID, String token, String topupRequest) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ReserveTopupRequest result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("topupRequest");
        pi3.setValue(topupRequest);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("InternetPackageReserve", list);

            result = new ReserveTopupRequest(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ApproveInternetPackage InternetPackageApprove(String userID, String token, String data, String orderID, String amount) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ApproveInternetPackage result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("data");
        pi3.setValue(data);
        pi3.setType(String.class);
        list.add(pi3);
        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("orderID");
        pi4.setValue(orderID);
        pi4.setType(String.class);
        list.add(pi4);

        PropertyInfo pi5 = new PropertyInfo();
        pi5.setName("amount");
        pi5.setValue(amount);
        pi5.setType(String.class);
        list.add(pi5);

        try {

            SoapObject resp = cs.Call("InternetPackageApprove", list);

            result = new ApproveInternetPackage(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ResponseStatus insertUserCard(String userID, String token, String card) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("card");
        pi3.setValue(card);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("InsertUserCard", list);

            if (resp != null) {

                result = new ResponseStatus(resp);

            }

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public String IncreaseWalletCreditRequest(String userID, String token, long mobile, String amount) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        String result = "";
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("mobile");
        pi3.setValue(mobile);
        pi3.setType(Long.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("amount");
        pi4.setValue(amount);
        pi4.setType(Long.class);
        list.add(pi4);

        try {

            SoapPrimitive resp = cs.CallPrim("IncreaseWalletCreditRequest", list);

            if (resp != null) {

                return resp.toString();

            }

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public String increaseWalletCreditRequestConfirm(String userID, String token, String data, String amount) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        String result = "";
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("data");
        pi3.setValue(data);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("amount");
        pi4.setValue(amount);
        pi4.setType(Double.class);
        list.add(pi4);

        try {

            SoapObject resp = cs.Call("IncreaseWalletCreditRequestConfirm", list);

            if (resp != null)
                result = resp.getPropertySafelyAsString("IncreaseWalletCreditRequestConfirmResult");


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ReserveTopupRequest pinChargeReserveFromWallet(String userID, String token, String topupRequest, int type) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ReserveTopupRequest result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("topupRequest");
        pi3.setValue(topupRequest);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("type");
        pi4.setValue(type);
        pi4.setType(String.class);
        list.add(pi4);

        try {

            SoapObject resp = cs.Call("PinChargeReserveFromWallet", list);

            if (resp != null) {

                result = new ReserveTopupRequest(resp);

            }

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public String bookBusTicket(String userID, String token, String tiketToBook) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        String result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("tiketToBook");
        pi3.setValue(tiketToBook);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapPrimitive resp = cs.CallPrim("bookBusTicketForApp", list);

            if (resp != null) {

                return resp.toString();

            }

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public String bookTicketFromWallet(String userID, String token, String tiketToBook) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        String result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("tiketToBook");
        pi3.setValue(tiketToBook);
        pi3.setType(String.class);
        list.add(pi3);


        try {

            SoapPrimitive resp = cs.CallPrim("bookTicketFromWallet", list);
            if (resp != null)
                return resp.toString();
            //  result = new BookTicket(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public SpecificTicketInfo getSpecificTicket(String userID, String token, String ticketID) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        SpecificTicketInfo result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("ticketID");
        pi3.setValue(ticketID);
        pi3.setType(String.class);
        list.add(pi3);


        try {

            SoapObject resp = cs.Call("getSpecificTicket", list);

            result = new SpecificTicketInfo(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public TraceTicketStatus traceTicketStatusWithTraceNumber(String userID, String token, String number) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        TraceTicketStatus result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("number");
        pi3.setValue(number);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("traceTicketStatusWithTraceNumber", list);

            result = new TraceTicketStatus(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public WalletCredit busTicketSuccess(String userID, String token, String data, String orderID, String amount) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        WalletCredit result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("data");
        pi3.setValue(data);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("orderID");
        pi4.setValue(orderID);
        pi4.setType(Long.class);
        list.add(pi4);

        PropertyInfo pi5 = new PropertyInfo();
        pi5.setName("amount");
        pi5.setValue(amount);
        pi5.setType(Double.class);
        list.add(pi5);

        try {

            SoapObject resp = cs.Call("BusTicketSuccess", list);

            result = new WalletCredit(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ArrayList<BusCities> getBusCities(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        BusCities result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        try {
            ArrayList<BusCities> busCities = new ArrayList<BusCities>();
            SoapObject resp = cs.Call("getBusCities", list);

            for (int i = 0; i < resp.getPropertyCount(); i++) {
                result = new BusCities((SoapObject) resp.getProperty(i));
                busCities.add(result);
            }
            return busCities;

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;
        }
    }

    public ArrayList<Routes> getBusRoutes(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        Routes result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        try {
            ArrayList<Routes> routs = new ArrayList<Routes>();
            SoapObject resp = cs.Call("getBusRoutes", list);

            for (int i = 0; i < resp.getPropertyCount(); i++) {
                result = new Routes((SoapObject) resp.getProperty(i));
                routs.add(result);
            }
            return routs;

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;
        }
    }

    public String getProfileImageByUserName(String username) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        String _response = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("username");
        pi1.setValue(username);
        pi1.setType(String.class);
        list.add(pi1);

        try {

            SoapPrimitive resp = cs.CallPrim("GetProfileImageByUserName", list);
//            SoapPrimitive object = (SoapPrimitive) resp.getAttribute("CheckVerificationCodeResponse");
            if (resp != null) {
                _response = String.valueOf(resp);

            }

            return _response;


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return "";
        }
    }

    public ArrayList<NotificationList> getAllNotifications(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        NotificationList result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        try {
            ArrayList<NotificationList> notifications = new ArrayList<NotificationList>();
            SoapObject resp = cs.Call("GetNotification", list);
            SoapObject object = (SoapObject) resp.getProperty("NotificationList");

            for (int i = 0; i < object.getPropertyCount(); i++) {
                result = new NotificationList((SoapObject) object.getProperty(i));
                notifications.add(result);
            }
            return notifications;

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;
        }
    }

    public String getAllBillIDs(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        String _response = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        try {

            SoapPrimitive resp = cs.CallPrim("GetAllBillIDs", list);
//            SoapPrimitive object = (SoapPrimitive) resp.getAttribute("CheckVerificationCodeResponse");
            if (resp != null) {
                _response = String.valueOf(resp);

            }

            return _response;


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return "";
        }
    }

    public ArrayList<PackagesDataPlan> getDataPlanListByPeriod(int operators, int period, int dataPlanType, String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        PackagesDataPlan result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("operators");
        pi1.setValue(operators);
        pi1.setType(Integer.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("period");
        pi2.setValue(period);
        pi2.setType(Integer.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("dataPlanType");
        pi3.setValue(dataPlanType);
        pi3.setType(Integer.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("userID");
        pi4.setValue(userID);
        pi4.setType(String.class);
        list.add(pi4);

        PropertyInfo pi5 = new PropertyInfo();
        pi5.setName("token");
        pi5.setValue(token);
        pi5.setType(String.class);
        list.add(pi5);

        try {
            ArrayList<PackagesDataPlan> packagesDataPlans = new ArrayList<PackagesDataPlan>();
            SoapObject resp = cs.Call("GetDataPlanListByPeriod", list);
            SoapObject object = (SoapObject) resp.getProperty("DataPlanLists");

            for (int i = 0; i < object.getPropertyCount(); i++) {
                result = new PackagesDataPlan((SoapObject) object.getProperty(i));
                packagesDataPlans.add(result);
            }
            return packagesDataPlans;


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;

        }
    }

    public WalletCredit increaseWalletCreditRequest(String userID, String token, long mobile, long amount) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        WalletCredit result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("mobile");
        pi3.setValue(mobile);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("amount");
        pi4.setValue(amount);
        pi4.setType(String.class);
        list.add(pi4);

        try {

            SoapObject resp = cs.Call("IncreaseWalletCreditRequest", list);

            if (resp != null) {

                result = new WalletCredit(resp);

            }

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ResponseStatus insertContact(String userID, String token, String contact) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("contact");
        pi3.setValue(contact);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("InsertContact", list);

            if (resp != null) {

                result = new ResponseStatus(resp);

            }

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ResponseStatus deleteContact(String userID, String token, String contactID) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("contactID");
        pi3.setValue(contactID);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("DeleteContact", list);

            if (resp != null) {

                result = new ResponseStatus(resp);

            }

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ResponseStatus updateContact(String userID, String token, String contact) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("contact");
        pi3.setValue(contact);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("UpdateContact", list);

            if (resp != null) {

                result = new ResponseStatus(resp);

            }

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ArrayList<ContactList> getAllContacts(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ContactList result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        try {
            ArrayList<ContactList> contacts = new ArrayList<ContactList>();
            SoapObject resp = cs.Call("getAllContacts", list);
            SoapObject object = (SoapObject) resp.getProperty("ContactList");

            for (int i = 0; i < object.getPropertyCount(); i++) {
                result = new ContactList((SoapObject) object.getProperty(i));
                contacts.add(result);
            }
            return contacts;


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;

        }
    }

    public ArrayList<ContactList> searchContacts(String userID, String token, String prefix) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ContactList result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("prefix");
        pi3.setValue(prefix);
        pi3.setType(String.class);
        list.add(pi3);

        try {
            ArrayList<ContactList> contacts = new ArrayList<ContactList>();
            SoapObject resp = cs.Call("searchContacts", list);
            SoapObject object = (SoapObject) resp.getProperty("ContactList");

            for (int i = 0; i < object.getPropertyCount(); i++) {
                result = new ContactList((SoapObject) object.getProperty(i));
                contacts.add(result);
            }
            return contacts;


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;

        }
    }

    public ArrayList<ContactList> getContactByPhone(String userID, String token, String phone) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ContactList result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("phone");
        pi3.setValue(phone);
        pi3.setType(String.class);
        list.add(pi3);

        try {
            ArrayList<ContactList> contacts = new ArrayList<ContactList>();
            SoapObject resp = cs.Call("getContactByPhone", list);
            SoapObject object = (SoapObject) resp.getProperty("ContactList");

            for (int i = 0; i < object.getPropertyCount(); i++) {
                result = new ContactList((SoapObject) object.getProperty(i));
                contacts.add(result);
            }
            return contacts;


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;

        }
    }

    public boolean CheckContactExist(String userID, String token, String phone) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("phone");
        pi3.setValue(phone);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapPrimitive resp = cs.CallPrim("CheckContactExist", list);
//            SoapPrimitive object = (SoapPrimitive) resp.getAttribute("CheckVerificationCodeResponse");

            return resp != null && resp.toString().equals("true");


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return false;
        }

    }

    public ArrayList<NotificationList> getMessages(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        NotificationList result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        try {
            ArrayList<NotificationList> messages = new ArrayList<NotificationList>();
            SoapObject resp = cs.Call("GetMessages", list);
            SoapObject object = (SoapObject) resp.getProperty("NotificationList");

            for (int i = 0; i < object.getPropertyCount(); i++) {
                result = new NotificationList((SoapObject) object.getProperty(i));
                messages.add(result);
            }
            return messages;

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;
        }
    }

    public String getShebaFromCard(String userID, String token, String cardPan, String nationalCode, String phone) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        String _response = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("cardPan");
        pi3.setValue(cardPan);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("nationalCode");
        pi4.setValue(nationalCode);
        pi4.setType(String.class);
        list.add(pi4);

        PropertyInfo pi5 = new PropertyInfo();
        pi5.setName("phone");
        pi5.setValue(phone);
        pi5.setType(String.class);
        list.add(pi5);

        try {

            SoapPrimitive resp = cs.CallPrim("getShebaFromCard", list);
//            SoapPrimitive object = (SoapPrimitive) resp.getAttribute("CheckVerificationCodeResponse");
            if (resp != null) {
                _response = String.valueOf(resp);

            }

            return _response;


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return "";
        }
    }

    public ResponseStatus insertPresentageCode(String userID, String token, String code, String name, boolean isdefault) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("code");
        pi3.setValue(code);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("name");
        pi4.setValue(name);
        pi4.setType(String.class);
        list.add(pi4);

        PropertyInfo pi5 = new PropertyInfo();
        pi5.setName("isdefault");
        pi5.setValue(isdefault);
        pi5.setType(Boolean.class);
        list.add(pi5);


        try {

            SoapObject resp = cs.Call("InsertPresentageCode", list);

            if (resp != null) {

                result = new ResponseStatus(resp);

            }

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ArrayList<PercentageCode> getAllPresentageCode(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        PercentageCode result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);


        try {
            ArrayList<PercentageCode> list1 = new ArrayList<PercentageCode>();

            SoapObject resp = cs.Call("Get_All_PresentageCode", list);
            SoapObject object = (SoapObject) resp.getProperty("PresentageCodeList");

            for (int i = 0; i < object.getPropertyCount(); i++) {
                result = new PercentageCode((SoapObject) object.getProperty(i));
                list1.add(result);
            }
            return list1;

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;
        }
    }

    public String getChargeType(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        String _response = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        try {

            SoapPrimitive resp = cs.CallPrim("GetChargeType", list);
//            SoapPrimitive object = (SoapPrimitive) resp.getAttribute("CheckVerificationCodeResponse");
            if (resp != null) {
                _response = String.valueOf(resp);

            }

            return _response;


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return "";
        }
    }

    public ArrayList<Province> getProvince() {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        Province result = null;

        try {
            ArrayList<Province> provinceList = new ArrayList<Province>();

            SoapObject resp = cs.Call("GetProvince", list);
            SoapObject object = (SoapObject) resp.getProperty("ProvinceList");

            for (int i = 0; i < object.getPropertyCount(); i++) {
                result = new Province((SoapObject) object.getProperty(i));
                provinceList.add(result);
            }
            return provinceList;

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;
        }
    }

    public String GetBillToken(String userID, String token, long mobile, long payID, long billID) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        String _response = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);
        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);
        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("mobile");
        pi3.setValue(mobile);
        pi3.setType(String.class);
        list.add(pi3);
        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("payID");
        pi4.setValue(payID);
        pi4.setType(String.class);
        list.add(pi4);
        PropertyInfo pi5 = new PropertyInfo();
        pi5.setName("billID");
        pi5.setValue(billID);
        pi5.setType(String.class);
        list.add(pi5);

        try {
            SoapPrimitive resp = cs.CallPrim("GetBillToken", list);

            return resp.toString();

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;
        }
    }

    public String BillSuccess(String userID, String token, String data, String orderID, String amount) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        String result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("data");
        pi3.setValue(data);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("orderID");
        pi4.setValue(orderID);
        pi4.setType(String.class);
        list.add(pi4);

        PropertyInfo pi5 = new PropertyInfo();
        pi5.setName("amount");
        pi5.setValue(amount);
        pi5.setType(String.class);
        list.add(pi5);


        try {

            SoapPrimitive resp = cs.CallPrim("BillSuccess", list);

            return resp.toString();

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public Map GetInternetPeriod(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        Map result = new Hashtable<>();

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        try {

            SoapPrimitive resp = cs.CallPrim("GetInternetPeriod", list);

            String jsonString = resp.toString();
            Gson g = new Gson();
            return g.fromJson(jsonString, Map.class);

            // Assert.assertEquals(2, map.size());
            //  Assert.assertEquals(Double.class, map.get("employee.salary").getClass());

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public boolean checkPresentageCode(String code) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("code");
        pi1.setValue(code);
        pi1.setType(String.class);
        list.add(pi1);

        try {

            SoapPrimitive resp = cs.CallPrim("CheckPresentageCode", list);
//            SoapPrimitive object = (SoapPrimitive) resp.getAttribute("CheckVerificationCodeResponse");

            return resp != null && resp.toString().equals("true");


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return false;
        }

    }

    public ResponseStatus InsertDepositTransaction(String userID, String token, String sheba, String ownerName, String amount) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("sheba");
        pi3.setValue(sheba);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("amount");
        pi4.setValue(amount);
        pi4.setType(String.class);
        list.add(pi4);
        PropertyInfo pi5 = new PropertyInfo();
        pi5.setName("ownerName");
        pi5.setValue(ownerName);
        pi5.setType(String.class);
        list.add(pi5);

        try {

            SoapObject resp = cs.Call("InsertDepositTransaction", list);

            if (resp != null) {

                result = new ResponseStatus(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public Bus getSpecificService(String userID, String token, String busID) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        Bus result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("busID");
        pi3.setValue(busID);
        pi3.setType(String.class);
        list.add(pi3);


        try {

            SoapObject resp = cs.Call("getSpecificService", list);

            if (resp != null) {

                // for (int i = 0; i < resp.getPropertyCount(); i++) {
                result = new Bus(resp);

                //}
            }

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public List<City> GetCities(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        City result = null;
        List<City> cityList = new ArrayList<>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);


        try {

            SoapObject resp = cs.Call("getBusCities", list);

            if (resp != null) {

                for (int i = 0; i < resp.getPropertyCount(); i++) {
                    result = new City((SoapObject) resp.getProperty(i));
                    cityList.add(result);
                }
            }

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return cityList;
    }

    public List<BusSummary> getAvailableServices(String userID, String token, String from, String to, String date) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        BusSummary result = null;
        List<BusSummary> busList = new ArrayList<>();
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("from");
        pi3.setValue(from);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("to");
        pi4.setValue(to);
        pi4.setType(String.class);
        list.add(pi4);

        PropertyInfo pi5 = new PropertyInfo();
        pi5.setName("date");
        pi5.setValue(date);
        pi5.setType(String.class);
        list.add(pi5);


        try {

            SoapObject resp = cs.Call("getAvailableServices", list);

            if (resp != null) {

                for (int i = 0; i < resp.getPropertyCount(); i++) {
                    result = new BusSummary((SoapObject) resp.getProperty(i));
                    busList.add(result);
                }
            }

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return busList;
    }

    public ArrayList<ContactList> getAllRegisteredContact(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ContactList result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        try {
            ArrayList<ContactList> contacts = new ArrayList<ContactList>();
            SoapObject resp = cs.Call("getAllRegisteredContact", list);
            SoapObject object = (SoapObject) resp.getProperty("ContactList");

            for (int i = 0; i < object.getPropertyCount(); i++) {
                result = new ContactList((SoapObject) object.getProperty(i));
                contacts.add(result);
            }
            return contacts;


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;

        }
    }

    public ResponseStatus TransferCredit(String userID, String token, String phone, String amount) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("phone");
        pi3.setValue(phone);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("amount");
        pi4.setValue(amount);
        pi4.setType(String.class);
        list.add(pi4);

        try {

            SoapObject resp = cs.Call("TransferCredit", list);

            if (resp != null) {

                result = new ResponseStatus(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public List<User> GetUserByPresentageCode(String userID, String token, String code) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        List<User> result = new ArrayList<>();

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);
        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("code");
        pi3.setValue(code);
        pi3.setType(String.class);
        list.add(pi3);


        try {

            SoapObject resp = cs.Call("GetUserByPresentageCode", list);
            SoapObject object = (SoapObject) resp.getProperty("UserList");
            for (int i = 0; i < object.getPropertyCount(); i++) {
                SoapObject mainObject = (SoapObject) object.getProperty(i);
                result.add(new User(mainObject));
            }


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public String GetProfileImageByUserName(String userName) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        String _response = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userName");
        pi1.setValue(userName);
        pi1.setType(String.class);
        list.add(pi1);

        try {


            SoapObject resp = cs.Call("GetProfileImageByUserName", list);

            return resp.toString();

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;
        }
    }

    public Map<String, String> getAirportCities(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        Map<String, String> result = new Hashtable<>();

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);


        try {

            SoapPrimitive resp = cs.CallPrim("GetAirportCities", list);
            Map<String, String> map = new Gson().fromJson(resp.toString(), new TypeToken<HashMap<String, String>>() {
            }.getType());
            return map;

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public Fund GetFundReport(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        Fund result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);


        try {

            SoapObject resp = cs.Call("GetFundReport", list);
            if (resp != null) {
                result = new Fund(resp);
            }

        } catch (
                Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ArrayList<Fund> GetFundHistory(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ArrayList<Fund> result = new ArrayList<>();

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);


        try {

            SoapObject resp = cs.Call("GetFundHistory", list);
            SoapObject object = (SoapObject) resp.getProperty("fundList");
            for (int i = 0; i < object.getPropertyCount(); i++) {
                SoapObject mainObject = (SoapObject) object.getProperty(i);
                result.add(new Fund(mainObject));
            }


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public TokenResponse checkToken(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        TokenResponse result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);


        try {

            SoapObject resp = cs.Call("CheckToken", list);

            if (resp != null) {
                result = new TokenResponse(resp);
            }

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public List<User> GetWinnersByDate(String userID, String token, String date) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        List<User> result = new ArrayList<>();

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("date");
        pi3.setValue(date);
        pi3.setType(String.class);
        list.add(pi3);


        try {

            SoapObject resp = cs.Call("Get_Winners_ByDate", list);
            SoapObject object = (SoapObject) resp.getProperty("UserList");
            for (int i = 0; i < object.getPropertyCount(); i++) {
                SoapObject mainObject = (SoapObject) object.getProperty(i);
                result.add(new User(mainObject));
            }


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ResponseStatus seenAllNotifications(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);


        try {

            SoapObject resp = cs.Call("SeenAllNotifications", list);

            if (resp != null) {

                result = new ResponseStatus(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ResponseStatus seenAllMessages(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        try {

            SoapObject resp = cs.Call("SeenAllMessages", list);

            if (resp != null) {
                result = new ResponseStatus(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public GetAirplaneTicketList GetAvailableToken(String userID, String token, String request) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        GetAirplaneTicketList result = new GetAirplaneTicketList();

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("request");
        pi3.setValue(request);
        pi3.setType(String.class);
        list.add(pi3);


        try {

            SoapPrimitive resp = cs.CallPrim("GetAvailableToken", list);
            Gson g = new Gson();
            result = g.fromJson(resp.toString(), GetAirplaneTicketList.class);
            // result = new GetAirplaneTicketList(resp);


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public List<Airline> getAirline(String userID, String token, String request) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        List<Airline> result = new ArrayList<>();

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);
        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("request");
        pi3.setValue(request);
        pi3.setType(String.class);
        list.add(pi3);

        try {
            SoapObject resp = cs.Call("getAirline", list);
            SoapObject obj = (SoapObject) ((SoapObject) ((SoapObject) resp.getProperty(0)).getProperty(0)).getProperty("data");
            for (int i = 0; i < obj.getPropertyCount(); i++) {
                result.add(new Airline((SoapObject) obj.getProperty(i)));
            }

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public String airplaneTicketRequestForApp(String userID, String token, String tiketToBook, long amount) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        String result = "";

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);
        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("tiketToBook");
        pi3.setValue(tiketToBook);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("amount");
        pi4.setValue(amount);
        pi4.setType(Long.class);
        list.add(pi4);

        try {

            SoapPrimitive resp = cs.CallPrim("airplaneTicketRequestForApp", list);
            return resp.toString();
            // Gson g= new Gson();
            //  result=g.fromJson(resp.toString(),AirReservation.class);
            // result = new GetAirplaneTicketList(resp);


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public WalletCredit airplaneTicketSuccess(String userID, String token, String data, String orderID, String amount) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        WalletCredit result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("data");
        pi3.setValue(data);
        pi3.setType(String.class);
        list.add(pi3);

        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("orderID");
        pi4.setValue(orderID);
        pi4.setType(Long.class);
        list.add(pi4);

        PropertyInfo pi5 = new PropertyInfo();
        pi5.setName("amount");
        pi5.setValue(amount);
        pi5.setType(Double.class);
        list.add(pi5);

        try {

            SoapObject resp = cs.Call("airplaneTicketSuccess", list);
            result = new WalletCredit(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public String airplainTicketRequestFromWallet(String userID, String token, String request, long amount) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        String result = "";

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);
        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("tiketToBook");
        pi3.setValue(request);
        pi3.setType(String.class);
        list.add(pi3);
        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("amount");
        pi4.setValue(amount);
        pi4.setType(String.class);
        list.add(pi4);


        try {

            SoapPrimitive resp = cs.CallPrim("airplainTicketRequestFromWallet", list);
            return resp.toString();
            // Gson g= new Gson();
            //  result=g.fromJson(resp.toString(),AirReservation.class);
            // result = new GetAirplaneTicketList(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public RepeatPurchase getRepeatPurchase(String userID, String token, String type) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        RepeatPurchase result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("type");
        pi3.setValue(type);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("GetRepeatPurchase", list);
            result = new RepeatPurchase(resp);

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ResponseStatus insertRepeatPurchase(String userID, String token, String info) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("info");
        pi3.setValue(info);
        pi3.setType(String.class);
        list.add(pi3);

        try {

            SoapObject resp = cs.Call("InsertRepeatPurchase", list);

            if (resp != null) {

                result = new ResponseStatus(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public String getChance(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        String result = "";

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);


        try {

            SoapPrimitive resp = cs.CallPrim("GetChance", list);
            if (resp != null)
                return resp.toString();
            else
                return "";
            // Gson g= new Gson();
            //  result=g.fromJson(resp.toString(),AirReservation.class);
            // result = new GetAirplaneTicketList(resp);


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

    public ArrayList<Chances> getUserChances(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        Chances result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        try {
            ArrayList<Chances> chances = new ArrayList<Chances>();
            SoapObject resp = cs.Call("Get_User_Chances", list);
            SoapObject object = (SoapObject) resp.getProperty("ChanceList");

            for (int i = 0; i < object.getPropertyCount(); i++) {
                result = new Chances((SoapObject) object.getProperty(i));
                chances.add(result);
            }
            return chances;


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;

        }
    }

    public ArrayList<User> agentUserReport(String userID, String token) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        User result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("userID");
        pi1.setValue(userID);
        pi1.setType(String.class);
        list.add(pi1);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("token");
        pi2.setValue(token);
        pi2.setType(String.class);
        list.add(pi2);

        try {
            ArrayList<User> users = new ArrayList<User>();
            SoapObject resp = cs.Call("AgentsUserReport", list);
            SoapObject object = (SoapObject) resp.getProperty("UserList");

            for (int i = 0; i < object.getPropertyCount(); i++) {
                result = new User((SoapObject) object.getProperty(i));
                users.add(result);
            }
            return users;


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;

        }
    }

    public ResponseStatus userIdByUsername(String phone) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("phone");
        pi1.setValue(phone);
        pi1.setType(String.class);
        list.add(pi1);

        try {
            SoapObject resp = cs.Call("GetUSerIDByUserName", list);
            result = new ResponseStatus(resp);

            return result;

        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return null;

        }
    }


}