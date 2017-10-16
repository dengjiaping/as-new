package com.vector.update_app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.vector.update_app.service.DownloadService;
import com.vector.update_app.utils.AppUpdateUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 版本更新管理器
 */
public class UpdateAppManager {
    final static String INTENT_KEY = "update_dialog_values";
    private static final String TAG = UpdateAppManager.class.getSimpleName();
    private Map<String, String> mParams;
    private Activity mActivity;
    private HttpManager mHttpManager;
    private String mUpdateUrl;
    private UpdateAppBean mUpdateApp;
    private String mTargetPath;
    private boolean isPost;
    private boolean mHideDialog;
    private boolean mShowIgnoreVersion;
    private boolean mDismissNotificationProgress;
    private boolean mOnlyWifi;
    //自定义参数

    private UpdateAppManager(Builder builder) {
        mActivity = builder.getActivity();
        mHttpManager = builder.getHttpManager();
        mUpdateUrl = builder.getUpdateUrl();

        mTargetPath = builder.getTargetPath();
        isPost = builder.isPost();
        mParams = builder.getParams();
        mHideDialog = builder.isHideDialog();
        mShowIgnoreVersion = builder.isShowIgnoreVersion();
        mDismissNotificationProgress = builder.isDismissNotificationProgress();
        mOnlyWifi = builder.isOnlyWifi();
    }

