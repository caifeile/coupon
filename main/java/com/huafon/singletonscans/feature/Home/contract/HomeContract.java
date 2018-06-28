package com.huafon.singletonscans.feature.Home.contract;

import com.huafon.singletonscans.Base.BasePresenter;
import com.huafon.singletonscans.Base.BaseView;

/**
 * Created by tsy on 16/8/30.
 */
public interface HomeContract {

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {
        void onPost();
    }

    interface Interactor {

    }
}
