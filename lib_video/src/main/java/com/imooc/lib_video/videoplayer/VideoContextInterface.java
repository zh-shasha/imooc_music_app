package com.imooc.lib_video.videoplayer;
//与应用层通信的接口
public interface VideoContextInterface {
    void onVideoSuccess();

    void onVideoFailed();

    void onVideoComplete();
}
