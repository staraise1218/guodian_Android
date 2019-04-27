package com.smile.guodian.presenter;


public interface OnLoadListener<T> {

    /**
     * 成功时的回调
     *
     * @param success 成功信息
     */
    void onSuccess(T success);

    /**
     * 失败时的回调
     *
     * @param msg 错误信息
     */
    void onError(String state, String msg);

    void networkError();
}
