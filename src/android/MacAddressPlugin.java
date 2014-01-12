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
        //test ip base creation:
        //ip = ip.substring(11, ip.length());
        
        
        macAddress = ip.substring(0,10);
        
       // ArrayList<String> devices = getConnectedIps();
        
       //testing loop This works, but is slow and produces few results..:
        checkHosts("192.168.1");
        
        

        return macAddress;
    }

      public String intToIp(int i) {

           return ( i & 0xFF)  + "." +
                   ((i >> 8 ) & 0xFF) + "." +
                   ((i >> 16 ) & 0xFF) + "." +
                   ((i >> 24 ) & 0xFF ) ;
           
        }


      
        
        //test loop:
            
      public void checkHosts(String subnet){
        
         int timeout=200;
         
         System.out.println("Starting ip loop for " + subnet);
         
         WifiManager wm = (WifiManager) this.cordova.getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
         
         Integer rawIP = wm.getConnectionInfo().getIpAddress();
           String ip = intToIp(rawIP);
           
         //get node subnet address as int (convert back to string during scan)
           
           int ipCenter = Integer.parseInt(ip.substring(10));
           
           System.out.println("IP Center is: " + (ipCenter));
           
           //RADIUS SCAN Loop, starting with the host devices Ip as the center:
           
         //set the direction to up by default
           String direction = null;
           if(ipCenter<254){direction = "up";}else{direction = "down";}
           
           //use negative int conversion to change direction (so it will go one up then one down, two up then two down etc etc):
           
           int highest = 2; 
           int lowest = 2;
           int actionCount = 0;
           //loop while high is less than 254 and low is greater than 1:
           System.out.println(direction);
           
           for(int i=1; highest <254 || lowest >1; ){
             
             int iptoScan = 0;
             
             //set the direction of i:
             if(direction == "up"){iptoScan = ipCenter + i;}else{iptoScan = ipCenter + (i*-1);}
             
             
             //do the thing:
             System.out.println(i);
             System.out.println("Radial Scan: " + subnet + "." + iptoScan);
             
             
             //switch up/down AND set the highest and lowest counters - only if there is room to go up or down:
             
             if(lowest<1 || highest>254){
            
               if(lowest<1){
                 direction = "up";
                 highest = ipCenter + i;
               //increment action count by two to skip repeated process (no longer need to use i twice as we are only going in one direction!)
                     actionCount = actionCount +2;
               }
               
               if(highest>254){
                 
                 direction ="down";
                 lowest = ipCenter + (i*-1);
               //increment action count by two to skip repeated process
                     actionCount = actionCount +2;
               }
               
             }else{
               
               //switch up/down AND set the highest and lowest counters
               if(direction == "up"){direction = "down";highest = ipCenter + i;}else{direction ="up";lowest=ipCenter + (i*-1);}   
             //count actions per digit increment (to ensure up +x and down -x are both done before x is incremented)
                 actionCount ++;
             }
            
                   
             //increment if this has been done twice:
             if(actionCount> 1){
              i++;
              //reset the action count:
              actionCount = 0;
             }
             
             
           }
           
           
           
           
//         for (int i=1;i<254;i++){
//           
//             String host=subnet + "." + i;
//             System.out.println("Trying: " + host );
//             try {
//        if (InetAddress.getByName(host).isReachable(timeout)){
//               System.out.println(host + " is reachable and is called: " + InetAddress.getByName(host).getHostName());
//               
//               
//           }
//      } catch (UnknownHostException e) {
//        System.out.println("Unknown Host Error");
//        // TODO Auto-generated catch block
//        //e.printStackTrace();
//      } catch (IOException e) {
//        System.out.println("IO Exception Error");
//        // TODO Auto-generated catch block
//        //e.printStackTrace();
//      }
//         }
         
         
         
         
         
      }
      
      
      
        //end test loop.
      
          public ArrayList<String> getConnectedIps(){
        
        
            //get the IP Address:
            
            String fullIP = getMacAddress();
            
            String baseIP = fullIP.substring(0,9);
                    
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
