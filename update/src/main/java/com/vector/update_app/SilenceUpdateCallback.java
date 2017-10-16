package com.vector.update_app;

import com.vector.update_app.service.DownloadService;
import com.vector.update_app.utils.AppUpdateUtils;

import java.io.File;

/**
 * Created by Vector
 * on 2017/7/20 0020.
 */

public class SilenceUpdateCallback extends UpdateCallback {
    @Override
    protected final void hasNewApp(final UpdateAppBean updateApp, final UpdateAppManager updateAppManager) {
        //添加信息
        UpdateAppBean updateAppBean = updateAppManager.fillUpdateAppData();
        //设置不显示通知栏下载进度
        updateAppBean.dismissNotificationProgress(true);

        if (AppUpdateUtils.appIsDownloaded(updateApp)) {
            showDialog(updateAppManager);
        } else {
            //假如是onlyWifi,则进行判断网络环境
            if (updateApp.isOnlyWifi() && !AppUpdateUtils.isWifi(updateAppManager.getContext())) {
                //要求是wifi下，且当前不是wifi环境
                return;
            }
            updateAppManager.download(new DownloadService.DownloadCallback() {
                @Override
                public void onStart() {

                }

                @Override
                public void onProgress(float progress, long totalSize) {

                }

                @Override
                public void setMax(long totalSize) {

                }

                @Override
                public boolean onFinish(File file) {
                    showDialog(updateAppManager);
                    return false;
                }


                @Override
                public void onError(String msg) {

                }
            });
        }
    }

    /**
     * @param updateAppManager 网路接口
     */
    protected void showDialog(UpdateAppManager updateAppManager) {
        updateAppManager.showDialogFragment();
    }
}
