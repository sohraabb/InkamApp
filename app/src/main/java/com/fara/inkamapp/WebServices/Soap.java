package com.fara.inkamapp.WebServices;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

public class Soap {

    private String _soapAction;
    private String _operationName;
    private String _WSDL_Target_NameSpace;
    private String _SOAP_ADDRESS;
    // public Context _soapContext;
    String internetConnection;
    public static String _SOAPExceptionMessage;

    public Soap(String WSDL_Target_NameSpace, String SOAP_Address) {

        _WSDL_Target_NameSpace = WSDL_Target_NameSpace;
        _SOAP_ADDRESS = SOAP_Address;
        // _soapContext = SOAP_Context;
    }

    public SoapObject Call(String OperationName, List<PropertyInfo> properties) {
        _operationName = OperationName;
        _soapAction = _WSDL_Target_NameSpace + _operationName;

        SoapObject request = new SoapObject(_WSDL_Target_NameSpace,
                _operationName);

        for (int i = 0; i < properties.size(); ++i)
            request.addProperty(properties.get(i));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // envelope.encodingStyle = "utf-8";
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(_SOAP_ADDRESS);
        SoapObject response2;

        try {

            httpTransport.call(_soapAction, envelope);
            Log.i("SOAP", "CALLERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR11");
            response2 = (SoapObject) envelope.getResponse();

            Log.i("SOAP", "CALLERRRRRRRR22");
            return response2;

        } catch (Exception exception) {

            if (exception != null) {
                _SOAPExceptionMessage = exception.toString();

            }
            Log.i("SOAP", exception.toString());
            return null;
        }

    }

    public SoapPrimitive CallPrim(String OperationName,
                                  List<PropertyInfo> properties) {// inja
        _SOAPExceptionMessage = "";
        _operationName = OperationName;
        _soapAction = _WSDL_Target_NameSpace + _operationName;

        SoapObject request = new SoapObject(_WSDL_Target_NameSpace,
                _operationName);

        for (int i = 0; i < properties.size(); ++i)
            request.addProperty(properties.get(i));

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // envelope.encodingStyle = "utf-8";
        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(_SOAP_ADDRESS);
        SoapPrimitive response2;
        try {

            httpTransport.call(_soapAction, envelope);
            Log.i("SOAP", "CALLERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR11");
            response2 = (SoapPrimitive) envelope.getResponse();

            Log.i("SOAP", "CALLERRRRRRRR22");
            return response2;

        } catch (Exception exception) {
            if (exception != null) {

                _SOAPExceptionMessage = exception.toString();

            }

            Log.i("SandP SOAP", exception.toString());
            return null;
        }

    }

}
