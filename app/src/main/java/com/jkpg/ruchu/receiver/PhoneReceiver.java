package com.jkpg.ruchu.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.jkpg.ruchu.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by qindi on 2017/9/1.
 */

public class PhoneReceiver extends BroadcastReceiver {
    private static boolean incomingFlag = false;

    //    private String incomingNumber;
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d("PhoneReceiver: ");

        //拨打电话
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            incomingFlag = false;
            final String phoneNum = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            LogUtils.d("phoneNum: " + phoneNum);
        } else {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    final PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                    incomingFlag = true;
                    LogUtils.i("CALL IN RINGING :" + incomingNumber + "CallPhone");
                    EventBus.getDefault().post("CallPhone");
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (incomingFlag) {
                        LogUtils.i("CALL IN ACCEPT :" + incomingNumber);
                    }
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (incomingFlag) {
                        LogUtils.i("CALL IDLE");
                        EventBus.getDefault().post("EndPhone");
                    }
                    break;
            }
        }
    };
}