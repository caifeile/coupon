package com.huafon.singletonscans.feature.login.interactor;

import android.os.Handler;

import com.huafon.singletonscans.GlobalApp;
import com.huafon.singletonscans.feature.login.bean.UserInfo;
import com.huafon.singletonscans.feature.login.contract.LoginContract;
import com.huafon.sdk.myokhttp.MyOkHttp;
import com.huafon.sdk.myutil.StringUtils;
import com.tsy.sdk.acache.ACache;

/**
 * Login Interactor层
 * Created by tsy on 16/8/30.
 */
public class LoginInteractor implements LoginContract.Interactor {

    private MyOkHttp mApi;
    private ACache mCache;

    //缓存key
    private final String CACHE_KEY_USERINFO = "CACHE_KEY_USERINFO";

    public LoginInteractor() {
        mApi = MyOkHttp.get();
        mCache = ACache.get(GlobalApp.getInstance().getContext());
    }

    @Override
    public void doLogin(String phone, String password, final LoginCallback callback) {
        //模拟异步网络请求登录

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                UserInfo userInfo = new UserInfo();
                userInfo.uid = "1212121";
                userInfo.userName = "tsy12321";
                userInfo.token = "wqw13w12312wsqw12";

                //存入缓存
                mCache.put(CACHE_KEY_USERINFO, userInfo);

                callback.onSuccess(userInfo);
            }
        }, 2000);
    }

    @Override
    public boolean isLogin() {
        UserInfo userInfo = (UserInfo) mCache.getAsObject(CACHE_KEY_USERINFO);
        if(!StringUtils.isEmpty(userInfo.uid) && !StringUtils.isEmpty(userInfo.token)) {
            return true;
        }
        return false;
    }

    @Override
    public UserInfo getLoginUser() {
        return (UserInfo) mCache.getAsObject(CACHE_KEY_USERINFO);
    }
}
