package com.smile.guodian;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.smile.guodian.model.entity.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property User_id = new Property(1, String.class, "user_id", false, "USER_ID");
        public final static Property Email = new Property(2, String.class, "email", false, "EMAIL");
        public final static Property Realname = new Property(3, String.class, "realname", false, "REALNAME");
        public final static Property Paypwd = new Property(4, String.class, "paypwd", false, "PAYPWD");
        public final static Property Sex = new Property(5, String.class, "sex", false, "SEX");
        public final static Property Birthday = new Property(6, String.class, "birthday", false, "BIRTHDAY");
        public final static Property User_money = new Property(7, String.class, "user_money", false, "USER_MONEY");
        public final static Property Frozen_money = new Property(8, String.class, "frozen_money", false, "FROZEN_MONEY");
        public final static Property Distribut_money = new Property(9, String.class, "distribut_money", false, "DISTRIBUT_MONEY");
        public final static Property Pay_points = new Property(10, String.class, "pay_points", false, "PAY_POINTS");
        public final static Property Reg_time = new Property(11, String.class, "reg_time", false, "REG_TIME");
        public final static Property Last_login = new Property(12, String.class, "last_login", false, "LAST_LOGIN");
        public final static Property Last_ip = new Property(13, String.class, "last_ip", false, "LAST_IP");
        public final static Property Mobile = new Property(14, String.class, "mobile", false, "MOBILE");
        public final static Property Head_pic = new Property(15, String.class, "head_pic", false, "HEAD_PIC");
        public final static Property Nickname = new Property(16, String.class, "nickname", false, "NICKNAME");
        public final static Property Level = new Property(17, String.class, "level", false, "LEVEL");
        public final static Property Discount = new Property(18, String.class, "discount", false, "DISCOUNT");
        public final static Property Total_amount = new Property(19, String.class, "total_amount", false, "TOTAL_AMOUNT");
        public final static Property Is_lock = new Property(20, String.class, "is_lock", false, "IS_LOCK");
        public final static Property Is_distribut = new Property(21, String.class, "is_distribut", false, "IS_DISTRIBUT");
        public final static Property Token = new Property(22, String.class, "token", false, "TOKEN");
        public final static Property Username = new Property(23, String.class, "username", false, "USERNAME");
        public final static Property Personnal_statement = new Property(24, String.class, "personnal_statement", false, "PERSONNAL_STATEMENT");
    }


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"USER_ID\" TEXT," + // 1: user_id
                "\"EMAIL\" TEXT," + // 2: email
                "\"REALNAME\" TEXT," + // 3: realname
                "\"PAYPWD\" TEXT," + // 4: paypwd
                "\"SEX\" TEXT," + // 5: sex
                "\"BIRTHDAY\" TEXT," + // 6: birthday
                "\"USER_MONEY\" TEXT," + // 7: user_money
                "\"FROZEN_MONEY\" TEXT," + // 8: frozen_money
                "\"DISTRIBUT_MONEY\" TEXT," + // 9: distribut_money
                "\"PAY_POINTS\" TEXT," + // 10: pay_points
                "\"REG_TIME\" TEXT," + // 11: reg_time
                "\"LAST_LOGIN\" TEXT," + // 12: last_login
                "\"LAST_IP\" TEXT," + // 13: last_ip
                "\"MOBILE\" TEXT," + // 14: mobile
                "\"HEAD_PIC\" TEXT," + // 15: head_pic
                "\"NICKNAME\" TEXT," + // 16: nickname
                "\"LEVEL\" TEXT," + // 17: level
                "\"DISCOUNT\" TEXT," + // 18: discount
                "\"TOTAL_AMOUNT\" TEXT," + // 19: total_amount
                "\"IS_LOCK\" TEXT," + // 20: is_lock
                "\"IS_DISTRIBUT\" TEXT," + // 21: is_distribut
                "\"TOKEN\" TEXT," + // 22: token
                "\"USERNAME\" TEXT," + // 23: username
                "\"PERSONNAL_STATEMENT\" TEXT);"); // 24: personnal_statement
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindString(2, user_id);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(3, email);
        }
 
        String realname = entity.getRealname();
        if (realname != null) {
            stmt.bindString(4, realname);
        }
 
        String paypwd = entity.getPaypwd();
        if (paypwd != null) {
            stmt.bindString(5, paypwd);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(6, sex);
        }
 
        String birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindString(7, birthday);
        }
 
        String user_money = entity.getUser_money();
        if (user_money != null) {
            stmt.bindString(8, user_money);
        }
 
        String frozen_money = entity.getFrozen_money();
        if (frozen_money != null) {
            stmt.bindString(9, frozen_money);
        }
 
        String distribut_money = entity.getDistribut_money();
        if (distribut_money != null) {
            stmt.bindString(10, distribut_money);
        }
 
        String pay_points = entity.getPay_points();
        if (pay_points != null) {
            stmt.bindString(11, pay_points);
        }
 
        String reg_time = entity.getReg_time();
        if (reg_time != null) {
            stmt.bindString(12, reg_time);
        }
 
        String last_login = entity.getLast_login();
        if (last_login != null) {
            stmt.bindString(13, last_login);
        }
 
        String last_ip = entity.getLast_ip();
        if (last_ip != null) {
            stmt.bindString(14, last_ip);
        }
 
        String mobile = entity.getMobile();
        if (mobile != null) {
            stmt.bindString(15, mobile);
        }
 
        String head_pic = entity.getHead_pic();
        if (head_pic != null) {
            stmt.bindString(16, head_pic);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(17, nickname);
        }
 
        String level = entity.getLevel();
        if (level != null) {
            stmt.bindString(18, level);
        }
 
        String discount = entity.getDiscount();
        if (discount != null) {
            stmt.bindString(19, discount);
        }
 
        String total_amount = entity.getTotal_amount();
        if (total_amount != null) {
            stmt.bindString(20, total_amount);
        }
 
        String is_lock = entity.getIs_lock();
        if (is_lock != null) {
            stmt.bindString(21, is_lock);
        }
 
        String is_distribut = entity.getIs_distribut();
        if (is_distribut != null) {
            stmt.bindString(22, is_distribut);
        }
 
        String token = entity.getToken();
        if (token != null) {
            stmt.bindString(23, token);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(24, username);
        }
 
        String personnal_statement = entity.getPersonnal_statement();
        if (personnal_statement != null) {
            stmt.bindString(25, personnal_statement);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindString(2, user_id);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(3, email);
        }
 
        String realname = entity.getRealname();
        if (realname != null) {
            stmt.bindString(4, realname);
        }
 
        String paypwd = entity.getPaypwd();
        if (paypwd != null) {
            stmt.bindString(5, paypwd);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(6, sex);
        }
 
        String birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindString(7, birthday);
        }
 
        String user_money = entity.getUser_money();
        if (user_money != null) {
            stmt.bindString(8, user_money);
        }
 
        String frozen_money = entity.getFrozen_money();
        if (frozen_money != null) {
            stmt.bindString(9, frozen_money);
        }
 
        String distribut_money = entity.getDistribut_money();
        if (distribut_money != null) {
            stmt.bindString(10, distribut_money);
        }
 
        String pay_points = entity.getPay_points();
        if (pay_points != null) {
            stmt.bindString(11, pay_points);
        }
 
        String reg_time = entity.getReg_time();
        if (reg_time != null) {
            stmt.bindString(12, reg_time);
        }
 
        String last_login = entity.getLast_login();
        if (last_login != null) {
            stmt.bindString(13, last_login);
        }
 
        String last_ip = entity.getLast_ip();
        if (last_ip != null) {
            stmt.bindString(14, last_ip);
        }
 
        String mobile = entity.getMobile();
        if (mobile != null) {
            stmt.bindString(15, mobile);
        }
 
        String head_pic = entity.getHead_pic();
        if (head_pic != null) {
            stmt.bindString(16, head_pic);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(17, nickname);
        }
 
        String level = entity.getLevel();
        if (level != null) {
            stmt.bindString(18, level);
        }
 
        String discount = entity.getDiscount();
        if (discount != null) {
            stmt.bindString(19, discount);
        }
 
        String total_amount = entity.getTotal_amount();
        if (total_amount != null) {
            stmt.bindString(20, total_amount);
        }
 
        String is_lock = entity.getIs_lock();
        if (is_lock != null) {
            stmt.bindString(21, is_lock);
        }
 
        String is_distribut = entity.getIs_distribut();
        if (is_distribut != null) {
            stmt.bindString(22, is_distribut);
        }
 
        String token = entity.getToken();
        if (token != null) {
            stmt.bindString(23, token);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(24, username);
        }
 
        String personnal_statement = entity.getPersonnal_statement();
        if (personnal_statement != null) {
            stmt.bindString(25, personnal_statement);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // user_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // email
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // realname
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // paypwd
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // sex
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // birthday
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // user_money
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // frozen_money
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // distribut_money
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // pay_points
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // reg_time
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // last_login
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // last_ip
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // mobile
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // head_pic
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // nickname
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // level
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // discount
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // total_amount
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // is_lock
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // is_distribut
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // token
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // username
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24) // personnal_statement
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUser_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setEmail(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRealname(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPaypwd(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSex(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setBirthday(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setUser_money(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setFrozen_money(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setDistribut_money(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setPay_points(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setReg_time(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setLast_login(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setLast_ip(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setMobile(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setHead_pic(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setNickname(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setLevel(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setDiscount(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setTotal_amount(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setIs_lock(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setIs_distribut(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setToken(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setUsername(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setPersonnal_statement(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(User entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
