package com.phonegap.plugins;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * The Class MacAddressPlugin.
 */
public class MacAddressPlugin extends CordovaPlugin {

    public boolean isSynch(String action) {
        if (action.equals("getMacAddress")) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.cordova.api.Plugin#execute(java.lang.String,
     * org.json.JSONArray, java.lang.String)
     */
    @Override
    public boolean execute(String action, JSONArray args,
            CallbackContext callbackContext) {

        if (action.equals("getMacAddress")) {

            String macAddress = this.getMacAddress();

            if (macAddress != null) {
                JSONObject JSONresult = new JSONObject();
                try {
                    JSONresult.put("mac", macAddress);
                    PluginResult r = new PluginResult(PluginResult.Status.OK,
                            JSONresult);
                    callbackContext.success(macAddress);
                    r.setKeepCallback(true);
                    callbackContext.sendPluginResult(r);
                    return true;
                } catch (JSONException jsonEx) {
                    PluginResult r = new PluginResult(
                            PluginResult.Status.JSON_EXCEPTION);
                    callbackContext.error("error");
                    r.setKeepCallback(true);
                    callbackContext.sendPluginResult(r);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the mac address.
     * 
     * @return the mac address
     */
    private String getMacAddress() {
        String macAddress = null;
        WifiManager wm = (WifiManager) this.cordova.getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        macAddress = wm.getConnectionInfo().getMacAddress();

        if (macAddress == null || macAddress.length() == 0) {
            macAddress = "No Mac Address Found!";
        }

        Integer rawIP = wm.getConnectionInfo().getIpAddress();

        String ip = intToIp(rawIP);
        
        macAddress = ip;

        return macAddress;
    }

      public String intToIp(int i) {

           return ( i & 0xFF)  + "." +
                   ((i >> 8 ) & 0xFF) + "." +
                   ((i >> 16 ) & 0xFF) + "." +
                   ((i >> 24 ) & 0xFF ) ;
           
        }


          public ArrayList<String> getConnectedIps(String baseIP){
        
        
        
             //function to get an array of all connected IP's on the subnet:
                     
              ArrayList<String> hosts = new ArrayList<String>();
              
                //subnet loop:
              int timeout=1000;
                 
             for (int i=1;i<254;i++){

                  String host=baseIP + "." + i;

                try {
                  if (InetAddress.getByName(host).isReachable(timeout)){
                         System.out.println(host + " is reachable");
                     }
                } catch (UnknownHostException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }

            }
              
              
              
              return hosts;
        
        
       }
}
