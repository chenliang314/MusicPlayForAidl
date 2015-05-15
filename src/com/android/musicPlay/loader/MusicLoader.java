package com.android.musicPlay.loader;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.android.musicPlay.model.Music;

public class MusicLoader extends BaseLoader<Music>{

    private Context mContext;
    
    public MusicLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public List<Music> loadInBackground() {
        ArrayList<Music> musics = new ArrayList<Music>();
        //1.内置SD卡
//         musics.addAll(scanLocalMusics(MediaStore.Audio.Media.INTERNAL_CONTENT_URI));
        //2.外置SD卡
        musics.addAll(scanLocalMusics(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI));
        return musics;
    }

    /** 
     *  查询url对应表上的音乐文件
     **/
    private  ArrayList<Music> scanLocalMusics(Uri uri){
        ArrayList<Music> musics = new ArrayList<Music>();
        Cursor c = mContext.getContentResolver().query(uri, null, null, null, null);
        if (c == null) {
            return musics;
        }
        c.moveToFirst();
        System.out.println("count:" + c.getCount());
        while(c.moveToNext()) {
            Music music = parseMusic(c);
            if (music != null) {
                musics.add(music);
            }
        }
        return musics;
    }

    
    public Music parseMusic(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        Music music = new Music();
        music.mID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
        music.mPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        music.mDisplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
        music.mMusicSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
        music.mLastModifyTime = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED));
        music.mMusicTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        music.mArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        music.mMusicDuration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        return music;
    }
}
