package com.android.musicPlay;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.musicPlay.common.MusicPlayController;
import com.android.musicPlay.common.MusicPlayController.OnMusicPlayStatusListener;
import com.android.musicPlay.model.Music;

public class BaseActivity extends FragmentActivity implements
        OnClickListener ,OnMusicPlayStatusListener,OnSeekBarChangeListener{
    
    /**
     * 播放器控制器
     **/
    protected MusicPlayController mController = null;
    /**
     * title栏中的回退按钮
     **/
    protected Button mBackBtn;
    /**
     * title名字
     **/
    protected TextView mTitle;
    /**
     * title栏中的设置按钮
     **/
    protected Button mSettingBtn;
    /**
     * 控制面板中的歌曲名字
     **/
    protected TextView mMusicName;
    /**
     * 控制面板中的歌手名字
     **/
    protected TextView mArtist;
    /**
     * 控制面中的播放按钮
     **/
    protected Button mPlayBtn;
    /**
     * 控制面板中的播放下一首歌曲
     **/
    protected Button mPlayNext;
    /**
     * 控制面板中的进度条
     **/
    protected SeekBar mSeekBar;
    /**
     * 控制面板中手动拖动SeekBar后的进度值
     **/
    protected int mSeekBarProgress;
    
    /**
     * 扩展布局，子类实例化布局文件后，添加到此布局中。
     **/
    protected RelativeLayout mExpendLayout;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        mController = MusicPlayController.newInstance(getApplication());
        setContentView(R.layout.base_activity);
        initCommonView();
        initContentView();
    }

    /**
     * 初始化内容区域布局，子类实现该方法，并填充相应布局
     **/
    protected void initContentView(){};
    /**
     * 初始化公用组件<title，播放按钮>
     **/
    protected void initCommonView(){
        mBackBtn = (Button) findViewById(R.id.back);
        mBackBtn.setOnClickListener(this);
        
        mTitle = (TextView) findViewById(R.id.title);
        
        mSettingBtn = (Button) findViewById(R.id.setting);
        mSettingBtn.setOnClickListener(this);
        
        mExpendLayout = (RelativeLayout) findViewById(R.id.content_layout);
        
        mArtist = (TextView) findViewById(R.id.play_singer);
        mMusicName = (TextView) findViewById(R.id.play_song_name);
        mPlayBtn = (Button) findViewById(R.id.play_btn_pause);
        mPlayBtn.setOnClickListener(this);
        mPlayNext = (Button) findViewById(R.id.play_btn_next);
        mPlayNext.setOnClickListener(this);
        
        mSeekBar = (SeekBar) findViewById(R.id.play_progress);
        mSeekBar.setOnSeekBarChangeListener(this);
    }
 
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mController.setOnMusicPlayStatusListener(this);
    }
    
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back){
            finish();
            return;
        }
        if (v.getId() == R.id.setting){
            Toast.makeText(this, "设置", Toast.LENGTH_LONG).show();
            return;
        }
        if (v.getId() == R.id.play_btn_pause) {
            mController.pasue();
            return;
        }
        if (v.getId() == R.id.play_btn_next) {
            mController.next();
            return;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        if (fromUser){
            mSeekBarProgress = progress;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mController.seekTo(mSeekBarProgress);
    }

    @Override
    public void update(final Music music, final int progress) {
        runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                mPlayBtn.setBackgroundResource(R.drawable.ic_media_pause);
                mMusicName.setText(music.mMusicTitle);
                mArtist.setText(music.mArtist);
                mSeekBar.setProgress(progress);
            }
        });
    }

    @Override
    public void playPasue(Music music) {
        runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                mPlayBtn.setBackgroundResource(R.drawable.ic_media_play);
            }
        });
    }

    @Override
    public void playStop(Music music) {
        // TODO Auto-generated method stub
        
    }
}
