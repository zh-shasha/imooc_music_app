package com.imooc.imooc_voice.api;

import com.imooc.imooc_voice.view.friend.model.BaseFriendModel;
import com.imooc.imooc_voice.view.login.user.User;
import com.imooc.lib_network.okhttp.CommonOkHttpClient;
import com.imooc.lib_network.okhttp.listener.DisposeDataHandle;
import com.imooc.lib_network.okhttp.listener.DisposeDataListener;
import com.imooc.lib_network.okhttp.request.CommonRequest;
import com.imooc.lib_network.okhttp.request.RequestParams;


/**
 * 请求中心
 */
public class RequestCenter {

    static class HttpConstants {
        private static final String ROOT_URL = "http://imooc.com/api";

        /**
         * 首页请求接口
         */
        private static String HOME_RECOMMAND = ROOT_URL + "/product/home_recommand.php";

        private static String HOME_FRIEND = ROOT_URL + "/product/home_friend.php";

        private static String HOME_RECOMMAND_MORE = ROOT_URL + "/product/home_recommand_more.php";

        /**
         * 登陆接口
         */
        public static String LOGIN = ROOT_URL + "/user/login_phone.php";
    }

    //根据参数发送所有post请求
    public static void postRequest(String url, RequestParams params, DisposeDataListener listener,
                                   Class<?> clazz) {
        CommonOkHttpClient.post(CommonRequest.
                createPostRequest(url, params), new DisposeDataHandle(listener, clazz));
    }


    /**
     * 用户登陆请求
     */
    public static void login(DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("mb", "18734924592");
        params.put("pwd", "999999q");
        RequestCenter.postRequest(HttpConstants.LOGIN, params, listener, User.class);
    }

    /**
     * 朋友页面请求
     *
     * @param listener
     */
    public static void requestFriendData(DisposeDataListener listener) {
        postRequest(HttpConstants.HOME_FRIEND,
                null,
                listener, BaseFriendModel.class);
    }
}
