# Phonegap-SMS

## PhoneGap plugin to send and receive sms

### Supported Features

- Send SMS
- Get SIM phone number
- Get MCC+MNC
- Check SMS feature availability
- Start Receiving SMSs
- Stop Receiving SMSs

### Supported Platforms

- Android

### Usage

#### Installation

    phonegap plugin add https://github.com/pyriel4you/Cordova-SMS.git
    
__or__
    
    cordova plugin add https://github.com/pyriel4you/Cordova-SMS.git
	
#### Require the plugin module

	var smsplugin = cordova.require("ro.telenes.cordova.smsplugin.smsplugin");
    
#### Methods

__send__

	smsplugin.send(number,message,successCallback(result),failureCallback(error));

__isSupported__

	smsplugin.isSupported(successCallback(result),failureCallback(error));

__getNumber__

	smsplugin.getNumber(successCallback(result),failureCallback(error));

__getMnc__

	smsplugin.getMnc(successCallback(result),failureCallback(error));
	
__startReception__

	smsplugin.startReception(successCallback(result),failureCallback(error));

__stopReception__
	
	smsplugin.stopReception(successCallback(result),failureCallback(error));



