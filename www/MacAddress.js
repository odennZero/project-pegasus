/*
 * MacAddress
 * Implements the javascript access to the cordova plugin for retrieving the device mac address. Returns 0 if not running on Android
 * @author Olivier Brand
 */

/**
 * @return the mac address class instance
 */
 var MacAddress = {

 	getMacAddress: function(successCallback, failureCallback,deviceLog){
 		cordova.exec(successCallback, failureCallback, 'MacAddress',
 			'getMacAddress', [deviceLog]);
 	}
 };

 module.exports = MacAddress;