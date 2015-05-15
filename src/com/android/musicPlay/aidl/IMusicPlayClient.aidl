package com.android.musicPlay.aidl;

import com.android.musicPlay.model.Music;

interface IMusicPlayClient{

    /** 更新歌曲播放进度 */
    void update(inout Music music,int progress);

    /** 服务播暂停播放歌曲 */
    void playPasue(inout Music music);

    /** 服务播停止播放歌曲 */
    void playStop(inout Music music);
}