package com.imooc.lib_audio.mediaplayer.view;

import android.animation.Animator;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.imooc.lib_audio.R;
import com.imooc.lib_audio.mediaplayer.core.AudioController;
import com.imooc.lib_audio.mediaplayer.events.AudioLoadEvent;
import com.imooc.lib_audio.mediaplayer.events.AudioPauseEvent;
import com.imooc.lib_audio.mediaplayer.events.AudioStartEvent;
import com.imooc.lib_audio.mediaplayer.model.AudioBean;
import com.imooc.lib_audio.mediaplayer.view.adpater.MusicPagerAdapter;
import com.imooc.lib_image_loader.app.ImageLoaderManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * 音乐播放页面唱针布局
 */
public class IndictorView extends RelativeLayout
        implements ViewPager.OnPageChangeListener {

    private Context mContext;

    /*
     * view相关
     */
    private ImageView mImageView;
    private ViewPager mViewPager;
    private MusicPagerAdapter mAdapter;
    /*
     * data
     */
    private AudioBean mAudioBean; //当前播放歌曲
    private ArrayList<AudioBean> mQueue; //播放队列

    public IndictorView(Context context) {
        this(context, null);
    }

    public IndictorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndictorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        EventBus.getDefault().register(this);
        initData();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initData() {
        mQueue = AudioController.getInstance().getQueue();
        mAudioBean = AudioController.getInstance().getNowPlaying();
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.indictor_view, this);
        mImageView = rootView.findViewById(R.id.tip_view);
        mViewPager = rootView.findViewById(R.id.view_pager);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mAdapter = new MusicPagerAdapter(mQueue, mContext);
        mViewPager.setAdapter(mAdapter);
        showLoadView(false);
        //要在UI初始化完，否则会多一次listener响应
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        AudioController.getInstance().setPlayIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                //滑动结束
                showPlayView();
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
                //在滑动过程中
                showPauseView();
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioLoadEvent(AudioLoadEvent event) {
        mAudioBean = event.mAudioBean;
        //更新viewpager为load态
        showLoadView(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioStartEvent(AudioStartEvent event) {
        //更新viewpager为播放态
        showPlayView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudioPauseEvent(AudioPauseEvent event) {
        //更新viewpager为暂停态
        showPauseView();
    }

    private void showLoadView(boolean isSmooth) {
        mViewPager.setCurrentItem(mQueue.indexOf(mAudioBean), isSmooth);
    }

    private void showPlayView() {
        Animator anim = mAdapter.getAnim(mViewPager.getCurrentItem());
        if (anim != null) {
            if (anim.isPaused()) {
                anim.resume();
            } else {
                anim.start();
            }
        }
    }

    private void showPauseView() {
        Animator anim = mAdapter.getAnim(mViewPager.getCurrentItem());
        if (anim != null) {
            anim.pause();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }
}
