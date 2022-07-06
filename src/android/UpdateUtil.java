package ma.xiaoshuai.cordova.wswUpdate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.lzy.okgo.BuildConfig;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.util.List;



public class UpdateUtil {
    private static ProgressBar pb;
    private static TextView tvStrPro;
    private static ProgressDialog progressDialog;
    private static Context mContext;

    /**
     * @description 下载  入参apk的url下载路径
     */

    public static void downloadApk(String url, Context context, String pageName) {
        initUpdatePop(context);
        //APK的下载目录，如无特定需求不用改
        mContext = context;
        final String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download";
        //下载的APK名，可自行修改
        final String fileName = "app-release.apk";
        //如无下载目录则创建目录
        File file = new File(filePath);
        if (!file.exists())
            file.mkdirs();

        OkGo.<File>get(url)
                .tag(context.getApplicationContext())
                .execute(new FileCallback(filePath, fileName) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        Log.e("success_update", response.message());

                    }

                    @Override
                    @SuppressLint("SetTextI18n")
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        //下载进度回调方法(此回调在主线程,可更新ui)
                        //布局文件添加ProgressBar  显示并将进度回调传递给进度条
//          pb.setProgress((int) (progress.fraction * 100));
//          tvStrPro.setText(progress.currentSize/1024/1024+"mb/"+progress.totalSize/1024/1024+"mb");
                        progressDialog.setProgress((int) (progress.fraction * 100));
                        //当前进度等于总进度，说明下载完成，隐藏进度条，安装apk
                        if (progress.currentSize == progress.totalSize) {
                            progressDialog.cancel();
                            installApk(pageName);
                        }


                    }

                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);
                        //updatePop.showAtLocation(view, Gravity.CENTER, 0, 0);
                        showLoading();
                    }
                });
    }

    private static void installApk(String pageName) {
        //参2：要安装的apk路径+apk名，与上方下载路径和apk名统一
        File apkFile = new File(Environment.getExternalStorageDirectory(), "/download/app-release.apk");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type = "application/vnd.android.package-archive";
        Uri uri;
        //兼容安卓7以下
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mContext, pageName + ".MyFileProvider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(uri, type);

        mContext.startActivity(intent);
    }

    //更新pop
    public static void initUpdatePop(Context context) {
//    updatePop = EasyPopup.create()
//      .setContext(context)
//      .setContentView(R.layout.pop_update)
//      .setWidth(600)
//      .setHeight(WindowManager.LayoutParams.WRAP_CONTENT)
//      .setOutsideTouchable(false)
//      .setBackgroundDimEnable(true)
//      .setFocusAndOutsideEnable(false)
//      .setDimValue(0.5f)
//      .setDimColor(Color.BLACK)
//      .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
//      .apply();
//
//    pb = updatePop.findViewById(R.id.pb_speed);
//    tvStrPro = updatePop.findViewById(R.id.tv_pro_str);
//    updatePop.setOnDismissListener(() -> AppManager.getAppManager().AppExit(MyApplication.getContext()));
    }

    //显示loading
    public static void showLoading() {
        final int MAX_VALUE = 100;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setProgress(0);
        progressDialog.setTitle("下载中");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(MAX_VALUE);
        progressDialog.show();
//    new Thread(new Runnable() {
//      @Override
//      public void run() {
//        int progress = 0;
//        while (progress < MAX_VALUE) {
//          try {
//            Thread.sleep(100);
//            progress++;
//            progressDialog.setProgress(progress);
//          } catch (InterruptedException e) {
//            e.printStackTrace();
//          }
//        }
//        //加载完毕自动关闭dialog
//        progressDialog.cancel();
//      }
//    }).start();

    }
}
