package com.fara.inkamapp.WebServices;

import android.content.Context;
import android.util.Log;

import com.fara.inkamapp.Models.CheckUsername;
import com.fara.inkamapp.Models.ResponseStatus;
import com.fara.inkamapp.Models.User;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

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

        //db = new AppSQLiteHelper(activity.getApplicationContext());
        // String url = "http://mylavan.com/tabletglobalservice.asmx";
        // cs = new SOAP("http://tempuri.org/", url);

        String url = "http://inkam.ir/webservice/webservice.asmx";
        //String url2 = "http://208.73.202.178:100/Service/regUserService.asmx";
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

            SoapObject resp = cs.Call("ChekUserName", list);

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

            SoapObject resp = cs.Call("CheckVerificationCode", list);
            SoapObject object = (SoapObject) resp.getProperty("CheckVerificationCodeResponse");

            return object != null && object.toString().equals("true");


        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());
            return false;
        }

    }

    public User agentLogin(String phone, String password) {
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

    public ResponseStatus updatePassword(String id, String token, String lastPass, String newPassword) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("id");
        pi1.setValue(token);
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

    public ResponseStatus insertUser(String user) {
        List<PropertyInfo> list = new ArrayList<PropertyInfo>();
        ResponseStatus result = null;

        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("user");
        pi1.setValue(user);
        pi1.setType(String.class);
        list.add(pi1);

        try {

            SoapObject resp = cs.Call("InsertUser", list);

            if (resp != null) {

                result = new ResponseStatus(resp);
            }
        } catch (Exception ex) {
            Log.e("Inkam_Caller", ex.toString());

        }
        return result;
    }

}