package com.smile.guodian;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.smile.guodian.model.entity.User;
import com.smile.guodian.model.entity.History;

import com.smile.guodian.UserDao;
import com.smile.guodian.HistoryDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDaoConfig;
    private final DaoConfig historyDaoConfig;

    private final UserDao userDao;
    private final HistoryDao historyDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        historyDaoConfig = daoConfigMap.get(HistoryDao.class).clone();
        historyDaoConfig.initIdentityScope(type);

        userDao = new UserDao(userDaoConfig, this);
        historyDao = new HistoryDao(historyDaoConfig, this);

        registerDao(User.class, userDao);
        registerDao(History.class, historyDao);
    }
    
    public void clear() {
        userDaoConfig.clearIdentityScope();
        historyDaoConfig.clearIdentityScope();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public HistoryDao getHistoryDao() {
        return historyDao;
    }

}
