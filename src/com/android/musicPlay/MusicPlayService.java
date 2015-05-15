package com.android.musicPlay;

import java.io.IOException;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;

import com.android.musicPlay.aidl.IMusicPlayClient;
import com.android.musicPlay.aidl.IMusicPlayService;
import com.android.musicPlay.common.Constant;
import com.android.musicPlay.model.Music;

/**
 * 后台播放音乐服务。通过MusicPlayController后台播放。
 **/
public class MusicPlayService extends Service implements OnCompletionListener,OnPreparedListener{

    private static final String TAG = "MusicPlayService";

    private static final int NOTIFICATION_ID = 1;
    //延迟更新界面信息的长度
    private static final int MESSAGE_TIME_DELAY = 200;
    private static final int MESSAGE_UPDATE_PROGRESS = 0X10;
    public MediaPlayer mMediaPlayer;
    //当前正在播放的歌曲
    private Music mMusic;
    //播放的歌曲列表
    private List<Music> mMusics;
    //客户端AIDL，更新客户端
    private IMusicPlayClient mClinet;
    //播放模式，默认是顺序播放.
    private int mMode = Constant.PLAY_MODE_SEQUENCE;
    
    private HandlerThread mHandlerThread;
    private MusicPlayHandler mMusicPlayHandler;
    
    private NotificationManager mNotificationManager;
    private Notification mNotification = null;
    
    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
        mHandlerThread = new HandlerThread(TAG);
        mHandlerThread.start();
        Looper looper = mHandlerThread.getLooper();
        if (looper == null) {
            looper = getMainLooper();
        }
        mMusicPlayHandler = new MusicPlayHandler(looper);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return mServiceBinder;
    } 

    private void setForeground(String text) {
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification = new Notification();
        mNotification.tickerText = text;
        mNotification.icon = R.drawable.ic_media_play;
        mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
        mNotification.setLatestEventInfo(getApplicationContext(), "RandomMusicPlayer",
                text, pi);
        startForeground(NOTIFICATION_ID, mNotification);
    }
    
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mHandlerThread != null) {
            mHandlerThread.quit();
        }
    }
    
    private void playMusic(Music item){
        if (item == null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                try {
                    mClinet.playPasue(mMusic);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return;
        }
        mMusic = item;
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(item.mPath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            updateProgress(0);
            setForeground("Playing...");
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void onCompletion(MediaPlayer mp) {
        setForeground("Prepared...");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // TODO Auto-generated method stub
//        updateProgress(0);
    }
    
    /**
     * 服务器的Binder对象，传给客户端控制服务器后台音频文件的音频文件的播放
     **/
    private IBinder mServiceBinder = new IMusicPlayService.Stub() {

        @Override
        public void setMusicPlayClient(IMusicPlayClient client)
                throws RemoteException {
            mClinet = client;
        }

        @Override
        public void setMode(int mode) throws RemoteException {
            mMode = mode;
        }

        @Override
        public void play(Music item, List<Music> data) throws RemoteException {
            mMusics = data;
//            mClinet.update(item,progress);
            playMusic(item);
        }

        public void seekto(int progress) throws RemoteException {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer
                        .seekTo(progress * mMediaPlayer.getDuration() / 100);
                updateProgress(0);
            }
        };
        
        @Override
        public void pause() throws RemoteException {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mClinet.playPasue(mMusic);
                setForeground("Pasue...");
            } else {
                mMediaPlayer.start();
                updateProgress(0);
                setForeground("Playing...");
            }
        }

        @Override
        public void stop() throws RemoteException {
            // TODO Auto-generated method stub

        }

        @Override
        public void next() throws RemoteException {
            setForeground("Next...");
            int index = mMusics.indexOf(mMusic);
            Music music = null;
            if (index + 1 < mMusics.size()) {
                music = mMusics.get(index+1);
            } else {
                music = mMusics.get(0);
            }
            playMusic(music);
        }

    };

    private class MusicPlayHandler extends Handler {

        public MusicPlayHandler(Looper looper) {
            // TODO Auto-generated constructor stub
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what != MESSAGE_UPDATE_PROGRESS) {
                return;
            }
             if(mMediaPlayer == null || mClinet == null){
                 return;
             }
            if (mMediaPlayer.isPlaying()) {
                int progress = mMediaPlayer.getCurrentPosition() * 100
                        / mMediaPlayer.getDuration();
                try {
                    mClinet.update(mMusic, progress);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                updateProgress(MESSAGE_TIME_DELAY);
            }
        }
    }
    
    private void updateProgress(int delayTime){
        if (mMusicPlayHandler.hasMessages(MESSAGE_UPDATE_PROGRESS)) {
            mMusicPlayHandler.removeMessages(MESSAGE_UPDATE_PROGRESS);;
        }
        Message msg = Message.obtain();
        msg.what = MESSAGE_UPDATE_PROGRESS;
        mMusicPlayHandler.sendMessageDelayed(msg, delayTime);
    }
}
