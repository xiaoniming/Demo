package com.seu.ni.demo.Web;

/**
 * Created by ni on 2015/12/5.
 */
public interface HttpCallBackListener {

    void onFinish(String response);

    void onErr(Exception e);
}
