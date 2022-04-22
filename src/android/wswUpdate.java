package ma.xiaoshuai.cordova.wswUpdate;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;

import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * This class echoes a string called from JavaScript.
 */

public class wswUpdate extends CordovaPlugin {
    protected static CallbackContext currentCallbackContext;
    public static final String TAG = "Cordova.Plugin.wswUpdate";
    public static final String ERROR_INVALID_PARAMETERS = "更新失败";
    private boolean isInit;
    CallbackContext callbackContext;

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("wswUpdate")) {
            return wswUpdate(args, callbackContext);
        }

        return false;
    }

    @SuppressLint({"wswUpdate", "LongLogTag"})
    protected boolean wswUpdate(CordovaArgs args, CallbackContext callbackContext) {
        JSONObject params = null;
        currentCallbackContext = callbackContext;
        try {
            params = args.getJSONObject(0);
//            String url = params.getString("url");
//            String packge = params.getString("packge");
            String packgeName = "io.ionic.starter";
            String url = "https://manquan-prod.oss-cn-beijing.aliyuncs.com/apk/app-release.apk";
            Log.i(TAG, "aliLogin request has been sent successfully.");
            if (XXPermissions.isGranted(cordova.getContext(), Permission.MANAGE_EXTERNAL_STORAGE)) {
                UpdateUtil.downloadApk(url, cordova.getContext(),packgeName);
            } else {
                XXPermissions.with(cordova.getContext())
                        // 不适配 Android 11 可以这样写
                        //.permission(Permission.Group.STORAGE)
                        // 适配 Android 11 需要这样写，这里无需再写 Permission.Group.STORAGE
                        .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                        .request(new OnPermissionCallback() {
                            @Override
                            public void onGranted(List<String> permissions, boolean all) {
                                if (all) {
                                    UpdateUtil.downloadApk(url, cordova.getActivity(),packgeName);
                                }
                            }

                            @Override
                            public void onDenied(List<String> permissions, boolean never) {
                                if (never) {
//                ToastUtils.show("被永久拒绝授权，请手动授予存储权限");
                                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
//                XXPermissions.startPermissionActivity(SplashActivity.this, permissions);
                                } else {
//                ToastUtils.show("获取存储权限失败");
                                }
                            }
                        });
            }
            return true;

        } catch (JSONException e) {
            e.printStackTrace();
            callbackContext.error(ERROR_INVALID_PARAMETERS);
            return false;
        }
    }
}
