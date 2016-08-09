package com.longyou.haitunpay;

import android.app.Application;

import com.longyou.haitunsdk.HaiTunPay;

/**
 * Created by CharryLi on 16/7/26.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        HaiTunPay.getInstance().initWithWechat("商户在海豚平台注册后获得的merId", "商户在海豚平台注册后获得的key", "");
        HaiTunPay.getInstance().init(this);
        HaiTunPay.setDebug(true);
    }
}
