package ma.xiaoshuai.cordova.wswUpdate;

import android.annotation.SuppressLint;
import android.util.Log;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */

public class wswUpdate extends CordovaPlugin {
  protected static CallbackContext currentCallbackContext;
  public static final String TAG = "Cordova.Plugin.wswUpdate";
  public static final String ERROR_INVALID_PARAMETERS = "更新失败";

  CallbackContext callbackContext;
  @Override
  public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
    if (action.equals("wswUpdate")) {
      return wswUpdate(args, callbackContext);
    }

    return false;
  }

  @SuppressLint("wswUpdate")
  protected boolean wswUpdate(CordovaArgs args, CallbackContext callbackContext) {
    JSONObject params = null;
    currentCallbackContext = callbackContext;
    try {
      params = args.getJSONObject(0);
      String sign = params.getString("url");
      Log.i(TAG, "aliLogin request has been sent successfully.");

      return true;

    } catch (JSONException e) {
      e.printStackTrace();
      callbackContext.error(ERROR_INVALID_PARAMETERS);
      return  false;
    }
  }


}
