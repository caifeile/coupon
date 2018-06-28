package com.huafon.singletonscans.feature.Home.view;

import android.os.Bundle;

import com.huafon.singletonscans.Base.BaseActivity;
import com.huafon.singletonscans.feature.Home.contract.HomeContract;
import com.huafon.singletonscans.feature.Home.presenter.HomePresenter;
import com.huafon.singletonscans.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements HomeContract.View {

    private HomeContract.Presenter mPresnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mPresnter = new HomePresenter(this);
    }

    @OnClick(R.id.btnPost)
    public void onPost() {
        mPresnter.onPost();
    }

    @Override
    public void showToast(String msg) {

    }
}