    /**
     * 可以直接利用下载功能，
     *
     * @param context          上下文
     * @param updateAppBean    下载信息配置
     * @param downloadCallback 下载回调
     */
    public static void download(final Context context, @NonNull final UpdateAppBean updateAppBean, @Nullable final DownloadService.DownloadCallback downloadCallback) {

        if (updateAppBean == null) {
            throw new NullPointerException("updateApp 不能为空");
        }

        DownloadService.bindService(context.getApplicationContext(), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ((DownloadService.DownloadBinder) service).start(updateAppBean, downloadCallback);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        });
    }

    public Context getContext() {
        return mActivity;
    }


    /**
     * @return 新版本信息
     */
    public UpdateAppBean fillUpdateAppData() {
        if (mUpdateApp != null) {
            mUpdateApp.setTargetPath(mTargetPath);
            mUpdateApp.setHttpManager(mHttpManager);
            mUpdateApp.setHideDialog(mHideDialog);
            mUpdateApp.showIgnoreVersion(mShowIgnoreVersion);
            mUpdateApp.dismissNotificationProgress(mDismissNotificationProgress);
            mUpdateApp.setOnlyWifi(mOnlyWifi);
            return mUpdateApp;
        }

        return null;
    }


    private boolean verify() {
        //版本忽略
        if (mShowIgnoreVersion && AppUpdateUtils.isNeedIgnore(mActivity, mUpdateApp.getNewVersion())) {
            return true;
        }

//        String preSuffix = "/storage/emulated";

        if (TextUtils.isEmpty(mTargetPath)
//                || !mTargetPath.startsWith(preSuffix)
                ) {
            Log.e(TAG, "下载路径错误:" + mTargetPath);
            return true;
        }
        return mUpdateApp == null;
    }

    /**
     * 跳转到更新页面
     */
    public void showDialogFragment() {

        //校验
        if (verify()) return;

        if (mActivity != null && !mActivity.isFinishing()) {
            Bundle bundle = new Bundle();
            //添加信息，
            fillUpdateAppData();
            bundle.putSerializable(INTENT_KEY, mUpdateApp);

            UpdateDialogFragment updateDialogFragment = new UpdateDialogFragment();
            updateDialogFragment.setArguments(bundle);
            updateDialogFragment.show(((FragmentActivity) mActivity).getSupportFragmentManager(), "dialog");
        }

    }

    /**
     * 静默更新
     */
    public void silenceUpdate() {
        checkNewApp(new SilenceUpdateCallback());
    }

    /**
     * 最简方式
     */

    public void update() {
        checkNewApp(new UpdateCallback());
    }

    /**
     * 检测是否有新版本
     *
     * @param callback 更新回调
     */
    public void checkNewApp(final UpdateCallback callback) {
        if (callback == null) {
            return;
        }
        callback.onBefore();

        if (DownloadService.isRunning || UpdateDialogFragment.isShow) {
            callback.onAfter();
            return;
        }

        //拼接参数
        Map<String, String> params = new HashMap<>();

        int versionCode = AppUpdateUtils.getVersionCode(mActivity);
        params.put("versionCode", versionCode + "");


        //添加自定义参数，其实可以实现HttManager中添加
        if (mParams != null && !mParams.isEmpty()) {
            //清空，那就使用自定参数
            params.clear();
            params.putAll(mParams);
        }

        //网络请求
        if (isPost) {
            mHttpManager.asyncPost(mUpdateUrl, params, new HttpManager.Callback() {
                @Override
                public void onResponse(String result) {
                    callback.onAfter();
                    if (result != null) {
                        processData(result, callback);
                    }
                }

                @Override
                public void onError(String error) {
                    callback.onAfter();
                    callback.noNewApp();
                }
            });
        } else {
            mHttpManager.asyncGet(mUpdateUrl, params, new HttpManager.Callback() {
                @Override
                public void onResponse(String result) {
                    callback.onAfter();
                    if (result != null) {
                        processData(result, callback);
                    }
                }

                @Override
                public void onError(String error) {
                    callback.onAfter();
                    callback.noNewApp();
                }
            });
        }
    }

    /**
     * 后台下载
     *
     * @param downloadCallback 后台下载回调
     */
    public void download(@Nullable final DownloadService.DownloadCallback downloadCallback) {
        if (mUpdateApp == null) {
            throw new NullPointerException("updateApp 不能为空");
        }
        mUpdateApp.setTargetPath(mTargetPath);
        mUpdateApp.setHttpManager(mHttpManager);
        DownloadService.bindService(mActivity.getApplicationContext(), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ((DownloadService.DownloadBinder) service).start(mUpdateApp, downloadCallback);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        });
    }

    /**
     * 后台下载
     */
    public void download() {
        download(null);
    }

    /**
     * 解析
     *
     * @param result
     * @param callback
     */
    private void processData(String result, @NonNull UpdateCallback callback) {
        try {
            mUpdateApp = callback.parseJson(result);
            if (mUpdateApp.isUpdate()) {
                callback.hasNewApp(mUpdateApp, this);
                //假如是静默下载，可能需要判断，
                //是否wifi,
                //是否已经下载，如果已经下载直接提示安装
                //没有则进行下载，监听下载完成，弹出安装对话框

            } else {
                callback.noNewApp();
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
            callback.noNewApp();
        }
    }

    public static class Builder {
        //必须有
        private Activity mActivity;
        //必须有
        private HttpManager mHttpManager;
        //必须有
        private String mUpdateUrl;

        //4,apk的下载路径
        private String mTargetPath;
        //5,是否是post请求，默认是get
        private boolean isPost;
        //6,自定义参数
        private Map<String, String> params;
        //7,是否隐藏对话框下载进度条
        private boolean mHideDialog;
        private boolean mShowIgnoreVersion;
        private boolean dismissNotificationProgress;
        private boolean mOnlyWifi;

        public Map<String, String> getParams() {
            return params;
        }

        /**
         * 自定义请求参数
         *
         * @param params 自定义请求参数
         * @return Builder
         */
        public Builder setParams(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public boolean isPost() {
            return isPost;
        }

        /**
         * 是否是post请求，默认是get
         *
         * @param post 是否是post请求，默认是get
         * @return Builder
         */
        public Builder setPost(boolean post) {
            isPost = post;
            return this;
        }

        public String getTargetPath() {
            return mTargetPath;
        }

        /**
         * apk的下载路径，
         *
         * @param targetPath apk的下载路径，
         * @return Builder
         */
        public Builder setTargetPath(String targetPath) {
            mTargetPath = targetPath;
            return this;
        }

        public Activity getActivity() {
            return mActivity;
        }

        /**
         * 是否是post请求，默认是get
         *
         * @param activity 当前提示的Activity
         * @return Builder
         */
        public Builder setActivity(Activity activity) {
            mActivity = activity;
            return this;
        }

        public HttpManager getHttpManager() {
            return mHttpManager;
        }

        /**
         * 设置网络工具
         *
         * @param httpManager 自己实现的网络对象
         * @return Builder
         */
        public Builder setHttpManager(HttpManager httpManager) {
            mHttpManager = httpManager;
            return this;
        }

        public String getUpdateUrl() {
            return mUpdateUrl;
        }

        /**
         * 更新地址
         *
         * @param updateUrl 更新地址
         * @return Builder
         */
        public Builder setUpdateUrl(String updateUrl) {
            mUpdateUrl = updateUrl;
            return this;
        }


        /**
         * @return 生成app管理器
         */
        public UpdateAppManager build() {
            //校验
            if (getActivity() == null || getHttpManager() == null || TextUtils.isEmpty(getUpdateUrl())) {
                throw new NullPointerException("必要参数不能为空");
            }
            if (TextUtils.isEmpty(getTargetPath())) {
                //sd卡是否存在
                String path = "";
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
                    try {
                        path = getActivity().getExternalCacheDir().getAbsolutePath();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (TextUtils.isEmpty(path)) {
                        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                    }
                } else {
                    path = getActivity().getCacheDir().getAbsolutePath();
                }
                setTargetPath(path);
            }
            return new UpdateAppManager(this);
        }

        /**
         * 是否隐藏对话框下载进度条
         *
         * @param b 是否隐藏对话框下载进度条
         * @return Builder
         */
        public Builder hideDialogOnDownloading(boolean b) {
            mHideDialog = b;
            return this;
        }

        /**
         * @return 是否影藏对话框
         */
        public boolean isHideDialog() {
            return mHideDialog;
        }

        /**
         * 显示忽略版本
         *
         * @return 是否忽略版本
         */
        public Builder showIgnoreVersion() {
            mShowIgnoreVersion = true;
            return this;
        }

        public boolean isShowIgnoreVersion() {
            return mShowIgnoreVersion;
        }

        /**
         * 不显示通知栏进度条
         *
         * @return 是否显示进度条
         */
        public Builder dismissNotificationProgress() {
            dismissNotificationProgress = true;
            return this;
        }

        public boolean isDismissNotificationProgress() {
            return dismissNotificationProgress;
        }

        public Builder setOnlyWifi() {
            mOnlyWifi = true;
            return this;
        }

        public boolean isOnlyWifi() {
            return mOnlyWifi;
        }
    }

}
