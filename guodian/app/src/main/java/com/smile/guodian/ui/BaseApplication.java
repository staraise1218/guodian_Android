package com.smile.guodian.ui;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.mob.MobSDK;
import com.smile.guodian.DaoMaster;
import com.smile.guodian.DaoSession;

import org.greenrobot.greendao.database.Database;

public class BaseApplication extends Application {
    public Context context;
    private String DBNAME = "guodian";
    public static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase(this);
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        MobSDK.init(this);


    }

    private void setupDatabase(Context context) {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(context, DBNAME);
        Database db = openHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }


}
