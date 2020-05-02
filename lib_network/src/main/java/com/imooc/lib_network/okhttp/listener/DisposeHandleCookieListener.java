package com.imooc.lib_network.okhttp.listener;
import java.util.ArrayList;

/**********************************************************
 * @文件名称：DisposeHandleCookieListener.java
 * @文件描述：当需要专门处理Cookie时创建此回调接口
 **********************************************************/
public interface DisposeHandleCookieListener extends DisposeDataListener
{
    public void onCookie(ArrayList<String> cookieStrLists);
}