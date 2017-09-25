package com.atgc.cotton.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.atgc.cotton.App;
import com.atgc.cotton.Constants;
import com.atgc.cotton.socket.SocketHandler;

/**
 * Created by Johnny on 2017/3/24.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Constants.Action.NET_CONENECT_CHANGE)) {
            //得到网络连接管理器
            ConnectivityManager connectionManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //通过管理器得到网络实例
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
            //判断是否连接
            if (networkInfo != null && networkInfo.isAvailable()) {
                Log.i("info", "===========网络可用!");
                SocketHandler.getInstance().sendMessage("token=" + App.getInstance().getToken());//重新登录
            }
        }
    }
}
