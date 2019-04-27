package com.smile.guodian.model;

import android.content.Context;

import com.smile.guodian.model.entity.HomeBottom;
import com.smile.guodian.model.entity.HomeTop;
import com.smile.guodian.presenter.OnLoadListener;


public interface HomeLoadModel extends LoadModel {

    void load(OnLoadListener<HomeTop> listener, Context context, int type);

    void load(OnLoadListener<HomeBottom> listener, Context context, int type, int page, int pageSize);
}
