package com.smile.guodian.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {
    @Id
    Long id;
    String user_id;
    String email;
    String realname;
    String paypwd;
    String sex;
    String birthday;
    String user_money;
    String frozen_money;
    String distribut_money;
    String pay_points;
    String reg_time;
    String last_login;
    String last_ip;
    String mobile;
    String head_pic;
    String nickname;
    String level;
    String discount;
    String total_amount;
    String is_lock;
    String is_distribut;
    String token;
    String username;

    @Generated(hash = 586692638)
    public User() {
    }

    @Generated(hash = 1366730479)
    public User(Long id, String user_id, String email, String realname,
            String paypwd, String sex, String birthday, String user_money,
            String frozen_money, String distribut_money, String pay_points,
            String reg_time, String last_login, String last_ip, String mobile,
            String head_pic, String nickname, String level, String discount,
            String total_amount, String is_lock, String is_distribut, String token,
            String username) {
        this.id = id;
        this.user_id = user_id;
        this.email = email;
        this.realname = realname;
        this.paypwd = paypwd;
        this.sex = sex;
        this.birthday = birthday;
        this.user_money = user_money;
        this.frozen_money = frozen_money;
        this.distribut_money = distribut_money;
        this.pay_points = pay_points;
        this.reg_time = reg_time;
        this.last_login = last_login;
        this.last_ip = last_ip;
        this.mobile = mobile;
        this.head_pic = head_pic;
        this.nickname = nickname;
        this.level = level;
        this.discount = discount;
        this.total_amount = total_amount;
        this.is_lock = is_lock;
        this.is_distribut = is_distribut;
        this.token = token;
        this.username = username;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPaypwd() {
        return paypwd;
    }

    public void setPaypwd(String paypwd) {
        this.paypwd = paypwd;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUser_money() {
        return user_money;
    }

    public void setUser_money(String user_money) {
        this.user_money = user_money;
    }

    public String getFrozen_money() {
        return frozen_money;
    }

    public void setFrozen_money(String frozen_money) {
        this.frozen_money = frozen_money;
    }

    public String getDistribut_money() {
        return distribut_money;
    }

    public void setDistribut_money(String distribut_money) {
        this.distribut_money = distribut_money;
    }

    public String getPay_points() {
        return pay_points;
    }

    public void setPay_points(String pay_points) {
        this.pay_points = pay_points;
    }

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getLast_ip() {
        return last_ip;
    }

    public void setLast_ip(String last_ip) {
        this.last_ip = last_ip;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getIs_lock() {
        return is_lock;
    }

    public void setIs_lock(String is_lock) {
        this.is_lock = is_lock;
    }

    public String getIs_distribut() {
        return is_distribut;
    }

    public void setIs_distribut(String is_distribut) {
        this.is_distribut = is_distribut;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
