package com.geekholt.launcherstarter;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.geekholt.launcherstarter.util.LaunchTimer;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;

import cn.jpush.android.api.JPushInterface;

/**
 * @Author：wuhaoteng
 * @Date:2019/7/4
 * @Desc：
 */
public class App extends Application {
    private static Application mApplication;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            // 一些处理
        }
    };
    private String mDeviceId;

    public static Application getApplication() {
        return mApplication;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        LaunchTimer.startRecord();
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        initAMap();
        initWeex();
        initStetho();
        initFresco();
        initDeviceId();
        initJPush();
    }


    private void initAMap() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }


    private void initWeex() {
        InitConfig config = new InitConfig.Builder().build();
        WXSDKEngine.initialize(this, config);
    }


    private void initStetho() {
        //假设这个方法中有一定要在主线程执行的代码（虽然handler只要传入looper也可以在子线程用，这里我们只是举一个例子）
        Handler handler = new Handler();
        Stetho.initializeWithDefaults(this);
    }


    private void initFresco() {
        Fresco.initialize(this);
    }


    private void initDeviceId() {
        // 真正自己的代码
        TelephonyManager tManager = (TelephonyManager) getSystemService(
                Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mDeviceId = tManager.getDeviceId();
    }


    private void initJPush() {
        //这个方法依赖与initDeviceId，所以存在先后关系
        JPushInterface.init(this);
        JPushInterface.setAlias(this, 0, mDeviceId);
    }
}
