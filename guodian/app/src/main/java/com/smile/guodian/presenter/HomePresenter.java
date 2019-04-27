package com.smile.guodian.presenter;

import android.content.Context;

import com.smile.guodian.model.HomeLoadModel;
import com.smile.guodian.model.entity.HomeBottom;
import com.smile.guodian.model.entity.HomeTop;
import com.smile.guodian.model.impl.HomeModelImpl;
import com.smile.guodian.contract.HomeContract;


public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View view;
    private HomeLoadModel loadModel;
    private Context context;

    public void init(HomeContract.View view, Context context) {
        loadModel = new HomeModelImpl();
        this.view = view;
        this.context = context;
        this.view.initView();
    }

    @Override
    public void start(int type) {
        view.loading();
        loadModel.load(new OnLoadListener<HomeTop>() {
            @Override
            public void onSuccess(HomeTop bean) {
                view.show(bean);
            }

            @Override
            public void onError(String state, String msg) {
                view.error(msg);
            }

            @Override
            public void networkError() {
                view.networkError();
            }
        }, context, type);

    }

    @Override
    public void start(int type, int page, int pageSize) {
        view.loading();
        loadModel.load(new OnLoadListener<HomeBottom>() {
            @Override
            public void onSuccess(HomeBottom success) {
                view.show(success);
            }

            @Override
            public void onError(String state, String msg) {
                view.error(msg);
            }

            @Override
            public void networkError() {
                view.networkError();
            }
        }, context, type, page, pageSize);
    }

    @Override
    public void start() {

    }
}
