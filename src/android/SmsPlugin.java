package ro.telenes.cordova.smsplugin;

import android.Manifest;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.wirget.Toast;
import android.telephony.TelephonyManager;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

public class SmsPlugin extends CordovaPlugin {
    public enum ActionType{
        SEND_SMS,GET_NUMBER,GET_MNC,HAS_SMS_POSSIBILITY,RECEIVE_SMS,STOP_RECEIVE_SMS;
    }
    private SmsSender smsSender;
    private SmsReceiver smsReceiver;

    private CallbackContext callback_receive;

    private TelephonyManager telephonyManager;

    private boolean isReceiving = false;
    private boolean result=false;

    private PluginResult pluginResult;
	
	@Override
    	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// In an actual app, you'd want to request a permission when the user performs an action
		// that requires that permission.
		getPermissionToSendSMS();
		getPermissionToReceiveSMS();
    	}
	
	// Identifier for the permission request
    	private static final int SEND_SMS_PERMISSIONS_REQUEST = 1;
	private static final int RECEIVE_SMS_PERMISSIONS_REQUEST = 1;
	
	public void getPermissionToSendSMS() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
			!= PackageManager.PERMISSION_GRANTED) {
		    if (shouldShowRequestPermissionRationale(
			    Manifest.permission.SEND_SMS)) {
		    }
		    requestPermissions(new String[]{Manifest.permission.SEND_SMS},
			    SEND_SMS_PERMISSIONS_REQUEST);
		}
	    }
	public void getPermissionToReceiveSMS() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
			!= PackageManager.PERMISSION_GRANTED) {
		    if (shouldShowRequestPermissionRationale(
			    Manifest.permission.RECEIVE_SMS)) {
		    }
		    requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS},
			    RECEIVE_SMS_PERMISSIONS_REQUEST);
		}
	    }

	    // Callback with the request from calling requestPermissions(...)
	    @Override
	    public void onRequestPermissionsResult(int requestCode,
						   @NonNull String permissions[],
						   @NonNull int[] grantResults) {
		// Make sure it's our original READ_CONTACTS request
		if (requestCode == SEND_SMS_PERMISSIONS_REQUEST) {
		    if (grantResults.length == 1 &&
			    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			Toast.makeText(this, "Send SMS permission granted", Toast.LENGTH_SHORT).show();
		    } else {
			// showRationale = false if user clicks Never Ask Again, otherwise true
			boolean showRationale = shouldShowRequestPermissionRationale( this, Manifest.permission.SEND_SMS);

			if (showRationale) {
			   // do something here to handle degraded mode
			} else {
			   Toast.makeText(this, "Send SMS permission denied", Toast.LENGTH_SHORT).show();
			}
		    }
		} else if (requestCode == RECEIVE_SMS_PERMISSIONS_REQUEST) {
		    if (grantResults.length == 1 &&
			    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			Toast.makeText(this, "Receive SMS permission granted", Toast.LENGTH_SHORT).show();
		    } else {
			// showRationale = false if user clicks Never Ask Again, otherwise true
			boolean showRationale = shouldShowRequestPermissionRationale( this, Manifest.permission.RECEIVE_SMS);

			if (showRationale) {
			   // do something here to handle degraded mode
			} else {
			   Toast.makeText(this, "Receive SMS permission denied", Toast.LENGTH_SHORT).show();
			}
		    }
		} else {
		    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	    }
	
	
	@Override
	public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        action=action.toUpperCase();
	
        switch(ActionType.valueOf(action)){
            case SEND_SMS:
                try {
                    String phoneNumber = args.getString(0);
                    String message = args.getString(1);
                    String method = args.getString(2);
                    smsSender = new SmsSender(this.cordova.getActivity());
                    if(method.equalsIgnoreCase("INTENT")){
                        smsSender.invokeSMSIntent(phoneNumber, message);
                        callbackContext.sendPluginResult(new PluginResult( PluginResult.Status.NO_RESULT));
                    } else{
                        smsSender.sendSMS(phoneNumber, message);
                    }

                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
                    result = true;
                }
                catch (JSONException ex) {
                    callbackContext.sendPluginResult(new PluginResult( PluginResult.Status.JSON_EXCEPTION));
                }
                break;
            case HAS_SMS_POSSIBILITY:
                Activity ctx = this.cordova.getActivity();
                if(ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)){
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, true));
                } else {
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, false));
                }
                result=true;
                break;
            case GET_NUMBER:
                    telephonyManager = (TelephonyManager) this.cordova.getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                    // Check phone data
                    if (telephonyManager == null) {
                        String message = "You didnt add an permission android.permission.READ_PHONE_STATE";
                        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, message));
                    }

                    // Check device GSM radio
                    if (telephonyManager.getSubscriberId() == null) {
                        String message = "Payment cant be done with no SIM card";
                        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, message));
                    }

                    // Check SIM state
                    if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_READY) {
                        String message = "SIM card is not ready";
                        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, message));
                    }

                    String phoneNumber = telephonyManager.getLine1Number();
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, phoneNumber));
                    result = true;
                break;
            case GET_MNC:
                    telephonyManager = (TelephonyManager) this.cordova.getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                    String mccmnc = telephonyManager.getSimOperator();
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, mccmnc));
                    result = true;
                break;
            case RECEIVE_SMS:
                // if already receiving (this case can happen if the startReception is called
                // several times
                if(this.isReceiving) {
                    // close the already opened callback ...
                    pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
                    pluginResult.setKeepCallback(false);
                    this.callback_receive.sendPluginResult(pluginResult);

                    // ... before registering a new one to the sms receiver
                }
                this.isReceiving = true;

                if(this.smsReceiver == null) {
                    this.smsReceiver = new SmsReceiver();
                    IntentFilter fp = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
                    fp.setPriority(1000);
                    // fp.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
                    this.cordova.getActivity().registerReceiver(this.smsReceiver, fp);
                }

                this.smsReceiver.startReceiving(callbackContext);

                pluginResult = new PluginResult(
                        PluginResult.Status.NO_RESULT);
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
                this.callback_receive = callbackContext;

                result=true;
                break;
            case STOP_RECEIVE_SMS:

                if(this.smsReceiver != null) {
                    smsReceiver.stopReceiving();
                }

                this.isReceiving = false;

                // 1. Stop the receiving context
                pluginResult = new PluginResult(
                        PluginResult.Status.NO_RESULT);
                pluginResult.setKeepCallback(false);
                this.callback_receive.sendPluginResult(pluginResult);

                // 2. Send result for the current context
                pluginResult = new PluginResult(
                        PluginResult.Status.OK);
                callbackContext.sendPluginResult(pluginResult);

                result=true;
                break;
            default:
                result=false;
        }
        return result;
	}
}
