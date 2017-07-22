'use strict';

var exec = require('cordova/exec');

var sms = {};

sms.send = function (phoneNumber, message, method, successCallback, failureCallback) {    
	return exec(successCallback, failureCallback, 'SmsPlugin', 'SEND_SMS', [phoneNumber, message, method]);
};
	//Check if the device has a possibility to send and receive SMS
sms.isSupported = function(successCallback,failureCallback) {
	return exec(successCallback, failureCallback, 'SmsPlugin', 'HAS_SMS_POSSIBILITY', []);
};
	//Get SIM phone number
sms.getNumber = function(successCallback,failureCallback) {
	return exec(successCallback, failureCallback, 'SmsPlugin', 'GET_NUMBER', []);
};
	//Get MCC+MNC
sms.getMnc = function(successCallback,failureCallback) {
	return exec(successCallback, failureCallback, 'SmsPlugin', 'GET_MNC', []);
};
	//Start receiving sms, and the successCallback function receives one string as parameter formatted such as [phonenumber]>[message]
sms.startReception = function(successCallback,failureCallback) {
	return exec(successCallback, failureCallback, 'SmsPlugin', 'RECEIVE_SMS', []);
};
	//Stop receiving sms
sms.stopReception:function(successCallback,failureCallback) {
	return exec(successCallback, failureCallback, 'SmsPlugin', 'STOP_RECEIVE_SMS', []);
};

module.exports=sms;
