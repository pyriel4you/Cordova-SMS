var argscheck = require('cordova/argscheck'),
    exec = require('cordova/exec');

var smsplugin = {
	send:function (phoneNumber, message, method, successCallback, failureCallback) {    
		return exec(successCallback, failureCallback, 'SmsPlugin', 'SEND_SMS', [phoneNumber, message, method]);
	},
	//Check if the device has a possibility to send and receive SMS
	isSupported:function(successCallback,failureCallback) {
		return exec(successCallback, failureCallback, 'SmsPlugin', 'HAS_SMS_POSSIBILITY', []);
	},
	//Get SIM phone number
	getNumber:function(successCallback,failureCallback) {
		return exec(successCallback, failureCallback, 'SmsPlugin', 'GET_NUMBER', []);
	},
	//Get MCC+MNC
	getMnc:function(successCallback,failureCallback) {
		return exec(successCallback, failureCallback, 'SmsPlugin', 'GET_MNC', []);
	},
	//Start receiving sms, and the successCallback function receives one string as parameter formatted such as [phonenumber]>[message]
	startReception:function(successCallback,failureCallback) {
		return exec(successCallback, failureCallback, 'SmsPlugin', 'RECEIVE_SMS', []);
	},
	//Stop receiving sms
	stopReception:function(successCallback,failureCallback) {
		return exec(successCallback, failureCallback, 'SmsPlugin', 'STOP_RECEIVE_SMS', []);
	}
};

module.exports=smsplugin;
