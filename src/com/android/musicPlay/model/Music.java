package com.android.musicPlay.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 一首歌曲的具体实例
 **/
public class Music implements Parcelable {

    public int mID = 0;

    /**
     * 歌曲大小
     **/
    public long mMusicSize = 0;

    /**
     * 歌曲最后更新时间，可以用于排序
     **/
    public long mLastModifyTime = 0;

    /**
     * 歌曲播放时间
     **/
    public long mMusicDuration = 0;

    /**
     * 歌曲的保存路径
     **/
    public String mPath = "";

    /**
     * 显示名字（歌曲名 - 作者），主要显示在list和notification中
     **/
    public String mDisplayName = "";

    /**
     * 歌曲名
     **/
    public String mMusicTitle = "";

    /**
     * 歌手名字
     **/
    public String mArtist = "";

    public Music() {
    }

    public Music(Parcel source) {
        readFromParcel(source);
    }

    public void readFromParcel(Parcel source) {
        mID = source.readInt();
        mMusicSize = source.readLong();
        mLastModifyTime = source.readLong();
        mMusicDuration = source.readLong();
        mPath = source.readString();
        mDisplayName = source.readString();
        mMusicTitle = source.readString();
        mArtist = source.readString();

    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mID);
        dest.writeLong(mMusicSize);
        dest.writeLong(mLastModifyTime);
        dest.writeLong(mMusicDuration);
        dest.writeString(mPath);
        dest.writeString(mDisplayName);
        dest.writeString(mMusicTitle);
        dest.writeString(mArtist);
    }

    public static final Parcelable.Creator<Music> CREATOR = new Parcelable.Creator<Music>() {

        @Override
        public Music createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new Music(source);
        }

        @Override
        public Music[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Music[size];
        }
    };
    
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Music)) {
            return false;
        }
        Music music = (Music) o;
        if (mID == music.mID 
                && TextUtils.equals(mMusicTitle, music.mMusicTitle)
                && TextUtils.equals(mArtist, music.mArtist)) {
            return true;
        }
        return false;
    };
    
    @Override
    public int hashCode() {
        return (mID + mMusicTitle + mArtist).hashCode();
    }
}
