package com.smile.guodian.contract;


import com.smile.guodian.base.BasePresenter;
import com.smile.guodian.base.BaseView;
import com.smile.guodian.model.entity.HomeBottom;
import com.smile.guodian.model.entity.HomeTop;



public interface HomeContract {
    interface View extends BaseView {
        void show(HomeTop bean);

        void show(HomeBottom bean);

        void loading();

        void networkError();

        void error(String msg);

        void showFailed(String msg);
    }

    interface Presenter extends BasePresenter {
        void start(int type);

        void start(int type, int page, int pageSize);
    }
}
