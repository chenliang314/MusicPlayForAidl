package com.android.musicPlay.common;

import java.util.List;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.android.musicPlay.MusicPlayService;
import com.android.musicPlay.aidl.IMusicPlayClient;
import com.android.musicPlay.aidl.IMusicPlayService;
import com.android.musicPlay.model.Music;

/**
 * 使用AIDL与Service夸进程通讯
 **/
public class MusicPlayController {

    private static MusicPlayController mInstance;
    /**
     * UI界面实现该类，便于更新UI状态
     **/
    private OnMusicPlayStatusListener mListener;
    /**
     * 服务端AIDL实现，用于控制MusicPlayService
     **/
    private IMusicPlayService mServiceAIDL;
    /**
     * 客户端AIDL实现，用于更新客户端界面一些操作
     **/
    private IMusicPlayClient mClientAIDL = new IMusicPlayClient.Stub() {

        @Override
        public void update(Music music,int progress) throws RemoteException {
            if (mListener != null) {
                mListener.update(music,progress);
            }
        }

        @Override
        public void playPasue(Music music) throws RemoteException {
            if (mListener != null) {
                mListener.playPasue(music);
            }
        }

        @Override
        public void playStop(Music music) throws RemoteException {
            if (mListener != null) {
                mListener.playStop(music);
            }
        }
    };

    /**
     * 实现客户端与服务器连接。在服务器重写IBinder方法，无需AIDL可以返回服务端相关的信息。
     **/
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceAIDL = IMusicPlayService.Stub.asInterface(service);
            try {
                mServiceAIDL.setMusicPlayClient(mClientAIDL);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                mServiceAIDL = null;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 处理与服务器进程断开连接
            mServiceAIDL = null;
        }
    };

    private MusicPlayController(Application app) {
        if (mServiceAIDL == null) {
            Intent intent = new Intent(app, MusicPlayService.class);
            app.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    public static MusicPlayController newInstance(Application app) {
        if (mInstance == null) {
            mInstance = new MusicPlayController(app);
        }
        return mInstance;
    }

    /**
     *设置界面状态监听器。在onResume中调用。
     **/
    public void setOnMusicPlayStatusListener(OnMusicPlayStatusListener listener){
        mListener = listener;
    }

    /**
     * @param mode 设置播放模式：单曲循环，顺序播放，随机播放
     * @see Constant.PLAY_MODE_REPEAT
     * @see Constant.PLAY_MODE_SEQUENCE
     * @see Constant.PLAY_MODE_RANDOM
     **/
    public void setMode(int mode){
        if (mServiceAIDL != null) {
            try {
                mServiceAIDL.setMode(mode);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 播放歌曲
     * 
     * @param model
     *            播放模式：单曲循环，顺序播放，随机播放
     **/
    public void play(Music music, List<Music> data) {
        try {
            mServiceAIDL.play(music, data);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 播放下一首歌曲
     **/
    public void next() {
        if (mServiceAIDL != null) {
            try {
                mServiceAIDL.next();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 跳转到指定的位置开始播放
     **/
    public void seekTo(int progress){
        if (mServiceAIDL != null) {
            try {
                mServiceAIDL.seekto(progress);;
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 暂停播放
     **/
    public void pasue() {
        if (mServiceAIDL != null) {
            try {
                mServiceAIDL.pause();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止播放
     **/
    public void stop() {
        if (mServiceAIDL != null) {
            try {
                mServiceAIDL.stop();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 歌曲播放状态监听器
     **/
    public interface OnMusicPlayStatusListener {
        /**
         * 更新歌曲控制面板
         **/
        public void update(Music music,int progress);

        /**
         * 服务播暂停播放歌曲
         **/
        public void playPasue(Music music);

        /**
         * 服务播停止播放歌曲
         **/
        public void playStop(Music music);
    }
}
