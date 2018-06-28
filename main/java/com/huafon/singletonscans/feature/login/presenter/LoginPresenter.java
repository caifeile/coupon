package com.huafon.singletonscans.feature.login.presenter;

import com.huafon.singletonscans.feature.login.contract.LoginContract;
import com.huafon.singletonscans.feature.login.bean.UserInfo;
import com.huafon.singletonscans.feature.login.interactor.LoginInteractor;
import com.huafon.sdk.myutil.StringUtils;

/**
 * Login presenter层
 * Created by tsy on 16/8/30.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;
    private LoginContract.Interactor mInteractor;

    public LoginPresenter(LoginContract.View view) {
        mView = view;
        mInteractor = new LoginInteractor();
    }

    @Override
    public void start() {

    }

    @Override
    public void onLogin(String phone, String password) {
        if(StringUtils.isEmpty(phone)) {
            mView.showToast("Empty phone");
            return;
        }

        if(StringUtils.isEmpty(password)) {
            mView.showToast("Empty password");
            return;
        }

        mInteractor.doLogin(phone, password, new LoginContract.Interactor.LoginCallback() {
            @Override
            public void onSuccess(UserInfo user_info) {
                mView.goHome();
            }

            @Override
            public void onFailure(String msg) {
                mView.showToast(msg);
            }
        });
    }
}
